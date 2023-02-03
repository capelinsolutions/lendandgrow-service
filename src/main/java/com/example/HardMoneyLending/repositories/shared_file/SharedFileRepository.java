package com.example.HardMoneyLending.repositories.shared_file;

import com.example.HardMoneyLending.models.shared_file.SharedFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SharedFileRepository extends MongoRepository<SharedFile,String> {
    SharedFile save(SharedFile sharedFile);

    Optional<SharedFile> findBySharedFileId(String id);

    SharedFile findByProperty_PropertyId(String propertyId);

    List<SharedFile> findAllSharedFileByEmailsDocumentAccess_Email(String userEmail);

    SharedFile findSharedFileByProperty_PropertyIdAndEmailsDocumentAccess_Email(String propertyId, String userEmail);

    SharedFile findSharedFileByEmailsDocumentAccess_DocumentAccess_DocumentUrlAndEmailsDocumentAccess_Email(String userEmail, String docUrl);
}
