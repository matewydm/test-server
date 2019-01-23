package pl.darenie.dns.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.darenie.dns.core.firebase.notification.FcmNotificationService;
import pl.darenie.dns.core.mapper.SettlementMapper;
import pl.darenie.dns.dao.BillRepository;
import pl.darenie.dns.dao.SettlementRepository;
import pl.darenie.dns.jpa.*;
import pl.darenie.dns.model.dto.Accounter;
import pl.darenie.dns.model.dto.SettlementDTO;
import pl.darenie.dns.model.enums.*;
import pl.darenie.dns.model.exception.CoreException;
import pl.darenie.dns.model.rest.request.SettlementRequest;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@PropertySource("classpath:notification.properties")
public class SettlementController {

    private final BillRepository billRepository;
    private final SettlementRepository settlementRepository;
    private final SettlementMapper settlementMapper;
    private final UserController userController;
    private final FcmNotificationService notificationService;


    @Value("${title.new.bill}")
    public String NEW_BILL_TITLE;
    @Value("${body.new.bill}")
    public String NEW_BILL_BODY;
    @Value("${title.cyclic.accounting}")
    public String TITLE_CYCLIC_ACCOUNTING;
    @Value("${body.cyclic.accounting}")
    public String BODY_CYCLIC_ACCOUNTING;
    @Value("${title.settlement.remind}")
    public String SETTLEMENT_REMIND_TITLE;
    @Value("${body.settlement.remind}")
    public String SETTLEMENT_REMIND_BODY;

    @Autowired
    public SettlementController(BillRepository billRepository,
                                SettlementRepository settlementRepository,
                                SettlementMapper settlementMapper,
                                UserController userController,
                                FcmNotificationService notificationService) {
        this.billRepository = billRepository;
        this.settlementRepository = settlementRepository;
        this.settlementMapper = settlementMapper;
        this.userController = userController;
        this.notificationService = notificationService;
    }

    @Async
    @Transactional(propagation= Propagation.REQUIRED)
    public void processSettlements(Bill newBill) {
        List<Accounter> accounters = new ArrayList<>();
        newBill = billRepository.findById(newBill.getId());
        newBill.getUserHasCashBill().stream()
                .filter(uhcb -> uhcb.getType().equals(UserCashType.CHARGER))
                .forEach(charger -> {
                    Accounter accounter = new Accounter();
                    accounter.setToken(charger.getUserId());
                    accounter.setCharge(charger.getCash());
                    accounters.add(accounter);
                });
        newBill.getUserHasCashBill().stream()
                .filter(uhcb -> uhcb.getType().equals(UserCashType.PAYER))
                .forEach(payer -> {
                    Accounter accounter = new Accounter();
                    accounter.setToken(payer.getUserId());
                    accounter.setPayment(payer.getCash());
                    if (accounters.contains(accounter)) {
                        int index = accounters.indexOf(accounter);
                        accounter.setCharge(accounters.get(index).getCharge());
                        accounters.set(index, accounter);
                    } else {
                        accounter.setCharge(0.0);
                        accounters.add(accounter);
                    }
                });

        List<Accounter> toSettle = accounters.stream()
                .filter(a -> !Objects.equals(a.getPayment(), a.getCharge()))
                .collect(Collectors.toList());

        while (toSettle.size() > 1) {
            Accounter maxCharger = Collections.max(toSettle, new Accounter.ChargerComparator());
            toSettle.remove(maxCharger);
            Accounter maxPayer = Collections.max(toSettle, new Accounter.PayerComparator());
            toSettle.add(maxCharger);

            int indexMaxCharger = toSettle.indexOf(maxCharger);
            int indexMaxPayer = toSettle.indexOf(maxPayer);

            Double debt = maxCharger.getCharge() - maxCharger.getPayment();
            Double due = maxPayer.getPayment() - maxPayer.getCharge();

            Settlement settlement;
            if (Objects.equals(debt, due)) {
                settlement = new Settlement(newBill, maxPayer.getToken(), maxCharger.getToken(), debt, SettlementStatus.UNPAID);
                toSettle.remove(maxCharger);
                toSettle.remove(maxPayer);
            } else if (debt < due) {
                settlement = new Settlement(newBill, maxPayer.getToken(), maxCharger.getToken(), debt, SettlementStatus.UNPAID);
                maxPayer.setPayment(maxPayer.getPayment() - debt);
                toSettle.set(indexMaxPayer, maxPayer);
                toSettle.remove(maxCharger);
            } else {
                settlement = new Settlement(newBill, maxPayer.getToken(), maxCharger.getToken(), due, SettlementStatus.UNPAID);
                maxCharger.setCharge(maxCharger.getCharge() - due);
                toSettle.set(indexMaxCharger, maxCharger);
                toSettle.remove(maxPayer);
            }
            if (!newBill.getPriority().equals(BillPriority.CRITICAL) && userController.getAccounting(maxPayer.getToken(), maxCharger.getToken()) != null) {
                settlement.setVisibility(Visibility.INVISIBLE);
            }
            settlementRepository.save(settlement);
            cacheUserBalance();
            cacheFriendBalance();
        }
        Bill finalNewBill = newBill;
        toSettle.forEach(accounter -> {
            notificationService.sendUserNotification(accounter.getToken(), NEW_BILL_TITLE, NEW_BILL_BODY.replaceAll("\\$name\\$",finalNewBill.getName()));
        });
    }

