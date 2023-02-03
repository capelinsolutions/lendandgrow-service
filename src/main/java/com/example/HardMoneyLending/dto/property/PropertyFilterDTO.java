package com.example.HardMoneyLending.dto.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyFilterDTO {
    private String city;
    private String address;
    private Double priceStart;
    private Double priceEnd;
    private String propertyTypeTitle;

    private int pageNo;
    private int pageSize;
}
