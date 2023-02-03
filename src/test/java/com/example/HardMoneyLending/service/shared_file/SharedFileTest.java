package com.example.HardMoneyLending.service.shared_file;


import com.example.HardMoneyLending.HardMoneyLendingApplication;
import com.example.HardMoneyLending.exception.general_exception.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception.EntityNotFoundException;
import com.example.HardMoneyLending.models.borrower.Borrower;
import com.example.HardMoneyLending.models.shared_file.SharedFile;
import com.example.HardMoneyLending.services.shared_file.ISharedFileService;
import com.example.HardMoneyLending.services.shared_file.SharedFileService;
import com.example.HardMoneyLending.utils.ListMessage;
import com.example.HardMoneyLending.utils.Message;
import org.junit.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HardMoneyLendingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SharedFileTest {


    @Autowired
    private ISharedFileService sharedFileService;

    @Test
    public void testGetSharedFileById(){
        try{
            Message<SharedFile> data = sharedFileService.getSharedFileById("62fb67da82a3ed7831d5fb0d");
            assertThat(data.getData()).isNotNull();
            assertThat(data.getStatus()).isEqualTo(HttpStatus.OK.value());
        }catch (com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testGetAllSharedFile(){
        try{
            ListMessage<SharedFile> data = sharedFileService.getAllSharedFile();
            assertThat(data.getData()).isNotNull();
            assertThat(data.getData().size()).isGreaterThan(0);
            assertThat(data.getStatus()).isEqualTo(HttpStatus.OK.value());
            System.out.println("Test Passed");
        }catch (com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testGetSharedFileByUserEmail(){
        try {
            ListMessage<SharedFile> data = sharedFileService.getAllSharedFileByUserEmail("tahir2@mailinator.com");
            assertThat(data.getData()).isNotNull();
            assertThat(data.getData().size()).isGreaterThan(0);
            assertThat(data.getStatus()).isEqualTo(HttpStatus.OK.value());
            System.out.println("Test Passed");
        }catch (com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testGetSharedFileByPropertyId(){
        try {
            Message<SharedFile> data = sharedFileService.getSharedFileByPropertyId("62bbf03e8b91f5661c99be7d");
            assertThat(data.getData()).isNotNull();
            assertThat(data.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(data.getData().getProperty().getPropertyId()).isEqualTo("62bbf03e8b91f5661c99be7d");
            System.out.println("Test Passed");
        }catch (com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testGetSharedFileByInvestorEmailAndPropertyId(){
        try {
            Message<SharedFile> data = sharedFileService.getSharedFileByInvestorEmailAndPropertyId("62e8adf3553ff10721aeebd3","junaid11@mailinator.com");
            assertThat(data.getData()).isNotNull();
            assertThat(data.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(data.getData().getProperty().getPropertyId()).isEqualTo("62e8adf3553ff10721aeebd3");
            System.out.println("Test Passed");
        }catch (com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testSharedFileFunction_LoggedUserExistWithDocumentUrl() throws BadRequestException, EntityNotFoundException {
        try {
            boolean data = sharedFileService.isDocumentUrlExistsForLoggedInUser("junaid11@mailinator.com","http://localhost:8082/api/v1/property/download?filename=1661147394917qasimpdf.pdf&type=PROPERTY_FILE");
            assertThat(data).isEqualTo(true);
            System.out.println("Test Passed");
        }catch (com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException |
                com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException e){
            System.out.println(e.getMessage());
        }
    }
}
