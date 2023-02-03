package com.example.HardMoneyLending.dto.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginatedPropertyRequest {

    private String borrowerId;
    private String title;
    private Double priceStart;
    private Double priceEnd;
    private String area;

    private int pageNo;
    private int pageSize;
}
