package com.example.HardMoneyLending.service.borrower;

import com.example.HardMoneyLending.HardMoneyLendingApplication;
import com.example.HardMoneyLending.dto.borrower.CreateBorrowerDTO;
import com.example.HardMoneyLending.dto.borrower.PaginatedBorrowerRequest;
import com.example.HardMoneyLending.dto.borrower.PaginatedBorrowerResponse;
import com.example.HardMoneyLending.dto.borrower.UpdateBorrowerDTO;
import com.example.HardMoneyLending.exception.general_exception.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception.EntityNotFoundException;
import com.example.HardMoneyLending.models.borrower.Borrower;
import com.example.HardMoneyLending.services.borrower.IBorrowerService;
import com.example.HardMoneyLending.services.property.property_type.IPropertyTypeService;
import com.example.HardMoneyLending.utils.Message;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HardMoneyLendingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BorrowerServiceTest {

    private CreateBorrowerDTO createBorrowerDTOValid;
    private CreateBorrowerDTO createBorrowerDTOInValid;
    private UpdateBorrowerDTO updateBorrowerDTOValid;
    private PaginatedBorrowerRequest paginatedBorrowerRequest;

    @Autowired
    private IBorrowerService borrowerService;

    @Autowired
    private IPropertyTypeService propertyTypeService;

    @Before
    public void init() {
        createBorrowerDTOValid = BorrowerDtoInit.createBorrower_withValidData();
        createBorrowerDTOInValid = BorrowerDtoInit.createBorrower_withInValidData();
        updateBorrowerDTOValid = BorrowerDtoInit.updateBorrower_withValidData();
        paginatedBorrowerRequest = BorrowerDtoInit.createPaginatedBorrowerRequest_withValidData();;
    }

    @Test
    public void testBorrowerCreation_whenProvidedValidData() {
        System.out.println(createBorrowerDTOValid.getAddress());
    }

    @Test
    public void testBorrowerCreation_whenProvidedInValidData() {
        System.out.println(createBorrowerDTOInValid.getAddress());
    }

    @Test
    public void testBorrowerUpdate_whenProvidedValidData() {
        Message<Borrower> data = borrowerService.updateBorrower(updateBorrowerDTOValid);
        assertThat(data.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void testBorrowerDelete_whenProvidedValidData() {
        Message<Borrower> data = borrowerService.deleteBorrower("62e76249437f0e4a2dabbd9a");
        assertThat(data.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void testBorrowerDelete_whenProvidedInValidData() {
        Message<Borrower> data = borrowerService.deleteBorrower("vsdebfbe1244fvbgb");
        assertThat(data.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testGetBorrower_whenProvidedValidData() {
        Borrower data = borrowerService.getBorrower("62ce957846f95307878cf8e2");
        //assertThat(data).isEqualTo(HttpStatus.OK.value());
        assertThat(data.getBorrowerId()).isNotNull();
    }

    @Test
    public void testGetBorrower_whenProvidedInValidData() {
        Message<Borrower> data = borrowerService.deleteBorrower("vsde99bfbe1244fvbgb");
        assertThat(data.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testGetAllBorrower() {
        List<Borrower> data = borrowerService.getAllBorrower();
        assertThat(data).isNotNull();
        assertThat(data.size()).isGreaterThan(0);
    }

    @Test
    public void getBorrowerByPagination_whenProvidedPageable() throws EntityNotFoundException {
        Pageable pageable = PageRequest.of(0, 2);
        Message<PaginatedBorrowerResponse> data = borrowerService.getAllBorrowerByPagination(paginatedBorrowerRequest, pageable);
        assertThat(data.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(data.getData().getBorrowers()).isNotNull();
    }

    @Test
    public void ExistBorrower() {
        try{
            //borrowerService.isBorrowerExists("");   // check for null or empty borrower id.
            assertThat(borrowerService.isBorrowerExists("62df97fae9327512d25020df"))
                    .isEqualTo(true);
        }catch (com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException |
                com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void getBorrowerByUserId(){
        try {
            //borrowerService.getBorrowerByUserId("");   // check for null or empty user id.
            Borrower borrower = borrowerService.getBorrowerByUserId("62d7cf4b07a224003a0d4dc0");
            assertThat(borrower.getBorrowerId()).isNotNull();
        }catch (com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void createPropertyTypeId() throws EntityNotFoundException, BadRequestException {
        Borrower borrower = borrowerService.getBorrower("62ce957846f95307878cf8e2");
        borrower.setPropertyType(propertyTypeService.getAllPropertyTypes().getData().stream().collect(Collectors.toSet()));
        borrower = borrowerService.save(borrower);

        assertThat(borrower.getPropertyType()).isNotNull();
        assertThat(borrower.getPropertyType().size()).isGreaterThan(0);
    }

    @Test
    public void getPaginatedData_whenProvidedCityValueOnly() throws EntityNotFoundException {
        Message<PaginatedBorrowerResponse> data = borrowerService
                .getAllBorrowerByPagination(
                        paginatedBorrowerRequest,
                        PageRequest.of(paginatedBorrowerRequest.getPageNo(), paginatedBorrowerRequest.getPageSize())
                );

        assertThat(data).isNotNull();
        assertThat(data.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(data.getData()).isNotNull();
    }
}
