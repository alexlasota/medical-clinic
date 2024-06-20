package com.alexlasota.medicalclinic.controller;

import com.alexlasota.medicalclinic.model.Doctor;
import com.alexlasota.medicalclinic.model.Facility;
import com.alexlasota.medicalclinic.service.DoctorService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class DoctorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    DoctorService doctorService;

    @Test
    void getDoctors_DoctorsExist_DoctorsReturned() throws Exception {
        Doctor doctor1 = new Doctor();
        doctor1.setId(1L);
        doctor1.setPhoneNumber("123");
        Doctor doctor2 = new Doctor();
        doctor2.setId(2L);
        when(doctorService.getDoctors(any()))
                .thenReturn(List.of(doctor1, doctor2));

        mockMvc.perform(MockMvcRequestBuilders.get("/doctors")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].phoneNumber").value("123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2L));
    }

    @Test
    void addDoctor_DoctorExist_DoctorAddedAndReturned() throws Exception {
        Doctor doctor1 = new Doctor();
        doctor1.setId(1L);
        doctor1.setPhoneNumber("123");
        when(doctorService.addDoctor(doctor1)).thenReturn(doctor1);

        mockMvc.perform(MockMvcRequestBuilders.post("/doctors")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(doctor1)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("123"));
    }

    @Test
    void getDoctorById_DoctorExists_DoctorReturned() throws Exception {
        Doctor doctor1 = new Doctor();
        doctor1.setId(3L);
        doctor1.setPhoneNumber("123");
        when(doctorService.getDoctorById(doctor1.getId())).thenReturn(doctor1);

        mockMvc.perform(MockMvcRequestBuilders.get("/doctors/{id}", 3L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(3L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("123"));
    }

    @Test
    void assignDoctorToFacility_DoctorAndFacilityExist_DoctorAssignedToFacility() throws Exception {
        Facility facility = new Facility();
        facility.setId(1L);
        List<Facility> facilities = new ArrayList<>();
        facilities.add(facility);
        Doctor doctor = new Doctor();
        doctor.setId(3L);
        doctor.setPhoneNumber("123");
        doctor.setFacilities(facilities);


        when(doctorService.assignDoctorToFacility(doctor.getId(), facility.getId())).thenReturn(doctor);

        mockMvc.perform(MockMvcRequestBuilders.patch("/doctors/{doctorId}/facilities/{facilityId}", 3L, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(3L));


    }
}
