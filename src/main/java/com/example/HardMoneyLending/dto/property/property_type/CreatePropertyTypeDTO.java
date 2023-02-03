package com.example.HardMoneyLending.dto.property.property_type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePropertyTypeDTO {
    @NotBlank
    private String title;
    private String description;
    private String iconUrl;
}
