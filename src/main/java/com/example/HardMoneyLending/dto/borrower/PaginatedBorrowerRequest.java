package com.example.HardMoneyLending.dto.borrower;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginatedBorrowerRequest {

    private String name;
    private String state;
    private String city;
    private String propertyTypeId;
    private int pageNo;
    private int pageSize;
}
