package com.example.HardMoneyLending.service.property;

import com.example.HardMoneyLending.HardMoneyLendingApplication;
import com.example.HardMoneyLending.exception.general_exception.EntityNotFoundException;
import com.example.HardMoneyLending.models.property.Property;
import com.example.HardMoneyLending.services.property.IPropertyService;
import com.example.HardMoneyLending.utils.ListMessage;
import com.example.HardMoneyLending.utils.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HardMoneyLendingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PropertyServiceTest {

    @Autowired
    private IPropertyService propertyService;

    @Test
    public void testGetPropertyById() {
        try {
            Message<Property> list = propertyService.getPropertyById("630f3540e4b0982085d0f5e4", true);
            assertThat(list).isNotNull();
            assertThat(list.getStatus()).isEqualTo(HttpStatus.OK.value());
            System.out.println("Test Passed");
        }catch (com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException e){
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void testGetPropertyByIdForUpdate() {
        try {
            Message<Property> list = propertyService.getPropertyByIdForUpdate("630f3540e4b0982085d0f5e4", true);
            assertThat(list).isNotNull();
            assertThat(list.getStatus()).isEqualTo(HttpStatus.OK.value());
            System.out.println("Test Passed");
        }catch (com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testGetAllProperty() {
        try {
            ListMessage<Property> data = propertyService.getAllProperty(true);
            assertThat(data.getData()).isNotNull();
            assertThat(data.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(data.getData().size()).isGreaterThan(0);
            System.out.println("Test Passed");
        }catch (com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testGetAllPropertyByBorrowerId(){
        try {
            ListMessage<Property> data = propertyService.getAllPropertyByBorrowerId("6303692de114a041a2b0357a", true);
            assertThat(data.getData()).isNotNull();
            assertThat(data.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(data.getData().size()).isGreaterThan(0);
        }catch (com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testGetAllPropertyByPropertyTypeId(){
        try {
            ListMessage<Property> data = propertyService.getAllPropertyByPropertyTypeId("62ac2899f8d0a108e95d7294", true);
            assertThat(data.getData()).isNotNull();
            assertThat(data.getStatus()).isEqualTo(HttpStatus.OK.value());
            System.out.println("Test Passed");
        }catch (com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException e){
            System.out.println(e.getMessage());
        }
    }


}