package com.alexlasota.medicalclinic.service;

import com.alexlasota.medicalclinic.exceptions.MedicalClinicException;
import com.alexlasota.medicalclinic.model.MedicalUser;
import com.alexlasota.medicalclinic.model.Password;
import com.alexlasota.medicalclinic.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.mockito.Mockito.when;


public class UserServiceTest {

    UserRepository userRepository;
    UserService userService;

    @BeforeEach
    void setup(){
        this.userRepository = Mockito.mock(UserRepository.class);
        this.userService = new UserService(userRepository);
    }

    @Test
    void updatePassword_UserExists_PasswordFormatCorrect_UserSaved(){
        //given
        String email = "al@gm.com";
        Password password = new Password("1234567");
        MedicalUser medicalUser = new MedicalUser();
        medicalUser.setEmail(email);
        medicalUser.setPassword(password.getPassword());
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(medicalUser));
        when(userRepository.save(medicalUser)).thenReturn(medicalUser);
        //when
        userService.updatePassword(email,password);
        //then
        Assertions.assertEquals("1234567", medicalUser.getPassword());
    }

    @Test
    void updatePassword_UserDoesntExist_ExceptionThrown(){
        String email = "al@gm.com";
        MedicalUser medicalUser = new MedicalUser();
        medicalUser.setEmail(email);
        Password password = new Password("1234567");
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        MedicalClinicException medicalClinicException = Assertions.assertThrows(MedicalClinicException.class,
                () -> userService.updatePassword(email, password));
        Assertions.assertEquals("User with given email doesn't exist", medicalClinicException.getMessage());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, medicalClinicException.getHttpStatus());
    }

    @Test
    void updatePassword_UserExists_PasswordFormatIncorrect_ExceptionThrown(){
        String email = "al@gm.com";
        String incorrectPassword = "1";
        MedicalUser medicalUser = new MedicalUser();
        medicalUser.setEmail(email);
        Password password = new Password(incorrectPassword);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(medicalUser));
        MedicalClinicException medicalClinicException = Assertions.assertThrows(MedicalClinicException.class,
                () -> userService.updatePassword(email, password));
        Assertions.assertEquals("Invalid password format", medicalClinicException.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, medicalClinicException.getHttpStatus());
    }
}