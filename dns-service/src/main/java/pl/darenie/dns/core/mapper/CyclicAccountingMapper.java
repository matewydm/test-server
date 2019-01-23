package pl.darenie.dns.core.mapper;

import org.springframework.stereotype.Controller;
import pl.darenie.dns.jpa.CyclicAccounting;
import pl.darenie.dns.model.dto.CyclicAccountingDTO;

@Controller
public class CyclicAccountingMapper {

    public CyclicAccountingDTO mapToDto(CyclicAccounting accounting) {
        if (accounting != null) {
            return new CyclicAccountingDTO.Builder()
                    .cyclicType(accounting.getType())
                    .day(accounting.getDay())
                    .hour(accounting.getHour())
                    .minute(accounting.getMinute())
                    .build();
        }
        return null;
    }

    public CyclicAccounting mapToJpa(CyclicAccountingDTO accounting) {
        if (accounting != null) {
            CyclicAccounting newAccounting = new CyclicAccounting();
            newAccounting.setType(accounting.getType());
            newAccounting.setDay(accounting.getDay());
            newAccounting.setHour(accounting.getHour());
            newAccounting.setMinute(accounting.getMinute());
            return newAccounting;
        }
        return null;
    }

    public CyclicAccounting remapJpa(CyclicAccounting accounting, CyclicAccountingDTO accountingDTO) {
        if (accountingDTO == null) {
            return null;
        }
        if (accounting == null) {
            accounting = new CyclicAccounting();
        }
        accounting.setType(accountingDTO.getType());
        accounting.setDay(accountingDTO.getDay());
        accounting.setHour(accountingDTO.getHour());
        accounting.setMinute(accountingDTO.getMinute());
        return accounting;
    }
}
