package com.example.HardMoneyLending.models.shared_file;

import com.example.HardMoneyLending.dto.shared_file.EmailsDocumentAccessDTO;
import com.example.HardMoneyLending.models.property.Property;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "shared_file")
public class SharedFile {
    @Id
    private String sharedFileId;
    private Property property;
    private List<EmailsDocumentAccessDTO> emailsDocumentAccess;
    private Long createdAt;
    private Long updatedAt;

}
