package com.example.HardMoneyLending.dto.form;

import com.example.HardMoneyLending.models.form.Form;
import com.example.HardMoneyLending.models.property.PropertyType;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class GetFormDTO {

        private PropertyType propertyType;
        private Set<Form> forms;

        public GetFormDTO(PropertyType propertyType, Set<Form> forms) {
            this.propertyType = propertyType;
            this.forms = forms;
        }

}
