package pl.darenie.dns.core.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.darenie.dns.jpa.Settlement;
import pl.darenie.dns.model.dto.SettlementDTO;

@Controller
public class SettlementMapper {

    private final UserMapper userMapper;

    @Autowired
    public SettlementMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public SettlementDTO toDto(Settlement settlement) {
        return new SettlementDTO.Builder()
                .id(settlement.getId())
                .billId(settlement.getBillId())
                .charger(userMapper.mapToDto(settlement.getCharger()))
                .payer(userMapper.mapToDto(settlement.getPayer()))
                .charge(settlement.getCharge())
                .status(settlement.getStatus())
                .build();
    }
}
