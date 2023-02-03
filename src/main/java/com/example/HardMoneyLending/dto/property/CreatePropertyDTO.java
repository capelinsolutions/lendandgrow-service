package com.example.HardMoneyLending.dto.property;

import com.example.HardMoneyLending.models.form.Form;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class CreatePropertyDTO {

    private String borrowerId;

    private String propertyTypeId;

    private String title;

    private String description;

    private double price = 0;

    private Boolean showPrice = Boolean.TRUE;

    private List<String> imageUrls;

    private String address;

    private String area;

    @NotNull
    private List<Form> questions;

    private Boolean active;
}
