package com.example.HardMoneyLending.models.fee_structure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "fee_structure")
public class FeeStructure {
    @Id
    String id;
    String to;
    String from;
    String text;
    boolean active;
}
