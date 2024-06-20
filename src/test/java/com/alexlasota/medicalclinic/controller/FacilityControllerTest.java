package com.alexlasota.medicalclinic.controller;

import com.alexlasota.medicalclinic.mapper.FacilityMapper;
import com.alexlasota.medicalclinic.model.Doctor;
import com.alexlasota.medicalclinic.model.Facility;
import com.alexlasota.medicalclinic.model.FacilityDto;
import com.alexlasota.medicalclinic.service.FacilityService;
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
public class FacilityControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    FacilityMapper facilityMapper;

    @MockBean
    FacilityService facilityService;

    @Test
    void getFacilities_FacilitiesExist_FacilitiesReturned() throws Exception {
        Facility facility = createFacility(1L, "ABC");
        Facility facility2 = createFacility(2L, "ABCD");

        when(facilityService.getFacilities(any()))
                .thenReturn(List.of(facility, facility2));

        mockMvc.perform(MockMvcRequestBuilders.get("/facilities")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("ABC"));
    }

    @Test
    void createFacility_FacilityExists_FacilityAddedAndReturned() throws Exception {
        Facility facility = createFacility(1L, "ABC");
        when(facilityService.addFacility(facility)).thenReturn(facility);
        mockMvc.perform(MockMvcRequestBuilders.post("/facilities")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(facility)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("ABC"));
    }

    @Test
    void getFacilityById_FacilityExists_ReturnFacility() throws Exception {
        Facility facility = createFacility(1L, "ABC");
        when(facilityService.getFacilityById(facility.getId())).thenReturn(facility);
        mockMvc.perform(MockMvcRequestBuilders.get("/facilities/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("ABC"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
    }

    @Test
    void assignFacilityToDoctor_FacilityAndDoctorExist_FacilityAssignedToDoctor() throws Exception {
        Facility facility = new Facility();
        facility.setId(1L);
        facility.setDoctors(new ArrayList<>());
        Doctor doctor = new Doctor();
        doctor.setId(2L);
        FacilityDto facilityDto = facilityMapper.facilityToFacilityDto(facility);
        when(facilityService.assignFacilityToDoctor(facility.getId(), doctor.getId())).thenReturn(facilityDto);
        when(facilityService.getFacilityById(1L)).thenReturn(facility);

        mockMvc.perform(MockMvcRequestBuilders.patch("/facilities/{facilityId}/doctors/{doctorId}", 1L, 2L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
    }

    Facility createFacility(Long id, String name) {
        Facility facility = new Facility();
        facility.setId(id);
        facility.setName(name);
        ArrayList<Doctor> doctors = new ArrayList<>();
        Doctor doctor = new Doctor();
        doctor.setPhoneNumber("123");
        doctors.add(doctor);
        facility.setDoctors(doctors);

        return facility;
    }
}
