package com.example.HardMoneyLending.services.investor;

import com.example.HardMoneyLending.dto.investor.CreateInvestorDTO;
import com.example.HardMoneyLending.dto.investor.PaginatedInvestorRequest;
import com.example.HardMoneyLending.dto.investor.PaginatedInvestorResponse;
import com.example.HardMoneyLending.dto.investor.UpdateInvestorDTO;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception.DateTimeException.DateNotValidException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.exception.general_exception_graphql.UnprocessableException;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.models.investor.Investor;
import com.example.HardMoneyLending.repositories.investor.InvestorRepository;
import com.example.HardMoneyLending.services.user.IUserService;
import com.example.HardMoneyLending.utils.Message;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class InvestorService implements IInvestorService {

    private static final Logger LOG = LoggerFactory.getLogger(InvestorService.class);

    private static final String SERIAL_NO_PREFIX = "HML";
    private static final String SERIAL_NO_POSTFIX = "J";
    private static final String PROFILE_PIC_DIRECTORY = "investor-profile-pics";

    @Value("${parent.resource.location}")
    private String PARENT_RESOURCES;

    @Value("${investor.resource.location}")
    private String INVESTOR_RESOURCES;

    private final InvestorRepository investorRepository;
    private final IUserService userService;

    @Autowired
    public InvestorService(final InvestorRepository investorRepository,
                           @Lazy final IUserService userService) {
        this.investorRepository = investorRepository;
        this.userService = userService;
    }

    @Override
    public Investor getInvestor(String investorId) {
        Optional<Investor> investor = investorRepository.findById(investorId);
        if(investor == null || !investor.isPresent())
            throw new EntityNotFoundException("Investor not found with provided id.");
        return investor.get();
    }

    @Transactional
    @Override
    public Message<Investor> createInvestor(CreateInvestorDTO createInvestorDTO) throws DateNotValidException, EntityNotFoundException, UnprocessableException {
        User user = userService.getUserByUserId(createInvestorDTO.getUserId());
        Investor investor = new Investor();

        investor.setName(createInvestorDTO.getName());
        investor.setEmail(createInvestorDTO.getEmail());
        investor.setAddress(createInvestorDTO.getAddress());
        investor.setCity(createInvestorDTO.getCity());
        investor.setCountry(createInvestorDTO.getCountry());
        investor.setDob(createInvestorDTO.getDob());
        investor.setGender(createInvestorDTO.getGender());
        investor.setOccupation(createInvestorDTO.getOccupation());
        investor.setUserId(createInvestorDTO.getUserId());
        investor.setQuestions(createInvestorDTO.getQuestions());
        investor.setFeeStructureId(createInvestorDTO.getFeeStructureId());
        investor.setTelephone(createInvestorDTO.getTelephone());
        investor.setUserId(user.getUserId());

        try {
            // Fetched last investor record for creating serial no
            String serialNo = "";
            List<Investor> savedInvestor = investorRepository.findFirst3ByOrderByCreatedAtDesc();
            int counter = 0;
            if (savedInvestor == null || savedInvestor.isEmpty()) {
                serialNo = SERIAL_NO_PREFIX + String.format("%04d", ++counter) + SERIAL_NO_POSTFIX;
            } else {
                String lastSerialNo = savedInvestor.get(0).getSerialNo();
                int serialNumberFetched = Integer.parseInt(lastSerialNo.substring(3, lastSerialNo.length()-1));
                serialNo = SERIAL_NO_PREFIX + String.format("%04d", ++serialNumberFetched) + SERIAL_NO_POSTFIX;
            }
            investor.setSerialNo(serialNo);
            investor.setIsActive(Boolean.TRUE);
            investor.setCreatedAt(new Date());
            investor = investorRepository.save(investor);
            LOG.info("Saved investor data into our registry.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnprocessableException("Data saving issue");
        }
        return new Message<Investor>()
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("Investor Created Successfully.")
                .setData(investor);
    }

    @Override
    public Message<Investor> deleteInvestor(String investorId) {
        Optional<Investor> investor = investorRepository.findById(investorId);
        Investor investor1 = investor.get();
        try {
            if (investor != null) {
                investor1.setIsActive(false);
                investorRepository.save(investor1);
            }
            return new Message<Investor>().setStatus(200).setMessage("Investor Deleted Successfully").setData(investor1);
        } catch (Exception e) {
            return new Message<Investor>().setMessage(e.getMessage()).setStatus(400);
        }
    }

    @Override
    public Message<Investor> updateInvestor(UpdateInvestorDTO updateInvestorDTO)
            throws BadRequestException, EntityNotFoundException, UnprocessableException {
        Investor investor = getInvestorById(updateInvestorDTO.getInvestorId());
        investor.setName(updateInvestorDTO.getName());
        investor.setEmail(updateInvestorDTO.getEmail());
        investor.setDob(updateInvestorDTO.getDob());
        investor.setAddress(updateInvestorDTO.getAddress());
        investor.setCity(updateInvestorDTO.getCity());
        investor.setZip(updateInvestorDTO.getZip());
        investor.setState(updateInvestorDTO.getState());
        investor.setCountry(updateInvestorDTO.getCountry());
        investor.setContact(updateInvestorDTO.getContact());
        investor.setOccupation(updateInvestorDTO.getOccupation());
        investor.setGender(updateInvestorDTO.getGender());
        investor.setTelephone(updateInvestorDTO.getTelephone());
        investor.setAbout(updateInvestorDTO.getAbout());
        investor.setCompanyName(updateInvestorDTO.getCompanyName());
        investor.setAdditionalLanguage(updateInvestorDTO.getAdditionalLanguage());
        investor.setFeeStructureId(updateInvestorDTO.getFeeStructureId());
        investor.setQuestions(updateInvestorDTO.getQuestions());
        investor.setUpdatedAt(new Date());

        try {
            investorRepository.save(investor);
            log.info("Investor updated successfully.");
            return new Message<Investor>()
                    .setStatus(HttpStatus.OK.value())
                    .setCode(HttpStatus.OK.toString())
                    .setMessage("Investor updated successfully.")
                    .setData(investor);
        } catch (Exception e) {
            log.error("Error: "+e.getLocalizedMessage());
            throw new UnprocessableException("Investor updation is failed.");
        }
    }

    @Override
    public List<Investor> getAllInvestor() throws EntityNotFoundException {
        List<Investor> data = investorRepository.findAllByIsActive(true);
        if (data == null || data.isEmpty())
            throw new EntityNotFoundException("Investors not found.");
        return data;
    }

    @Override
    public Message<PaginatedInvestorResponse> getAllInvestorByPagination(PaginatedInvestorRequest dto, Pageable pageable) throws EntityNotFoundException {

        Page<Investor> result = null;
        if (dto.getName() == null && dto.getState() == null && dto.getCity() == null && dto.getFeeStructureId() == null) {
            result = investorRepository.findAll(pageable);
        }else {
            // JUGAAR: Spring boot jpa issue for mongodb that if we provide null with ContainingIgnoreCase query
            // then it will throw IllegalArgument exception so we have to use any special character for instead
            // null
            populateSpecialCharWhenProvidedNullFilterValues(dto);
            result = investorRepository.
                    findAllByNameContainingIgnoreCaseOrStateContainingIgnoreCaseOrCityContainingIgnoreCaseOrFeeStructureId(
                            dto.getName(), dto.getState(), dto.getCity(), dto.getFeeStructureId(), pageable);
        }
        if (result.getContent() == null || result.getContent().isEmpty())
            throw new EntityNotFoundException("No data found.");

        log.info("Fetched paginated data of investor");

        PaginatedInvestorResponse response = PaginatedInvestorResponse.builder()
                .investors(result.getContent())
                .pageNo(dto.getPageNo())
                .pageSize(dto.getPageSize())
                .totalRows(result.getTotalElements())
                .noOfPages(result.getTotalPages())
                .build();


        // Mapped investor to response type
        return new Message<PaginatedInvestorResponse>()
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("Successfully Fetched.")
                .setData(response);
    }

    private void populateSpecialCharWhenProvidedNullFilterValues(PaginatedInvestorRequest dto) {
        dto.setName(dto.getName() == null ? "@" : dto.getName() );
        dto.setCity(dto.getCity() == null ? "@" : dto.getCity() );
        dto.setState(dto.getState() == null ? "@" : dto.getState() );
        dto.setFeeStructureId(dto.getFeeStructureId() == null ? "@" : dto.getFeeStructureId());
    }

    @Override
    public Investor getInvestorByUserId(String userId) throws EntityNotFoundException, BadRequestException {
        if (userId == null || userId.isEmpty()) {
            log.error("User id should not be null.");
            throw new BadRequestException("User id should not be null.");
        }

        Investor investorProfile = investorRepository.findByUserId(userId);
        if (investorProfile == null) {
            log.error("Investor not found with userId: "+userId);
            throw new EntityNotFoundException("Investor with provided userId is not found.");
        }
        return investorProfile;
    }

    @Override
    public Investor getInvestorById(String id) throws EntityNotFoundException, BadRequestException {
        if (id == null || id.isEmpty()) {
            log.error("User id should not be null.");
            throw new BadRequestException("User id should not be null.");
        }

        Optional<Investor> investor = investorRepository.findById(id);
        if (!investor.isPresent()) {
            log.error("Investor not found with investor id: "+id);
            throw new EntityNotFoundException("Investor with provided investor id is not found.");
        }
        return investor.get();
    }

    @Override
    public Investor save(Investor investor) throws BadRequestException {
        try {
            return investorRepository.save(investor);
        } catch (Exception e) {
            log.error("Error: "+e.getLocalizedMessage());
            throw new BadRequestException("Some thing went wrong at backend side.");
        }
    }

    @Override
    public boolean uploadProfile(List<Part> parts, DataFetchingEnvironment environment) {
        // Create directory for investor profile pics if not already exists.
        String directoryName = PARENT_RESOURCES + INVESTOR_RESOURCES + "//" + PROFILE_PIC_DIRECTORY;
        File directory = new File(directoryName);

        // Check if the directories are already exists
        if (!directory.exists()) {
            directory.mkdirs();
        }

        List<Part> attachmentParts = environment.getArgument("files");
        int i = 1;
        for (Part part : attachmentParts) {
            String uploadName = "copy" + i;
            try {
                part.write(directoryName + "/" + part.getSubmittedFileName());
                log.info("File name: "+part.getSubmittedFileName());
            } catch (IOException e) {
                e.printStackTrace();
            }
            i++;
        }
        return true;
    }

    @Override
    public long countNoOfInvestorByUserIdList(List<String> recipientIdList) {
        return investorRepository.countByUserIdIn(recipientIdList);
    }
}
