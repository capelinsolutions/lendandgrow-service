package com.example.HardMoneyLending.service.borrower;

import com.example.HardMoneyLending.dto.borrower.CreateBorrowerDTO;
import com.example.HardMoneyLending.dto.borrower.PaginatedBorrowerRequest;
import com.example.HardMoneyLending.dto.borrower.UpdateBorrowerDTO;
import com.example.HardMoneyLending.models.borrower.Borrower;

import java.time.LocalDate;

public class BorrowerDtoInit {

    public static CreateBorrowerDTO createBorrower_withValidData(){
        CreateBorrowerDTO createBorrowerDTO = new CreateBorrowerDTO();
        createBorrowerDTO.setName("Muhammad Tahir");
        createBorrowerDTO.setEmail("tahir26@gmail.com");
        createBorrowerDTO.setCity("Karachi");
        createBorrowerDTO.setAddress("ABC address");
        createBorrowerDTO.setContact("0333895964");
        createBorrowerDTO.setCountry("Pakistan");
        createBorrowerDTO.setDob(LocalDate.now());
        createBorrowerDTO.setGender("MALE");
        createBorrowerDTO.setOccupation("Software Developer");
        createBorrowerDTO.setState("Sindh");
        createBorrowerDTO.setZip("75300");
        createBorrowerDTO.setTelephone("0213454551");
        return createBorrowerDTO;
    }

    public static CreateBorrowerDTO createBorrower_withInValidData(){
        CreateBorrowerDTO createBorrowerDTO = new CreateBorrowerDTO();
        createBorrowerDTO.setName("Muhammad Tahir");
        createBorrowerDTO.setCity("Karachi");
        createBorrowerDTO.setAddress("ABC address");
        createBorrowerDTO.setContact("0333895964");
        createBorrowerDTO.setCountry("Pakistan");
        createBorrowerDTO.setDob(LocalDate.now());
        createBorrowerDTO.setGender("MALE");
        createBorrowerDTO.setOccupation("Software Developer");
        createBorrowerDTO.setState("Sindh");
        createBorrowerDTO.setZip("75300");
        createBorrowerDTO.setTelephone("0213454551");
        return createBorrowerDTO;
    }

    public static UpdateBorrowerDTO updateBorrower_withValidData(){
        UpdateBorrowerDTO updateBorrowerDTO = new UpdateBorrowerDTO();
        updateBorrowerDTO.setBorrowerId("62ce957846f95307878cf8e2");
        updateBorrowerDTO.setUserId("62ce957846f95307878cf8e1");
        updateBorrowerDTO.setName("Muhammad Tahir");
        updateBorrowerDTO.setEmail("david64@mailinator.com");
        updateBorrowerDTO.setCity("Karachi");
        updateBorrowerDTO.setAddress("ABC address");
        updateBorrowerDTO.setContact("0333895964");
        updateBorrowerDTO.setCountry("Pakistan");
        updateBorrowerDTO.setDob(LocalDate.now());
        updateBorrowerDTO.setGender("MALE");
        updateBorrowerDTO.setOccupation("Software Developer");
        updateBorrowerDTO.setState("Sindh");
        updateBorrowerDTO.setZip("75300");
        updateBorrowerDTO.setTelephone("0213454551");
        updateBorrowerDTO.setCompanyName("Daraz");
        return updateBorrowerDTO;
    }

    public static PaginatedBorrowerRequest createPaginatedBorrowerRequest_withValidData() {
        PaginatedBorrowerRequest paginatedBorrowerRequest = new PaginatedBorrowerRequest();
//        paginatedBorrowerRequest = new PaginatedBorrowerRequest();
//        paginatedBorrowerRequest.setName(null);
//        paginatedBorrowerRequest.setCity(null);
//        paginatedBorrowerRequest.setState(null);
        paginatedBorrowerRequest.setPropertyTypeTitle("Single Family");
        paginatedBorrowerRequest.setPageNo(0);
        paginatedBorrowerRequest.setPageSize(10);
        return paginatedBorrowerRequest;
    }

}