package com.alexlasota.medicalclinic.controller;

import com.alexlasota.medicalclinic.mapper.PatientMapper;
import com.alexlasota.medicalclinic.model.PatientDto;
import com.alexlasota.medicalclinic.service.PatientService;
import com.alexlasota.medicalclinic.model.Patient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final PatientMapper patientMapper;


    @Operation(summary = "Get all patients", description = "Retrieves a list of patients with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public List<PatientDto> getPatients(Pageable pageable) {
        return patientMapper.mapListToDto(patientService.getPatients(pageable));
    }

    @Operation(summary = "Add a new patient", description = "Adds a new patient to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Patient.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Patient addPatient(@RequestBody Patient patient) {
        patientService.addPatient(patient);
        return patient;
    }

    @Operation(summary = "Remove a patient by email", description = "Removes a patient from the system by their email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Patient removed",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid email",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUserByEmail(@PathVariable String email) {
        patientService.removeUserByEmail(email);
    }

    @Operation(summary = "Edit a patient's details", description = "Updates the details of an existing patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PutMapping("/{email}")
    public PatientDto editPatient(@PathVariable String email, @RequestBody Patient newPatientData) {
        return patientMapper.patientToPatientDto(patientService.editPatient(email, newPatientData));
    }
}