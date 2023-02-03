package com.example.HardMoneyLending.models.property;

import com.example.HardMoneyLending.models.form.Form;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "property")
public class Property {

    @Id
    private String propertyId;

    private String borrowerId;

    private String propertyTypeId;

    private String title;

    private String description;

    private Double price;

    private Boolean showPrice = Boolean.TRUE;

    private List<String> imageUrls;

    private String address;

    private String area;

    private List<Form> questions;

    private Long createdAt;

    private Long updatedAt;

    private Boolean active = Boolean.FALSE;
}