    public void processInvisibleSettlement(Friend friend) {
        List<Settlement> invSettlements = settlementRepository.findByChargerIdAndPayerIdAndVisibility(friend.getUserId(), friend.getFriendId(), Visibility.INVISIBLE);
        Double charge = invSettlements.stream().mapToDouble(Settlement::getCharge).sum();
        if (charge > 0) {
            Settlement settlement = new Settlement(friend.getFriendId(), friend.getUserId(), charge, SettlementStatus.UNPAID);
            settlementRepository.save(settlement);
        }
        invSettlements.forEach(settlement -> {
            settlement.setVisibility(Visibility.ARCHIVED);
            settlementRepository.save(settlement);
        });
        notificationService.sendUserNotification(friend.getUser(), TITLE_CYCLIC_ACCOUNTING, BODY_CYCLIC_ACCOUNTING.replaceAll("\\$name\\$",friend.getFriend().getDisplayName()));
    }

    public void updateSettlementStatus(SettlementRequest settlementRequest) throws CoreException {
        Settlement settlement = settlementRepository.findOne(settlementRequest.getSettlementId());
        if (settlement == null) {
            throw new CoreException("Settlement doesn't exist", ErrorCode.SETTLEMENT_NOT_EXIST);
        }
        settlement.setStatus(settlementRequest.getStatus());
        settlementRepository.save(settlement);
        if (settlementRequest.getStatus().equals(SettlementStatus.PAID)){
            cacheUserBalance();
            cacheFriendBalance();
//            userController.refreshCache(settlement.getChargerId(), settlement.getPayerId(), debt);
//            userController.updateCache(settlement.getPayerId(), settlement.getChargerId(), (-1) * settlement.getCharge());
        }
    }

    public List<SettlementDTO> getSettlements(String userToken, SettlementStatus status) throws CoreException {
        List<Settlement> settlements;
        if (status.equals(SettlementStatus.UNPAID)) {
            settlements = settlementRepository.findByChargerIdAndStatusAndVisibility(userToken, status, Visibility.VISIBLE);
        } else if (status.equals(SettlementStatus.WAITING_FOR_CONFIRMATION)){
            settlements =  settlementRepository.findByPayerIdAndStatusAndVisibility(userToken, status, Visibility.VISIBLE);
        } else {
            settlements = new ArrayList<>();
        }
        return settlements.stream()
                .map(settlementMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<SettlementDTO> getDues(String userToken) {
        List<Settlement> settlements = settlementRepository.findByPayerIdAndStatusAndVisibility(userToken, SettlementStatus.UNPAID, Visibility.VISIBLE);
        return settlements.stream()
                .map(settlementMapper::toDto)
                .collect(Collectors.toList());
    }

    public void cacheUserBalance() {
        List<User> users = userController.getAll();
        users.stream().filter(user -> user.getBalanceCache() == null).forEach(user -> user.setBalanceCache(new BalanceCache()));
        users.forEach(user -> {
            List<Settlement> debtSettlements = settlementRepository.findByChargerAndStatus(user,SettlementStatus.UNPAID);
            List<Settlement> dueSettlements = settlementRepository.findByPayerAndStatus(user,SettlementStatus.UNPAID);
            Double debt = debtSettlements.stream().mapToDouble(Settlement::getCharge).sum();
            Double due = dueSettlements.stream().mapToDouble(Settlement::getCharge).sum();
            user.setBalanceCache(new BalanceCache(debt,due));
        });
        userController.saveAll(users);
    }

    public void cacheFriendBalance() {
        List<Friend> friends = userController.getAllFriend();
        friends.forEach(friend -> {
            List<Settlement> settlements = settlementRepository.findByChargerAndPayerAndStatus(friend.getUser(),friend.getFriend(),SettlementStatus.UNPAID);
            Double debt = settlements.stream().mapToDouble(Settlement::getCharge).sum();
            userController.refreshCache(friend.getFriendId(), friend.getUserId(), debt);
        });

    }

    public void clearSettlements(Long billId) {
        List<Settlement> settlements = settlementRepository.findByBillId(billId);
        settlementRepository.delete(settlements);

    }

    public void sendNotification(SettlementRequest settlementRequest) {
        Settlement settlement = settlementRepository.getOne(settlementRequest.getSettlementId());
        User user = userController.getOne(settlement.getChargerId());
        notificationService.sendUserNotification(user, SETTLEMENT_REMIND_TITLE, SETTLEMENT_REMIND_BODY
                .replaceAll("\\$user\\$", settlement.getPayer().getDisplayName())
                .replaceAll("\\$charge\\$", settlement.getCharge().toString())
        );
    }


}
