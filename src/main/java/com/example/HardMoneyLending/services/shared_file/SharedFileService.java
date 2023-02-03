package com.example.HardMoneyLending.services.shared_file;

import com.example.HardMoneyLending.constants.Constants;
import com.example.HardMoneyLending.dto.shared_file.CreateSharedFileDTO;
import com.example.HardMoneyLending.dto.shared_file.DocumentAccessDTO;
import com.example.HardMoneyLending.dto.shared_file.EmailsDocumentAccessDTO;
import com.example.HardMoneyLending.dto.shared_file.UpdateSharedFileDTO;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.models.property.Property;
import com.example.HardMoneyLending.models.shared_file.SharedFile;
import com.example.HardMoneyLending.repositories.shared_file.SharedFileRepository;
import com.example.HardMoneyLending.services.property.IPropertyService;
import com.example.HardMoneyLending.services.user.IUserService;
import com.example.HardMoneyLending.utils.ListMessage;
import com.example.HardMoneyLending.utils.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SharedFileService implements ISharedFileService{

    @Value("${parent.resource.location}")
    private String PARENT_RESOURCES;

    @Value("${borrower.resource.location}")
    private String BORROWER_RESOURCES;

    private final SharedFileRepository repo;

    private final IPropertyService propertyService;

    private final IUserService userService;

    @Autowired
    public SharedFileService(SharedFileRepository repo, IPropertyService propertyService, IUserService userService) {
        this.repo = repo;
        this.propertyService = propertyService;
        this.userService = userService;
    }

    @Override
    public Message<SharedFile> createSharedFile(CreateSharedFileDTO dto) throws EntityNotFoundException, BadRequestException{
        // find shared file by property id
        SharedFile sharedFileWithProperty = repo.findByProperty_PropertyId(dto.getPropertyId());

        // if property exist in shared file DB
        if(sharedFileWithProperty != null){
            // changes do in db data
            return editSharedFile(dto,sharedFileWithProperty);
        }

        // else create shared file the data in DB
        log.info("Creating Shared File in our database.");

        // find the property by propertyId
        Message<Property> propertyMessage = propertyService.getPropertyById(dto.getPropertyId(),true);
        Property property = propertyMessage.getData();

        //data save into shared file obj
        SharedFile sharedFile = new SharedFile(
                null,
                property,
                dto.getEmailsDocumentAccess(),
                System.currentTimeMillis(),
                null
        );

        try{
            // shared file data save in db

            sharedFile = repo.save(sharedFile);

            return new Message<SharedFile>()
                    .setStatus(HttpStatus.OK.value())
                    .setCode(HttpStatus.OK.toString())
                    .setMessage("Property is saved successfully.")
                    .setData(sharedFile);
        }
        catch (Exception e){
            log.error("Error: "+e.getLocalizedMessage());
            throw new BadRequestException("Database issue occurred.");
        }
    }

    private Message<SharedFile> editSharedFile(CreateSharedFileDTO dto, SharedFile dbSharedFile) {

        //check EmailsDocumentAccess of dto data same as data in DBSharedFile
        for (EmailsDocumentAccessDTO providedEmailsDocumentAccess : dto.getEmailsDocumentAccess()) {
            boolean isUserExist = false;
            for(EmailsDocumentAccessDTO i : dbSharedFile.getEmailsDocumentAccess()){
                // if user email found
                if(i.getEmail().equals(providedEmailsDocumentAccess.getEmail()) && !isUserExist){
                    isUserExist = true;
                    log.info("user exit");

                    //check DocumentAccess of dto data same as data in DBSharedFile
                    for (DocumentAccessDTO providedDocumentAccess : providedEmailsDocumentAccess.getDocumentAccess()){

                        boolean isDocumentExist = false;
                        for (DocumentAccessDTO e : i.getDocumentAccess()){

                            // if document found by Url
                            if (e.getDocumentUrl().equals(providedDocumentAccess.getDocumentUrl()) && !isDocumentExist) {
                                //set AccessType data in DBSharedFile
                                isDocumentExist = true;
                                e.getAccessType().addAll(providedDocumentAccess.getAccessType());

                            }

                        }
                        //else documentUrl not found then save DocumentAccessDTO
                        if(!isDocumentExist){
                            DocumentAccessDTO documentAccessDTO = new DocumentAccessDTO();
                            documentAccessDTO.setAccessType(providedDocumentAccess.getAccessType());
                            documentAccessDTO.setDocumentUrl(providedDocumentAccess.getDocumentUrl());
                            i.getDocumentAccess().add(documentAccessDTO);
                        }

                    }

                }
            }
            //if user email not found then save EmailsDocumentAccessDTO
            if(!isUserExist){
                EmailsDocumentAccessDTO emailsDocumentAccessDTO = new EmailsDocumentAccessDTO();
                emailsDocumentAccessDTO.setEmail(providedEmailsDocumentAccess.getEmail());
                emailsDocumentAccessDTO.setDocumentAccess(providedEmailsDocumentAccess.getDocumentAccess());
                dbSharedFile.getEmailsDocumentAccess().add(emailsDocumentAccessDTO);
                log.info("no user exit");
            }

        }

        try {
            repo.save(dbSharedFile);
            log.info("Property created 2 successfully with property id: " + dbSharedFile.getProperty().getPropertyId());
            return new Message<SharedFile>()
                    .setStatus(HttpStatus.OK.value())
                    .setCode(HttpStatus.OK.toString())
                    .setMessage("Shared File created 2 successfully.")
                    .setData(dbSharedFile);
        } catch (Exception e) {
            log.error("Error: "+ e.getLocalizedMessage());
            throw new BadRequestException("Internal database issue occurred.");
        }

    }

    @Override
    public Message<SharedFile> updateSharedFile(UpdateSharedFileDTO dto) throws EntityNotFoundException{

        // check shared file id should be provided
        if(dto.getSharedFileId() == null || dto.getSharedFileId().isEmpty()){
            log.error("Shared file Id " + dto.getSharedFileId() +" should not be null or empty.");
            throw new EntityNotFoundException("Provided Shared file Id should not be null or empty. ");
        }

        // delete the existing data
        repo.deleteById(dto.getSharedFileId());

        // create and set data in createSharedFileDTO
        CreateSharedFileDTO createDto = new CreateSharedFileDTO();
        createDto.setPropertyId(dto.getPropertyId());
        createDto.setEmailsDocumentAccess(dto.getEmailsDocumentAccess());

        // save the updated data in DB
        log.error("Shared file Updating... ");
        return createSharedFile(createDto);

    }

    @Override
    public Message<SharedFile> getSharedFileById(String id) throws EntityNotFoundException {

        if (id==null || id.isEmpty()){
            log.error("Shared file Id should not be null or empty.");
            throw new EntityNotFoundException("Shared file Id should not be null or empty.");
        }

        Optional<SharedFile> sharedFile = repo.findBySharedFileId(id);
        if (sharedFile == null || !sharedFile.isPresent()) {
            log.error("Shared file not found with id: "+id);
            throw new EntityNotFoundException("Shared file not found.");
        }

        return new Message<SharedFile>()
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("Shared file fetched successfully.")
                .setData(sharedFile.get());
    }

    @Override
    public ListMessage<SharedFile> getAllSharedFile() throws EntityNotFoundException{
        List<SharedFile> sharedFileList = repo.findAll();

        if (sharedFileList==null || sharedFileList.isEmpty()){
            log.error("No Shared Files found");
            throw new EntityNotFoundException("No Shared Files found");
        }

        return new ListMessage<SharedFile>()
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("Shared Files successfully fetched.")
                .setData(sharedFileList);
    }

    @Override
    public ListMessage<SharedFile> getAllSharedFileByUserEmail(String userEmail) throws EntityNotFoundException{

        if (userEmail==null || userEmail.isEmpty()){
            log.error("User email should not be null or empty.");
            throw new EntityNotFoundException("User email should not be null or empty.");
        }

        List<SharedFile> sharedFileList = repo.findAllSharedFileByEmailsDocumentAccess_Email(userEmail);

        if (sharedFileList==null || sharedFileList.isEmpty()){
            log.error("No shared file found with given user email");
            throw new EntityNotFoundException("No shared file found with given user email");
        }
        return new ListMessage<SharedFile>()
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("Successfully fetched Shared files with given user email.")
                .setData(sharedFileList);
    }

    @Override
    public Message<SharedFile> getSharedFileByPropertyId(String propertyId) throws EntityNotFoundException {

        if (propertyId==null || propertyId.isEmpty()){
            log.error("Property Id should not be null or empty.");
            throw new EntityNotFoundException("Property Id should not be null or empty.");
        }

        SharedFile sharedFile = repo.findByProperty_PropertyId(propertyId);

        if (sharedFile==null){
            log.error("No shared file found with given property Id");
            throw new EntityNotFoundException("No shared file found with given property Id");
        }

        return new Message<SharedFile>()
                .setCode(HttpStatus.OK.toString())
                .setStatus(HttpStatus.OK.value())
                .setMessage("Successfully fetched Shared files with given property Id.")
                .setData(sharedFile);
    }

    @Override
    public Message<SharedFile> getSharedFileByInvestorEmailAndPropertyId(String propertyId, String userEmail) throws EntityNotFoundException{

        if (propertyId==null || propertyId.isEmpty() ){
            log.error("Property Id should not be null or empty.");
            throw new EntityNotFoundException("Property Id should not be null or empty.");
        }

        SharedFile sharedFile = repo.findSharedFileByProperty_PropertyIdAndEmailsDocumentAccess_Email(propertyId,userEmail);

        if (sharedFile==null){
            log.error("No shared file found with given property id and user email of investor");
            throw new EntityNotFoundException("No shared file found with given property id and user email of investor");
        }
        return new Message<SharedFile>()
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("Successfully fetched Shared files with given property id and user email.")
                .setData(sharedFile);
    }


    @Override
    public boolean isDocumentUrlExistsForLoggedInUser (String userEmail , String docUrl)
            throws com.example.HardMoneyLending.exception.general_exception.EntityNotFoundException,
            com.example.HardMoneyLending.exception.general_exception.BadRequestException {
        if(docUrl==null || docUrl.isEmpty()){
            log.error("Document Url " + docUrl +" should not be null or empty. ");
            throw new com.example.HardMoneyLending.exception.general_exception.BadRequestException("Provided Document Url should not be null or empty. ");
        }

        SharedFile sharedFile = repo.findSharedFileByEmailsDocumentAccess_DocumentAccess_DocumentUrlAndEmailsDocumentAccess_Email(docUrl,userEmail);
        if (sharedFile==null) {
            log.error("Document is not found with url: "+docUrl);
            throw new com.example.HardMoneyLending.exception.general_exception.EntityNotFoundException("Document is not found with provided url.");
        }
        return true;
    }

    @Override
    public byte[] getFile(String filename, String type){
        String directoryPath = null;
        switch (type) {
            case Constants.PROPERTY_FILE:
                directoryPath = PARENT_RESOURCES + BORROWER_RESOURCES + Constants.BORROWER_PROPERTY_FILE_DIRECTORY;
                break;
        }
        File file = new File(directoryPath+"//"+filename);
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            log.error("Error: "+e.getLocalizedMessage());
            log.error("Error getting property file.");
            return null;
        }
    }


}
