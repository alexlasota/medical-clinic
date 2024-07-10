package com.alexlasota.medicalclinic.controller;

import com.alexlasota.medicalclinic.mapper.DoctorMapper;
import com.alexlasota.medicalclinic.model.Doctor;
import com.alexlasota.medicalclinic.model.DoctorDto;
import com.alexlasota.medicalclinic.service.DoctorService;
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
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    @Operation(summary = "Get all doctors", description = "Retrieves a list of doctors with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public List<DoctorDto> getDoctors(Pageable pageable) {
        return doctorMapper.mapListToDto(doctorService.getDoctors(pageable));
    }

    @Operation(summary = "Add a new doctor", description = "Adds a new doctor to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Doctor created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Doctor.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Doctor addDoctor(@RequestBody Doctor doctor) {
        return doctorService.addDoctor(doctor);
    }

    @Operation(summary = "Get doctor by ID", description = "Retrieves a doctor by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved doctor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDto.class))),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public DoctorDto getDoctorById(@PathVariable Long id) {
        return doctorMapper.doctorToDoctorDto(doctorService.getDoctorById(id));
    }

    @Operation(summary = "Assign doctor to facility", description = "Assigns a doctor to a facility")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor assigned to facility",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDto.class))),
            @ApiResponse(responseCode = "404", description = "Doctor or facility not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PatchMapping("/{doctorId}/facilities/{facilityId}")
    public DoctorDto assignDoctorToFacility(@PathVariable Long doctorId, @PathVariable Long facilityId) {
        Doctor doctor = doctorService.assignDoctorToFacility(doctorId, facilityId);
        return doctorMapper.doctorToDoctorDto(doctor);
    }
}