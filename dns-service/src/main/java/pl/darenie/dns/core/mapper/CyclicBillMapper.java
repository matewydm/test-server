package pl.darenie.dns.core.mapper;

import org.springframework.stereotype.Controller;
import pl.darenie.dns.jpa.CyclicBill;
import pl.darenie.dns.model.dto.CyclicBillDTO;
import pl.darenie.dns.model.dto.base.CyclicDTO;

@Controller
public class CyclicBillMapper {

    public CyclicBillDTO mapToDto(CyclicBill cyclicBill) {
        return new CyclicBillDTO.Builder()
                .id(cyclicBill.getId())
                .cyclicType(cyclicBill.getType())
                .day(cyclicBill.getDay())
                .hour(cyclicBill.getHour())
                .minute(cyclicBill.getMinute())
                .build();
    }

    public CyclicBill mapToJpa(CyclicBillDTO cyclicBillDTO) {
        if (cyclicBillDTO != null) {
            CyclicBill cyclicBill = new CyclicBill();
            cyclicBill.setId(cyclicBillDTO.getId());
            cyclicBill.setType(cyclicBillDTO.getType());
            cyclicBill.setDay(cyclicBillDTO.getDay());
            cyclicBill.setHour(cyclicBillDTO.getHour());
            cyclicBill.setMinute(cyclicBillDTO.getMinute());
            return cyclicBill;
        }
        return null;
    }

}