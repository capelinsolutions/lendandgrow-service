package com.example.HardMoneyLending.dto.borrower;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateBorrowerDTO extends CreateBorrowerDTO{
    private String borrowerId;
}
