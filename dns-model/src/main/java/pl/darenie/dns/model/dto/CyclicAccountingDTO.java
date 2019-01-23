package pl.darenie.dns.model.dto;

import pl.darenie.dns.model.dto.base.CyclicDTO;

public class CyclicAccountingDTO extends CyclicDTO{

    public CyclicAccountingDTO() {
        super();
    }

    protected CyclicAccountingDTO(Builder builder) {
        super(builder);
    }

    public static class Builder extends CyclicDTO.Builder<Builder> {
        public Builder() {
        }

        public CyclicAccountingDTO build() {
            return new CyclicAccountingDTO(this);
        }
    }
}
