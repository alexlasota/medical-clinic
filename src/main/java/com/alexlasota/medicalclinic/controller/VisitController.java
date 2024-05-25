package com.alexlasota.medicalclinic.controller;

import com.alexlasota.medicalclinic.mapper.VisitMapper;
import com.alexlasota.medicalclinic.model.Patient;
import com.alexlasota.medicalclinic.model.Visit;
import com.alexlasota.medicalclinic.model.VisitDto;
import com.alexlasota.medicalclinic.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/visit")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;
    private final VisitMapper visitMapper;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public VisitDto createVisit(@RequestParam Long doctorId,
                                @RequestParam LocalDateTime visitStartDate,
                                @RequestParam LocalDateTime visitEndDate) {
        Visit visit = visitService.createVisit(doctorId, visitStartDate, visitEndDate);
        return visitMapper.visitToVisitDto(visit);
    }



    //getVisits

    //addVisit

    //assignPatientToVisit
}
