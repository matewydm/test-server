package pl.darenie.dns.core;

import org.junit.Before;
import org.junit.Test;
import pl.darenie.dns.core.firebase.notification.FcmNotificationService;
import pl.darenie.dns.core.mapper.SettlementMapper;
import pl.darenie.dns.core.mapper.UserMapper;
import pl.darenie.dns.dao.BillRepository;
import pl.darenie.dns.dao.SettlementRepository;
import pl.darenie.dns.jpa.Bill;
import pl.darenie.dns.jpa.Settlement;
import pl.darenie.dns.jpa.User;
import pl.darenie.dns.jpa.UserHasCashBill;
import pl.darenie.dns.model.enums.SettlementStatus;
import pl.darenie.dns.model.enums.UserCashType;
import pl.darenie.dns.model.exception.CoreException;
import pl.darenie.dns.model.rest.request.SettlementRequest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SettlementControllerTest {

    private SettlementController settlementController;

    private BillRepository billRepository;
    private SettlementRepository settlementRepository;
    private SettlementMapper settlementMapper;
    private UserController userController;
    private FcmNotificationService notificationService;

    @Before
    public void setUp() throws Exception {
        billRepository = mock(BillRepository.class);
        settlementRepository = mock(SettlementRepository.class);
        settlementMapper = new SettlementMapper(new UserMapper());
        userController = mock(UserController.class);
        notificationService = mock(FcmNotificationService.class);
        settlementController = new SettlementController(billRepository, settlementRepository, settlementMapper, userController, notificationService);
    }

    @Test
    public void processSettlements() {
        Bill bill = mock(Bill.class, RETURNS_DEEP_STUBS);
        UserHasCashBill userHasCashBill = mock(UserHasCashBill.class, RETURNS_DEEP_STUBS);
        when(userHasCashBill.getType()).thenReturn(UserCashType.CHARGER);
        Set<UserHasCashBill> userHasCashBillSet = new HashSet<>();
        userHasCashBillSet.add(userHasCashBill);
        when(bill.getUserHasCashBill()).thenReturn(userHasCashBillSet);


        when(billRepository.findById(any())).thenReturn(bill);

        settlementController.processSettlements(bill);

        verify(notificationService, times(0)).sendUserNotification(anyString(), anyString(), anyString());
    }

    @Test
    public void processInvisibleSettlement() {
    }

    @Test(expected = CoreException.class)
    public void updateSettlementStatusException() throws Exception {
        SettlementRequest rq = mock(SettlementRequest.class);
        when(rq.getSettlementId()).thenReturn(null);
        settlementController.updateSettlementStatus(rq);
    }

    @Test
    public void updateSettlementStatusWAITING() throws Exception {
        SettlementRequest rq = mock(SettlementRequest.class);
        when(rq.getSettlementId()).thenReturn(1L);
        when(rq.getStatus()).thenReturn(SettlementStatus.WAITING_FOR_CONFIRMATION);
        Settlement settlement = mock(Settlement.class);

        when(settlementRepository.findOne(1L)).thenReturn(settlement);
        settlementController.updateSettlementStatus(rq);


        verify(settlementRepository, times(1)).save(any(Settlement.class));
    }

    @Test
    public void updateSettlementStatusPAID() throws Exception {
        SettlementRequest rq = mock(SettlementRequest.class);
        when(rq.getSettlementId()).thenReturn(1L);
        when(rq.getStatus()).thenReturn(SettlementStatus.PAID);
        Settlement settlement = mock(Settlement.class);

        when(settlementRepository.findOne(1L)).thenReturn(settlement);


        settlementController.updateSettlementStatus(rq);


        verify(settlementRepository, times(1)).save(any(Settlement.class));
        verify(userController, times(1)).saveAll(anyList());
        verify(userController, times(0)).refreshCache(any(), any(), any());
    }


    @Test
    public void cacheUserBalance() {

        settlementController.cacheUserBalance();
        verify(userController, times(1)).saveAll(anyList());

    }


    @Test
    public void sendNotification() {
        SettlementRequest rq = mock(SettlementRequest.class);

        Settlement settlement = mock(Settlement.class, RETURNS_DEEP_STUBS);
        when(settlementRepository.getOne(any())).thenReturn(settlement);
        settlementController.SETTLEMENT_REMIND_BODY = "TEST";

        settlementController.sendNotification(rq);

        verify(notificationService, times(1))
                .sendUserNotification(any(User.class), any(), any());
    }
}