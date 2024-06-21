package com.alexlasota.medicalclinic.controller;

import com.alexlasota.medicalclinic.mapper.VisitMapper;
import com.alexlasota.medicalclinic.model.*;
import com.alexlasota.medicalclinic.service.VisitService;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;
    private final VisitMapper visitMapper;

    @GetMapping
    public List<SimpleVisitDto> getVisits(Pageable pageable) {
        List<Visit> visitsPage = visitService.getVisits(pageable);
        return visitsPage.stream()
                .map(visitMapper::visitToSimpleVisit)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SimpleVisitDto createVisit(@RequestBody VisitRequestDto visitRequestDto) {
        return visitService.createVisit(visitRequestDto);
    }


    @PatchMapping("/{visitId}/patients/{patientId}")
    public VisitDto assignPatientToVisit(@PathVariable Long visitId, @PathVariable Long patientId) {
        return visitService.assignPatientToVisit(visitId, patientId);
    }
}