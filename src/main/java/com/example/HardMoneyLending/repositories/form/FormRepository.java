package com.example.HardMoneyLending.repositories.form;

import com.example.HardMoneyLending.models.form.Form;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FormRepository extends MongoRepository<Form, String> {
    List<Form> findByPropertyTypeId(String propertyTypeId);

    Set<Form> findAllFormByIdIn(List<String> data);

    Form findByName(String documentTypeName);

   // List<Form> findAllByIdInAndPropertyTpyeId(List<String> listOfFormsIds, String propertyTypeId);

    Set<Form> findAllByIdInAndPropertyTypeId(List<String> listOfFormsIds, String propertyTypeId);

    //  List<Form> findAllFormByFormIdsIn(List<String> listOfFormsIds);

}
