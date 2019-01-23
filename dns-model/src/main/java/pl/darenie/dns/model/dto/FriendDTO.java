package pl.darenie.dns.model.dto;

public class FriendDTO extends UserDTO {

    private CyclicAccountingDTO cyclicAccounting;

    public FriendDTO() {
        super();
    }

    public FriendDTO(Builder builder) {
        super(builder);
        this.cyclicAccounting = builder.cyclicAccounting;
    }

    public CyclicAccountingDTO getCyclicAccounting() {
        return cyclicAccounting;
    }

    public void setCyclicAccounting(CyclicAccountingDTO cyclicAccounting) {
        this.cyclicAccounting = cyclicAccounting;
    }

    public static class Builder extends UserDTO.Builder<Builder> {

        private CyclicAccountingDTO cyclicAccounting;

        public Builder() {}

        public Builder cyclicAccounting(CyclicAccountingDTO val) {
            cyclicAccounting = val;
            return this;
        }

        public FriendDTO build() { return new FriendDTO(this); }
    }

}
