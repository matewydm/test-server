package pl.darenie.dns.model.dto;

import pl.darenie.dns.model.dto.base.CyclicDTO;
import pl.darenie.dns.model.enums.CyclicType;

public class CyclicBillDTO extends CyclicDTO {

    public CyclicBillDTO() {}

    protected CyclicBillDTO(Builder builder) {
        super(builder);
    }

    public static class Builder extends CyclicDTO.Builder<Builder> {
        public Builder() {
        }

        public CyclicBillDTO build() {
            return new CyclicBillDTO(this);
        }
    }
}
