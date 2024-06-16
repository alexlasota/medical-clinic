package com.alexlasota.medicalclinic.service;

import com.alexlasota.medicalclinic.model.MedicalUser;
import com.alexlasota.medicalclinic.model.Password;
import com.alexlasota.medicalclinic.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
}
