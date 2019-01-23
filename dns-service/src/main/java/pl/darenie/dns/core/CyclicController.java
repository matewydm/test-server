package pl.darenie.dns.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.darenie.dns.dao.*;
import pl.darenie.dns.jpa.*;
import pl.darenie.dns.model.dto.CyclicDTO;
import pl.darenie.dns.model.enums.CyclicType;
import pl.darenie.dns.model.enums.SettlementStatus;
import pl.darenie.dns.model.enums.Visibility;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CyclicController {

    private CyclicAccountingRepository cyclicAccountingRepository;
    private CyclicBillRepository cyclicBillRepository;
    private SettlementController settlementController;

    @Autowired
    public CyclicController(CyclicAccountingRepository cyclicAccountingRepository, CyclicBillRepository cyclicBillRepository, SettlementController settlementController) {
        this.cyclicAccountingRepository = cyclicAccountingRepository;
        this.cyclicBillRepository = cyclicBillRepository;
        this.settlementController = settlementController;
    }

    public void processCyclicBills(CyclicDTO cyclic) {
        List<CyclicBill> cyclicBills = cyclicBillRepository.findByTypeAndDayAndHourAndMinute(cyclic.getType(), cyclic.getDay(), cyclic.getHour(), cyclic.getMinute());
        List<Bill> bills = cyclicBills.stream().map(CyclicBill::getBill).collect(Collectors.toList());
        bills.forEach(bill -> settlementController.processSettlements(bill));
    }

    public void processCyclicAccountings(CyclicDTO cyclic) {
        List<CyclicAccounting> cyclicAccountings = cyclicAccountingRepository.findByTypeAndDayAndHourAndMinute(cyclic.getType(), cyclic.getDay(), cyclic.getHour(), cyclic.getMinute());
        List<Friend> friends = cyclicAccountings.stream().map(CyclicAccounting::getFriend).collect(Collectors.toList());
        friends.forEach(friend -> settlementController.processInvisibleSettlement(friend));
    }
}
