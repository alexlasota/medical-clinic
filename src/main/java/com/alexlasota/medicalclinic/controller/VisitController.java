package com.alexlasota.medicalclinic.controller;

import com.alexlasota.medicalclinic.mapper.VisitMapper;
import com.alexlasota.medicalclinic.model.*;
import com.alexlasota.medicalclinic.service.VisitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;
    private final VisitMapper visitMapper;

    @Operation(summary = "Get all visits", description = "Retrieves a list of all visits with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleVisitDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public List<SimpleVisitDto> getVisits(Pageable pageable) {
        List<Visit> visitsPage = visitService.getVisits(pageable);
        return visitsPage.stream()
                .map(visitMapper::visitToSimpleVisit)
                .toList();
    }

    @Operation(summary = "Create a new visit", description = "Creates a new visit and returns the created visit details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Visit created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleVisitDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SimpleVisitDto createVisit(@RequestBody VisitRequestDto visitRequestDto) {
        return visitService.createVisit(visitRequestDto);
    }

    @Operation(summary = "Assign patient to visit", description = "Assigns a patient to a specific visit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient assigned to visit successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VisitDto.class))),
            @ApiResponse(responseCode = "404", description = "Visit or patient not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PatchMapping("/{visitId}/patients/{patientId}")
    public VisitDto assignPatientToVisit(@PathVariable Long visitId, @PathVariable Long patientId) {
        return visitService.assignPatientToVisit(visitId, patientId);
    }

    @GetMapping("/patient/{patientId}")
    public List<VisitDto> getVisitsByPatientId(@PathVariable Long patientId) {
        return visitService.getVisitsByPatientId(patientId);
    }
    @GetMapping("/doctor/{doctorId}/available")
    public List<VisitDto> getAvailableVisitsByDoctorId(@PathVariable Long doctorId) {
        return visitService.getAvailableVisitsByDoctorId(doctorId);
    }

    @GetMapping("/specialization/{specialization}/date")
    public List<VisitDto> getAvailableVisitsBySpecializationAndDate(
            @PathVariable String specialization,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        return visitService.getAvailableVisitsBySpecializationAndDate(specialization, startDate, endDate);
    }
}