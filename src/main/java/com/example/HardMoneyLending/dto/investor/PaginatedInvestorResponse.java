package com.example.HardMoneyLending.dto.investor;

import com.example.HardMoneyLending.models.investor.Investor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginatedInvestorResponse{

    private List<Investor> investors;

    private int pageNo;
    private int pageSize;
    private int noOfPages;
    private long totalRows;
}
