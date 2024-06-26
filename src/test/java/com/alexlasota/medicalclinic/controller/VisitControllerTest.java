package com.alexlasota.medicalclinic.controller;

import com.alexlasota.medicalclinic.mapper.VisitMapper;
import com.alexlasota.medicalclinic.model.*;
import com.alexlasota.medicalclinic.service.VisitService;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class VisitControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    VisitMapper visitMapper;

    @MockBean
    VisitService visitService;

    @Test
    void getVisits_VisitsExist_VisitsReturned() throws Exception {
        Visit visit1 = new Visit();
        visit1.setVisitStartDate(LocalDateTime.of(2023, 6, 1, 9, 0));
        visit1.setId(1L);
        Visit visit2 = new Visit();
        visit2.setVisitStartDate(LocalDateTime.of(2023, 7, 1, 9, 0));
        visit2.setId(2L);

        when(visitService.getVisits(any()))
                .thenReturn(List.of(visit1, visit2));

        mockMvc.perform(MockMvcRequestBuilders.get("/visits")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].visitStartDate").value("2023-06-01T09:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].visitStartDate").value("2023-07-01T09:00:00"));


    }

    @Test
    void createVisit_VisitExists_VisitCreatedAndReturned() throws Exception {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setPhoneNumber("12345");
        VisitRequestDto visitRequestDto = new VisitRequestDto();
        visitRequestDto.setDoctorId(1L);
        visitRequestDto.setVisitStartDate(LocalDateTime.of(2023, 6, 1, 9, 0));
        SimpleVisitDto simpleVisitDto = new SimpleVisitDto();
        Visit visit = new Visit();
        visit.setDoctor(doctor);
        visit.setVisitStartDate(visitRequestDto.getVisitStartDate());
        visit.setVisitEndDate(visitRequestDto.getVisitEndDate());

        when(visitService.createVisit(visitRequestDto)).thenReturn(simpleVisitDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/visits")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(visitRequestDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}