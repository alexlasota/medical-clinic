package com.alexlasota.medicalclinic.controller;

import com.alexlasota.medicalclinic.mapper.VisitMapper;
import com.alexlasota.medicalclinic.model.*;
import com.alexlasota.medicalclinic.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/visit")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;
    private final VisitMapper visitMapper;

    @PostMapping("/create")
    public SimpleVisitDto createVisit(@RequestBody VisitRequestDto visitRequestDto) {
        return visitService.createVisit(visitRequestDto);
    }

    @GetMapping("/all")
    public List<SimpleVisitDto> getAllVisits() {
        List<Visit> visits = visitService.getVisits();
        return visits.stream()
                .map(visitMapper::visitToSimpleVisit)
                .toList();
    }

    @PatchMapping("/{visitId}/{patientId}")
    public VisitDto assignPatientToVisit(@PathVariable Long visitId, @PathVariable String patientId) {
        return visitService.assignPatientToVisit(visitId, patientId);
    }
}
