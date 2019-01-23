package pl.darenie.dns.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.darenie.dns.core.BillController;
import pl.darenie.dns.core.SettlementController;
import pl.darenie.dns.core.firebase.FirebaseTokenHolder;
import pl.darenie.dns.model.dto.BillDTO;
import pl.darenie.dns.model.dto.SettlementDTO;
import pl.darenie.dns.model.enums.SettlementStatus;
import pl.darenie.dns.model.exception.CoreException;
import pl.darenie.dns.model.rest.request.SettlementRequest;

import java.util.List;

@RestController
@RequestMapping("/settlement")
public class SettlementResource {

    @Autowired
    private SettlementController settlementController;
    @Autowired
    private FirebaseTokenHolder firebaseTokenHolder;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity updateSettlementStatus(@RequestBody SettlementRequest settlementRequest) throws CoreException {
        settlementController.updateSettlementStatus(settlementRequest);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/unpaid", method = RequestMethod.GET)
    public ResponseEntity getUnpaidBills() throws CoreException {
        List<SettlementDTO> settlementDTOS = settlementController.getSettlements(firebaseTokenHolder.getUserToken(), SettlementStatus.UNPAID);
        return ResponseEntity.ok(settlementDTOS);
    }

    @RequestMapping(value = "/awaiting", method = RequestMethod.GET)
    public ResponseEntity getAwaitingPayments() throws CoreException {
        List<SettlementDTO> settlementDTOS = settlementController.getSettlements(firebaseTokenHolder.getUserToken(), SettlementStatus.WAITING_FOR_CONFIRMATION);
        return ResponseEntity.ok(settlementDTOS);
    }

    @RequestMapping(value = "/dues", method = RequestMethod.GET)
    public ResponseEntity getDues() throws CoreException {
        List<SettlementDTO> settlementDTOS = settlementController.getDues(firebaseTokenHolder.getUserToken());
        return ResponseEntity.ok(settlementDTOS);
    }

    @RequestMapping(value = "/notification", method = RequestMethod.POST)
    public ResponseEntity sendNotification(@RequestBody SettlementRequest settlementRequest) throws CoreException {
        settlementController.sendNotification(settlementRequest);
        return ResponseEntity.ok().build();
    }



}
