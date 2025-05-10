package com.example;

import com.example.utils.ErrorToolManager;
import com.example.utils.enums.Form;
import com.example.utils.services.ValidationService;
import com.example.utils.services.ValidationService.UserModelValidations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserValidationsTest {

    @Mock
    private ErrorToolManager errorToolManager;

    private ValidationService validationService;
    private UserModelValidations validator;

    @BeforeEach
    void setup() {
        validationService = new ValidationService();
        validator = validationService.new UserModelValidations(errorToolManager);
    }

    @Test
    void testPasswordMatchSuccess() {
        assertTrue(validator.confirmedPassword("secret", "secret", Form.ADDACCOUNT));
        assertFalse(validator.confirmedPassword("secret", "secret2", Form.ADDACCOUNT));

        // You can verify interactions if needed:
        // verify(errorToolManager).addError(...);
    }
}
