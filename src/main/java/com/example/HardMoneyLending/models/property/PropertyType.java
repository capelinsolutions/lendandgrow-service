package com.example.HardMoneyLending.models.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "propertyType")
public class PropertyType {
    @Id
    private String propertyTypeId;

    @Indexed(unique=true)
    private String title;
    private String description;
    private String iconUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyType that = (PropertyType) o;
        return Objects.equals(propertyTypeId, that.propertyTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyTypeId);
    }

    @Override
    public String toString() {
        return "PropertyType{" +
                "propertyTypeId='" + propertyTypeId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                '}';
    }
}
