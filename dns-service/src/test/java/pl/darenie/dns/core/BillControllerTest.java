package pl.darenie.dns.core;

import org.junit.Before;
import org.junit.Test;
import pl.darenie.dns.core.mapper.BillMapper;
import pl.darenie.dns.core.mapper.CyclicBillMapper;
import pl.darenie.dns.core.mapper.UserMapper;
import pl.darenie.dns.dao.BillRepository;
import pl.darenie.dns.jpa.Bill;
import pl.darenie.dns.model.dto.BillDTO;
import pl.darenie.dns.model.enums.BillPriority;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class BillControllerTest {

    private BillController billController;
    private BillRepository billRepository;
    private SettlementController settlementController;

    @Before
    public void setUp() throws Exception {
        billRepository = mock(BillRepository.class);
        BillMapper billMapper = new BillMapper(new UserMapper(), new CyclicBillMapper());
        settlementController = mock(SettlementController.class);
        billController = new BillController(billRepository, billMapper, settlementController);
    }

    @Test
    public void addEventBillTest() throws Exception {

        BillDTO bill1 = mock(BillDTO.class);

        when(bill1.getPayment()).thenReturn(20.0);
        when(bill1.getName()).thenReturn("Example payment");
        when(bill1.getPriority()).thenReturn(BillPriority.CRITICAL);
        when(bill1.getId()).thenReturn(1L);

        when(billRepository.save(any(Bill.class)))
                .thenReturn(mock(Bill.class));
        billController.addEventBill(bill1, "id-test");

        verify(settlementController, times(1)).clearSettlements(any());

        verify(settlementController, times(1)).processSettlements(any());
    }

    @Test
    public void getBillsByOwnerId() {
        Bill bill1 = mock(Bill.class);
        when(bill1.getPayment()).thenReturn(20.0);
        when(bill1.getName()).thenReturn("Example paymeny");
        when(bill1.getPriority()).thenReturn(BillPriority.CRITICAL);
        List<Bill> bills = new ArrayList<>();
        bills.add(bill1);
        when(billRepository.findDistinctByOwnerId(any())).thenReturn(bills);

        List<BillDTO> result = billController.getBillsByOwnerId(anyString());

        assertEquals(1, result.size());

        assertEquals("Example paymeny", result.get(0).getName());

        assertEquals(new Double(20.0), result.get(0).getPayment());

        assertEquals(BillPriority.CRITICAL, result.get(0).getPriority());
    }


    @Test
    public void getBillHistory() {
        Bill bill1 = mock(Bill.class);
        when(bill1.getPayment()).thenReturn(20.0);
        when(bill1.getName()).thenReturn("Payments 1");
        when(bill1.getPriority()).thenReturn(BillPriority.CRITICAL);


        Bill bill2 = mock(Bill.class);
        when(bill2.getPayment()).thenReturn(50.2);
        when(bill2.getName()).thenReturn("Payments 2");
        when(bill2.getPriority()).thenReturn(BillPriority.MEDIUM);


        List<Bill> bills = new ArrayList<>();
        bills.add(bill1);
        bills.add(bill2);


        when(billRepository.findBillHistory(any())).thenReturn(bills);

        List<BillDTO> result = billController.getBillHistory(anyString());

        assertEquals(2, result.size());

        assertEquals("Payments 1", result.get(0).getName());

        assertEquals(new Double(20.0), result.get(0).getPayment());

        assertEquals(BillPriority.CRITICAL, result.get(0).getPriority());

        assertEquals("Payments 2", result.get(1).getName());

        assertEquals(new Double(50.2), result.get(1).getPayment());

        assertEquals(BillPriority.MEDIUM, result.get(1).getPriority());

    }
}