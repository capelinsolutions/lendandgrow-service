package com.example.HardMoneyLending.services.borrower;

import com.example.HardMoneyLending.dto.borrower.CreateBorrowerDTO;
import com.example.HardMoneyLending.dto.borrower.PaginatedBorrowerRequest;
import com.example.HardMoneyLending.dto.borrower.PaginatedBorrowerResponse;
import com.example.HardMoneyLending.dto.borrower.UpdateBorrowerDTO;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotUpdateException;
import com.example.HardMoneyLending.models.borrower.Borrower;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.repositories.borrower.BorrowerRepository;
import com.example.HardMoneyLending.services.user.IUserService;
import com.example.HardMoneyLending.utils.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BorrowerService implements IBorrowerService {

    private final BorrowerRepository borrowerRepository;

    private final IUserService iUserService;

    @Autowired
    public BorrowerService(final BorrowerRepository borrowerRepository, @Lazy IUserService iUserService) {
        this.borrowerRepository = borrowerRepository;
        this.iUserService = iUserService;
    }

    @Override
    public Borrower getBorrower(String borrowerId) throws BadRequestException, EntityNotFoundException {
        if (borrowerId == null || borrowerId.isEmpty())
            throw new BadRequestException("Borrower id should not be null.");
        Optional<Borrower> borrower = borrowerRepository.findById(borrowerId);
        if(!borrower.isPresent())
            throw new EntityNotFoundException("Borrower not found.");
        return borrower.get();
    }

    @Transactional
    @Override
    public Message<Borrower> createBorrower(CreateBorrowerDTO createBorrowerDTO) throws BadRequestException{
        try {
            Borrower borrower = new Borrower();
            borrower.setName(createBorrowerDTO.getName());
            borrower.setEmail(createBorrowerDTO.getEmail());
            borrower.setAddress(createBorrowerDTO.getAddress());
            borrower.setCity(createBorrowerDTO.getCity());
            borrower.setZip(createBorrowerDTO.getZip());
            borrower.setState(createBorrowerDTO.getState());
            borrower.setCountry(createBorrowerDTO.getCountry());
            borrower.setContact(createBorrowerDTO.getContact());
            borrower.setDob(createBorrowerDTO.getDob());
            borrower.setGender(createBorrowerDTO.getGender());
            borrower.setOccupation(createBorrowerDTO.getOccupation());
            borrower.setUserId(createBorrowerDTO.getUserId());
            if(createBorrowerDTO.getTelephone() != null && createBorrowerDTO.getTelephone().isEmpty()){
                borrower.setTelephone(createBorrowerDTO.getTelephone());
            }
            borrower.setCreatedAt(new Date());
            borrower.setIsActive(Boolean.TRUE);
            borrower = borrowerRepository.save(borrower);
            return new Message<Borrower>().setStatus(200).setMessage("Borrower Created Successfully").setData(borrower);
        } catch(Exception e){
            return new Message<Borrower>().setMessage(e.getMessage()).setStatus(400);
        }
    }

    @Transactional
    @Override
    public Message<Borrower> updateBorrower(UpdateBorrowerDTO updateBorrowerDto)
            throws BadRequestException, EntityNotFoundException, EntityNotUpdateException {
        Borrower borrower = getBorrower(updateBorrowerDto.getBorrowerId());
        borrower.setName(updateBorrowerDto.getName());
        borrower.setDob(updateBorrowerDto.getDob());
        borrower.setAddress(updateBorrowerDto.getAddress());
        borrower.setCity(updateBorrowerDto.getCity());
        borrower.setZip(updateBorrowerDto.getZip());
        borrower.setState(updateBorrowerDto.getState());
        borrower.setCountry(updateBorrowerDto.getCountry());
        borrower.setContact(updateBorrowerDto.getContact());
        borrower.setTelephone(updateBorrowerDto.getTelephone());
        borrower.setGender(updateBorrowerDto.getGender());
        borrower.setOccupation(updateBorrowerDto.getOccupation());
        borrower.setCompanyName(updateBorrowerDto.getCompanyName());
        borrower.setUpdatedAt(new Date());

        User user = iUserService.getUserByUserId(updateBorrowerDto.getUserId());
        user.setCity(updateBorrowerDto.getCity());
        user.setAddress(updateBorrowerDto.getAddress());
        user.setContact(updateBorrowerDto.getContact());
        user.setName(updateBorrowerDto.getName());
        user.setCountry(updateBorrowerDto.getCountry());

        try {
            iUserService.save(user);
            borrower = borrowerRepository.save(borrower);
            log.info("Borrower updated successfully.");
            return new Message<Borrower>()
                    .setStatus(HttpStatus.OK.value())
                    .setCode(HttpStatus.OK.toString())
                    .setMessage("Borrower updated successfully.")
                    .setData(borrower);
        } catch (Exception e) {
            log.error("Error: "+e.getLocalizedMessage());
            throw new EntityNotUpdateException("Borrower not updated.");
        }
    }

    @Override
    public Message<Borrower> deleteBorrower(String borrowerId) {
        Optional<Borrower> borrower = borrowerRepository.findById(borrowerId);

        try {
            if (!borrower.isPresent()) {
                return new Message<Borrower>().setMessage("Borrower not found with id: "+borrowerId).setStatus(400);
            }
            Borrower borrower1 = borrower.get();
            borrower1.setIsActive(false);
            borrowerRepository.save(borrower1);
            return new Message<Borrower>().setStatus(200).setMessage("Borrower Created Successfully").setData(borrower1);
        } catch (Exception e) {
            return new Message<Borrower>().setMessage(e.getMessage()).setStatus(400);
        }
    }

    @Override
    public List<Borrower> getAllBorrower() throws EntityNotFoundException {
        List<Borrower> data = borrowerRepository.findAll();
        if (data == null || data.isEmpty()) {
            log.error("No borrower data found.");
            throw new EntityNotFoundException("Borrower not found.");
        }
        return data;
    }

    @Override
    public Message<PaginatedBorrowerResponse> getAllBorrowerByPagination(PaginatedBorrowerRequest dto, Pageable pageable) throws EntityNotFoundException {

        Page<Borrower> resulting = null;
        if (dto.getName() == null && dto.getState() == null && dto.getCity() == null && dto.getPropertyTypeId() == null) {
            resulting = borrowerRepository.findAll(pageable);
        }else {
            // JUGAAR: Spring boot jpa issue for mongodb that if we provide null with ContainingIgnoreCase query
            // then it will throw IllegalArgument exception so we have to use any special character for instead
            // null
            populateSpecialCharWhenProvidedNullFilterValues(dto);
            resulting = borrowerRepository.
                    findAllByNameContainingIgnoreCaseOrStateContainingIgnoreCaseOrCityContainingIgnoreCaseOrPropertyType_propertyTypeId(
                            dto.getName(), dto.getState(), dto.getCity(), dto.getPropertyTypeId(), pageable);
        }
        if (resulting.getContent() == null || resulting.getContent().isEmpty())
            throw new EntityNotFoundException("No data found.");

        PaginatedBorrowerResponse response = PaginatedBorrowerResponse.builder()
                .borrowers(resulting.getContent())
                .pageNo(dto.getPageNo())
                .pageSize(dto.getPageSize())
                .noOfPages(resulting.getTotalPages())
                .totalRows(resulting.getTotalElements())
                .build();

        return new Message<PaginatedBorrowerResponse>()
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("Successfully Fetched.")
                .setData(response);
    }

    private void populateSpecialCharWhenProvidedNullFilterValues(PaginatedBorrowerRequest dto) {
        dto.setName(dto.getName() == null ? "@" : dto.getName() );
        dto.setCity(dto.getCity() == null ? "@" : dto.getCity() );
        dto.setState(dto.getState() == null ? "@" : dto.getState() );
        dto.setPropertyTypeId(dto.getPropertyTypeId() == null ? "@" : dto.getPropertyTypeId());
    }

    @Override
    public boolean isBorrowerExists(String borrowerId) throws BadRequestException, EntityNotFoundException {
        if (borrowerId == null || borrowerId.isEmpty()) {
            log.error("Borrower id: "+borrowerId+" should not be null or empty.");
            throw new BadRequestException("Provided borrower id should not be null or empty.");
        }

        boolean isExists = borrowerRepository.existsByBorrowerId(borrowerId);
        if (!isExists) {
            log.error("Borrower is not found with id: "+borrowerId);
            throw new EntityNotFoundException("Borrower is not found with provided id.");
        }
        return isExists;
    }

    @Override
    public Borrower getBorrowerByUserId(String userId) throws EntityNotFoundException {
        Optional<Borrower> borrower = borrowerRepository.findByUserId(userId);
        if(!borrower.isPresent())
            throw new EntityNotFoundException("Borrower not found.");
        return borrower.get();
    }

    @Override
    public Borrower save(Borrower borrower) throws BadRequestException {
        try {
            return borrowerRepository.save(borrower);
        } catch (Exception e) {
            log.error("Error: "+e.getLocalizedMessage());
            throw new BadRequestException("Some thing went wrong at backend side.");
        }
    }

    @Override
    public long countNoOfBorrowersByUserIdList(List<String> recipientIdList) {
        return borrowerRepository.countByUserIdIn(recipientIdList);
    }
}
