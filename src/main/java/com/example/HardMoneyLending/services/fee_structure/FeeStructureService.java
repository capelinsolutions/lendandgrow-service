package com.example.HardMoneyLending.services.fee_structure;

import com.example.HardMoneyLending.dto.fee_structure.CreateFeeStructureDTO;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotSavedException;
import com.example.HardMoneyLending.exception.general_exception_graphql.UnprocessableException;
import com.example.HardMoneyLending.models.fee_structure.FeeStructure;
import com.example.HardMoneyLending.repositories.fee_structure.FeeStructureRepository;
import com.example.HardMoneyLending.utils.ListMessage;
import com.example.HardMoneyLending.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeeStructureService implements IFeeStructureService{

    private FeeStructureRepository repo;

    @Autowired
    public FeeStructureService(FeeStructureRepository repo) {
        this.repo = repo;
    }

    @Override
    public Message<FeeStructure> createFeeStructure(CreateFeeStructureDTO createFeeStructureDTO) throws EntityNotSavedException {
        Message<FeeStructure> m = new Message<>();
        try {
            FeeStructure feeStructure = new FeeStructure();
            feeStructure.setText(createFeeStructureDTO.getText());
            feeStructure.setTo(createFeeStructureDTO.getTo());
            feeStructure.setFrom(createFeeStructureDTO.getFrom());
            feeStructure.setActive(true);
            repo.save(feeStructure);
            return m.setData(feeStructure)
                    .setMessage("Created fee structure.")
                    .setStatus(HttpStatus.CREATED.value())
                    .setCode(HttpStatus.CREATED.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntityNotSavedException("Entity not saved in registry.");
        }
    }

    @Override
    public Message<FeeStructure> deleteFeeStructureById(String feeStructureId) throws EntityNotFoundException, UnprocessableException {

        try {
            FeeStructure feeStructure = repo.findByIdAndActive(feeStructureId, true);
            if(feeStructure == null)
                throw new EntityNotFoundException("Fee structure with id: "+feeStructureId+" is not found.");
            feeStructure.setActive(false);
            repo.save(feeStructure);
            Message<FeeStructure> m = new Message<>();
            return m.setMessage("Fee structure successfully deleted.")
                    .setData(feeStructure)
                    .setCode(HttpStatus.OK.toString())
                    .setStatus(HttpStatus.OK.value());
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnprocessableException("Delete operation is not successful.");
        }
    }

    @Override
    public ListMessage<FeeStructure> getAllFeeStructure() {
        List<FeeStructure> data = repo.findAllByActive(true);
        ListMessage<FeeStructure> response = new ListMessage<>();
        if(data == null)
            return response.setCode(HttpStatus.NOT_FOUND.toString()).setStatus(HttpStatus.NOT_FOUND.value()).setMessage("There is no data available");
        return response.setCode(HttpStatus.OK.toString()).setStatus(HttpStatus.OK.value()).setMessage("Successfully fetched.").setData(data);
    }

    @Override
    public Message<FeeStructure> getFeeStructureById(String id) {
        Optional<FeeStructure> feeStructure = repo.findById(id);
        Message<FeeStructure> response = new Message<>();
        return (!feeStructure.isPresent() ?
                response.setMessage("No data found.").setStatus(HttpStatus.NOT_FOUND.value()).setCode(HttpStatus.NOT_FOUND.toString()) :
                response.setMessage("Successfully fetched.").setStatus(HttpStatus.OK.value()).setCode(HttpStatus.OK.toString()).setData(feeStructure.get())
                );
    }
}
