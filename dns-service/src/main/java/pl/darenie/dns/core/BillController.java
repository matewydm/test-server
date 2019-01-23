package pl.darenie.dns.core;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.darenie.dns.core.mapper.CyclicBillMapper;
import pl.darenie.dns.core.mapper.SettlementMapper;
import pl.darenie.dns.dao.SettlementRepository;
import pl.darenie.dns.jpa.*;
import pl.darenie.dns.core.mapper.BillMapper;
import pl.darenie.dns.dao.BillRepository;
import pl.darenie.dns.dao.UserRepository;
import pl.darenie.dns.model.dto.Accounter;
import pl.darenie.dns.model.dto.SettlementDTO;
import pl.darenie.dns.model.enums.*;
import pl.darenie.dns.model.exception.CoreException;
import pl.darenie.dns.model.dto.BillDTO;
import pl.darenie.dns.model.rest.request.SettlementRequest;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class BillController {

    private static final Logger LOGGER = Logger.getLogger(UserDeviceService.class.toString());

    private final BillRepository billRepository;
    private final BillMapper billMapper;
    private final SettlementController settlementController;

    @Autowired
    public BillController(BillRepository billRepository, BillMapper billMapper, SettlementController settlementController) {
        this.billRepository = billRepository;
        this.billMapper = billMapper;
        this.settlementController = settlementController;
    }


    public void addEventBill(BillDTO billDTO, String ownerId) throws CoreException {
        Bill bill;
        if (billDTO.getId() != null) {
            bill = billRepository.findById(billDTO.getId());
            bill = billMapper.copyValues(bill,billDTO);
            bill.setOwnerId(ownerId);
        } else {
            bill = billMapper.mapToJpa(billDTO);
            bill.setOwnerId(ownerId);
        }
        LOGGER.log(Logger.Level.INFO, "Dare");
        try {
            bill = billRepository.save(bill);
        } catch (Exception e) {
            throw new CoreException("Cannot add new bill", ErrorCode.CANNOT_ADD_BILL);
        }
        if (billDTO.getId() != null) {
            settlementController.clearSettlements(billDTO.getId());
        }
        if (bill.getCyclicBill() == null ) {
            settlementController.processSettlements(bill);
        }
    }

    public List<BillDTO> getBillsByOwnerId(String userToken) {
        return billRepository.findDistinctByOwnerId(userToken).stream()
                .map(billMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<BillDTO> getBillHistory(String userToken) {
        return billRepository.findBillHistory(userToken).stream()
                .map(billMapper::mapToDto)
                .collect(Collectors.toList());
    }
}