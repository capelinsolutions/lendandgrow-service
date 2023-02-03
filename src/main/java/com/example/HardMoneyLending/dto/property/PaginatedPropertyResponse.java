package com.example.HardMoneyLending.dto.property;

import com.example.HardMoneyLending.models.property.Property;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginatedPropertyResponse {

    private List<Property> property;

    private int pageNo;
    private int pageSize;
    private int noOfPages;
    private long totalRows;
}
