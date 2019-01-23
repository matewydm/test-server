package pl.darenie.dns.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.darenie.dns.core.BillController;
import pl.darenie.dns.core.firebase.FirebaseTokenHolder;
import pl.darenie.dns.model.exception.CoreException;
import pl.darenie.dns.model.dto.BillDTO;

import java.util.List;

@RestController
@RequestMapping("/bill")
public class BillResource {

    private final BillController billController;
    private final FirebaseTokenHolder firebaseTokenHolder;

    @Autowired
    public BillResource(BillController billController, FirebaseTokenHolder firebaseTokenHolder) {
        this.billController = billController;
        this.firebaseTokenHolder = firebaseTokenHolder;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity addEventBill(@RequestBody BillDTO billDTO) throws CoreException {
        billController.addEventBill(billDTO, firebaseTokenHolder.getUserToken());
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getBills() throws CoreException {
        List<BillDTO> bills = billController.getBillsByOwnerId(firebaseTokenHolder.getUserToken());
        return ResponseEntity.ok(bills);
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public ResponseEntity getBillHistory() throws CoreException {
        List<BillDTO> bills = billController.getBillHistory(firebaseTokenHolder.getUserToken());
        return ResponseEntity.ok(bills);
    }

}
