package com.example.HardMoneyLending.service.form;

import com.example.HardMoneyLending.HardMoneyLendingApplication;
import com.example.HardMoneyLending.exception.general_exception.BadRequestException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HardMoneyLendingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FormTypeServiceTest {

    /*private CreateFormTypeDTO textField;
    private CreateFormTypeDTO textArea;
    private CreateFormTypeDTO checkBox;
    private CreateFormTypeDTO radioButton;

    @Autowired
    private IFormTypeService service;*/

    @Before
    public void init() {
        /*textField = new CreateFormTypeDTO();
        textField.setName("Text Field");
        textField.setValue("textField");

        textArea = new CreateFormTypeDTO();
        textArea.setName("Text Area");
        textArea.setValue("textArea");

        checkBox = new CreateFormTypeDTO();
        checkBox.setName("Check Box");
        checkBox.setValue("checkBox");

        radioButton = new CreateFormTypeDTO();
        radioButton.setName("Radio Button");
        radioButton.setValue("radioButton");*/
    }

    @Test
    public void createFormType_whenProvidedValidData() throws BadRequestException {
        /*Message<FormType> response = service.createFormType(textField);
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        response = service.createFormType(textArea);
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        response = service.createFormType(checkBox);
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        response = service.createFormType(radioButton);
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());*/

    }
}

