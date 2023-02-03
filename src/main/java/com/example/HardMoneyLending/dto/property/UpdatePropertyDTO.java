package com.example.HardMoneyLending.dto.property;

import com.example.HardMoneyLending.models.form.Form;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdatePropertyDTO {

    private String propertyId;
    private String borrowerId;
    private String propertyTypeId;
    private String title;
    private String description;
    private double price;
    private Boolean showPrice;
    private List<String> imageUrls;
    private String address;
    private String area;
    private List<Form> questions;
    private Boolean active;
}
