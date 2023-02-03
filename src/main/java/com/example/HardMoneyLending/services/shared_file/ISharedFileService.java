package com.example.HardMoneyLending.services.shared_file;

import com.example.HardMoneyLending.dto.shared_file.CreateSharedFileDTO;
import com.example.HardMoneyLending.dto.shared_file.UpdateSharedFileDTO;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.models.shared_file.SharedFile;
import com.example.HardMoneyLending.utils.ListMessage;
import com.example.HardMoneyLending.utils.Message;

public interface ISharedFileService {

    Message<SharedFile> createSharedFile(CreateSharedFileDTO dto) throws EntityNotFoundException, BadRequestException;

    Message<SharedFile> updateSharedFile(UpdateSharedFileDTO dto) throws EntityNotFoundException;

    Message<SharedFile> getSharedFileById(String id) throws EntityNotFoundException;

    ListMessage<SharedFile> getAllSharedFile() throws EntityNotFoundException;


    ListMessage<SharedFile> getAllSharedFileByUserEmail(String userEmail) throws EntityNotFoundException;

    Message<SharedFile> getSharedFileByPropertyId(String propertyId) throws EntityNotFoundException;

    Message<SharedFile> getSharedFileByInvestorEmailAndPropertyId(String propertyId, String userEmail) throws EntityNotFoundException;


    boolean isDocumentUrlExistsForLoggedInUser(String username, String url)
            throws com.example.HardMoneyLending.exception.general_exception.EntityNotFoundException,
            com.example.HardMoneyLending.exception.general_exception.BadRequestException;

    byte[] getFile(String filename, String type);
}
