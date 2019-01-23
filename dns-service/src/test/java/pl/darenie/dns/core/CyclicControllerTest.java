package pl.darenie.dns.core;

import org.junit.Before;
import org.junit.Test;
import pl.darenie.dns.dao.CyclicAccountingRepository;
import pl.darenie.dns.dao.CyclicBillRepository;
import pl.darenie.dns.model.dto.CyclicDTO;
import pl.darenie.dns.model.enums.CyclicType;

import static org.mockito.Mockito.*;

public class CyclicControllerTest {

    private CyclicAccountingRepository cyclicAccountingRepository;
    private CyclicBillRepository cyclicBillRepository;
    private SettlementController settlementController;
    private CyclicController cyclicController;

    @Before
    public void setUp() throws Exception {
        cyclicAccountingRepository = mock(CyclicAccountingRepository.class);
        cyclicBillRepository = mock(CyclicBillRepository.class);
        settlementController = mock(SettlementController.class);
        cyclicController = new CyclicController(cyclicAccountingRepository, cyclicBillRepository, settlementController);
    }

    @Test
    public void processCyclicBillsTest() throws Exception {

        CyclicDTO cyclic = mock(CyclicDTO.class);

        when(cyclic.getType()).thenReturn(CyclicType.MONTH);
        when(cyclic.getDay()).thenReturn(4);
        when(cyclic.getHour()).thenReturn(10);
        when(cyclic.getMinute()).thenReturn(30);

        verify(settlementController, times(cyclicBillRepository.findByTypeAndDayAndHourAndMinute(cyclic.getType(), cyclic.getDay(), cyclic.getHour(), cyclic.getMinute()).size()))
            .processSettlements(any());
    }

    @Test
    public void processCyclicAccountingsTest() throws Exception {

        CyclicDTO cyclic = mock(CyclicDTO.class);

        when(cyclic.getType()).thenReturn(CyclicType.WEEK);
        when(cyclic.getDay()).thenReturn(1);
        when(cyclic.getHour()).thenReturn(15);
        when(cyclic.getMinute()).thenReturn(30);

        verify(settlementController, times(cyclicAccountingRepository.findByTypeAndDayAndHourAndMinute(cyclic.getType(), cyclic.getDay(), cyclic.getHour(), cyclic.getMinute()).size()))
                .processInvisibleSettlement(any());
    }

}
