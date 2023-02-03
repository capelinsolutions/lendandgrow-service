package com.example.HardMoneyLending.dto.shared_file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentAccessDTO {
    private String documentUrl;
    private Set<String> accessType;
}
