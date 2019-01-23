package pl.darenie.dns.model.rest.request;

import pl.darenie.dns.model.enums.SettlementStatus;

public class SettlementRequest {

    private Long settlementId;
    private SettlementStatus status;

    public Long getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(Long settlementId) {
        this.settlementId = settlementId;
    }

    public SettlementStatus getStatus() {
        return status;
    }

    public void setStatus(SettlementStatus status) {
        this.status = status;
    }
}
