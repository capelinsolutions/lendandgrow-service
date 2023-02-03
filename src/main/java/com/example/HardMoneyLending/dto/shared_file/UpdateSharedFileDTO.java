package com.example.HardMoneyLending.dto.shared_file;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateSharedFileDTO {

    private String sharedFileId;
    private String propertyId;
    private List<EmailsDocumentAccessDTO> emailsDocumentAccess;
}
