package pl.darenie.dns.model.rest.request;

import java.util.List;

public class BillAddRequest {

    private String name;
    private Double payment;
    private List<UserCash> payers;
    private List<UserCash> chargers;

}
