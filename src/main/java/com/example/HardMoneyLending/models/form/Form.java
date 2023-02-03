package com.example.HardMoneyLending.models.form;

import com.example.HardMoneyLending.dto.form.Validation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "form")
public class Form {

    @Transient
    public static final String DOCUMENT_TYPE_NAME = "document";

    @Id
    private String id;
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
    private boolean isSelected;
    private List<String> options;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Form form = (Form) o;
        return id.equals(form.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
