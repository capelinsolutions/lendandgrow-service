package com.example.HardMoneyLending.dto.shared_file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailsDocumentAccessDTO {
    private String email;
    private List<DocumentAccessDTO> documentAccess;
}
