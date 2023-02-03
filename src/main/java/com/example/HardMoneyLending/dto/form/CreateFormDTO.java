package com.example.HardMoneyLending.dto.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class CreateFormDTO {

    private String propertyTypeId;
    @NotBlank
    private String type;
    @NotBlank
    private String label;

    private String inputType;
    @NotBlank
    private String name;
    private List<Validation> validations;
    private String value;
    private List<String> options;
}
