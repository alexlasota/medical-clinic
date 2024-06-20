package com.alexlasota.medicalclinic.controller;

import com.alexlasota.medicalclinic.mapper.PatientMapper;
import com.alexlasota.medicalclinic.model.MedicalUser;
import com.alexlasota.medicalclinic.model.Patient;
import com.alexlasota.medicalclinic.model.PatientDto;
import com.alexlasota.medicalclinic.service.PatientService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PatientMapper patientMapper;

    @MockBean
    PatientService patientService;

    @Test
    void getPatients_PatientsExist_PatientsReturned() throws Exception {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setIdCardNo("ab12");
        Patient patient1 = new Patient();
        patient1.setId(2L);
        patient1.setIdCardNo("cd34");
        when(patientService.getPatients(any()))
                .thenReturn(List.of(patient, patient1));

        mockMvc.perform(MockMvcRequestBuilders.get("/patients")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].idCardNo").value("cd34"));
    }

    @Test
    void addPatient_PatientExist_PatientAddedAndReturned() throws Exception {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setPhoneNumber("12345");
        patient.setIdCardNo("a1");
        when(patientService.addPatient(patient)).thenReturn(patient);

        mockMvc.perform(MockMvcRequestBuilders.post("/patients")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patient)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idCardNo").value("a1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("12345"));
    }

    @Test
    void removeUserByEmail_UserExists_UserDeleted() throws Exception {
        String email = "al@gmail.com";
        MedicalUser medicalUser = new MedicalUser();
        medicalUser.setEmail(email);
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setMedicalUser(medicalUser);

        doNothing().when(patientService).removeUserByEmail(email);
        mockMvc.perform(MockMvcRequestBuilders.delete("/patients/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void editPatient_PatientExists_PatientEdited() throws Exception {
        String email = "al@gmail.com";
        MedicalUser medicalUser = new MedicalUser();
        medicalUser.setEmail(email);
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setMedicalUser(medicalUser);

        Patient updatedPatient = new Patient();
        updatedPatient.setId(1L);

        PatientDto patientDto = patientMapper.patientToPatientDto(patient);
        patientDto.setId(1L);

        when(patientService.editPatient(email, patient)).thenReturn(updatedPatient);

        mockMvc.perform(MockMvcRequestBuilders.put("/patients/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patient)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
    }
}