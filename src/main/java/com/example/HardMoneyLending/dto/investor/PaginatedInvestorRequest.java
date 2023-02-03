package com.example.HardMoneyLending.dto.investor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginatedInvestorRequest {

    private String name;
    private String city;
    private String state;
    private String feeStructureId;
    private int pageNo;
    private int pageSize;
}
