package com.alexlasota.medicalclinic.controller;

import com.alexlasota.medicalclinic.model.MedicalUser;
import com.alexlasota.medicalclinic.model.Password;
import com.alexlasota.medicalclinic.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @Test
    void updatePassword_UserWithEmailExists_PasswordUpdated() throws Exception {
        String email = "al@gmail.com";
        MedicalUser medicalUser = new MedicalUser();
        medicalUser.setId(1L);
        medicalUser.setFirstName("ALex");
        medicalUser.setLastName("lasota");
        medicalUser.setEmail(email);
        medicalUser.setPassword("123");
        Password newPassword = new Password("abc123");
        when(userService.updatePassword(any(),any())).thenReturn(medicalUser);

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/al@gmail.com/password")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newPassword)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
