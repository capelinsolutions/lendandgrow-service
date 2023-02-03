package com.example.HardMoneyLending.dto.borrower;

import com.example.HardMoneyLending.models.borrower.Borrower;
import com.example.HardMoneyLending.models.investor.Investor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginatedBorrowerResponse {

    private List<Borrower> borrowers;

    private int pageNo;
    private int pageSize;
    private int noOfPages;
    private long totalRows;
}
