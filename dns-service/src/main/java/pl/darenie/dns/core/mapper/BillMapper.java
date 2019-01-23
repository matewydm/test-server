package pl.darenie.dns.core.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.darenie.dns.jpa.Bill;
import pl.darenie.dns.jpa.UserHasCashBill;
import pl.darenie.dns.model.dto.BillDTO;
import pl.darenie.dns.model.dto.CyclicBillDTO;
import pl.darenie.dns.model.enums.BillPriority;
import pl.darenie.dns.model.enums.UserCashType;
import pl.darenie.dns.model.rest.request.UserCash;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BillMapper {

    private final UserMapper userMapper;
    private final CyclicBillMapper cyclicBillMapper;

    @Autowired
    public BillMapper(UserMapper userMapper, CyclicBillMapper cyclicBillMapper) {
        this.userMapper = userMapper;
        this.cyclicBillMapper = cyclicBillMapper;
    }

    public Bill mapToJpa(BillDTO billDTO) {
        if (billDTO != null) {
            Bill bill = new Bill();
            return mapValues(bill,billDTO);
        }
        return null;
    }

    public Bill copyValues(Bill bill, BillDTO billDTO) {
        if (bill != null) {
            return mapValues(bill,billDTO);
        }
        return mapToJpa(billDTO);
    }

    public BillDTO mapToDto(Bill bill) {
        if (bill != null) {
            return new BillDTO.Builder()
                    .id(bill.getId())
                    .name(bill.getName())
                    .payment(bill.getPayment())
                    .ownerId(bill.getOwnerId())
                    .priority(bill.getPriority())
                    .chargers(bill.getUserHasCashBill().stream()
                            .filter(uhc -> uhc.getType().equals(UserCashType.CHARGER))
                            .map(uhc -> new UserCash.Builder()
                                    .firebaseToken(uhc.getUserId())
                                    .firstname(uhc.getUser().getFirstname())
                                    .lastname(uhc.getUser().getLastname())
                                    .cash(uhc.getCash())
                                    .build())
                            .collect(Collectors.toList()))
                    .payers(bill.getUserHasCashBill().stream()
                            .filter(uhc -> uhc.getType().equals(UserCashType.PAYER))
                            .map(uhc -> new UserCash.Builder()
                                    .firebaseToken(uhc.getUserId())
                                    .firstname(uhc.getUser().getFirstname())
                                    .lastname(uhc.getUser().getLastname())
                                    .cash(uhc.getCash())
                                    .build())
                            .collect(Collectors.toList()))
                    .cyclicBill((bill.getCyclicBill() == null) ? null :
                            new CyclicBillDTO.Builder()
                                .cyclicType(bill.getCyclicBill().getType())
                                .day(bill.getCyclicBill().getDay())
                                .hour(bill.getCyclicBill().getHour())
                                .minute(bill.getCyclicBill().getMinute())
                                .build())
                    .build();
        }
        return null;
    }

    private Bill mapValues(Bill bill, BillDTO billDTO) {
        bill.setName(billDTO.getName());
        bill.setPayment(billDTO.getPayment());
        bill.setPriority(billDTO.getPriority());
        List<UserHasCashBill> listCashBill = new ArrayList<>();
        listCashBill.addAll(billDTO.getPayers().stream()
                .map(userCash -> new UserHasCashBill(bill, userCash.getFirebaseToken(), userCash.getCash(), UserCashType.PAYER))
                .collect(Collectors.toList()));
        listCashBill.addAll(billDTO.getChargers().stream()
                .map(userCash -> new UserHasCashBill(bill, userCash.getFirebaseToken(), userCash.getCash(), UserCashType.CHARGER))
                .collect(Collectors.toList()));
        bill.getUserHasCashBill().removeIf(uhc -> !listCashBill.contains(uhc));
        listCashBill.forEach(lcb -> {
            if (bill.getUserHasCashBill().contains(lcb)) {
                bill.getUserHasCashBill().stream().filter(it -> it.equals(lcb)).forEach(it -> it.setCash(lcb.getCash()));
            } else {
                bill.addUserHasCashBill(lcb);
            }
        });
        bill.setCyclicBill(cyclicBillMapper.mapToJpa(billDTO.getCyclicBill()));
        if(bill.getOwnerId() == null) {
            bill.setOwnerId(billDTO.getOwnerId());
        }
        return bill;
    }
}

