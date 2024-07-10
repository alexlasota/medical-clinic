package com.alexlasota.medicalclinic.controller;

import com.alexlasota.medicalclinic.mapper.FacilityMapper;
import com.alexlasota.medicalclinic.model.Facility;
import com.alexlasota.medicalclinic.model.FacilityDto;
import com.alexlasota.medicalclinic.service.FacilityService;
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
@RequestMapping("/facilities")
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityService facilityService;
    private final FacilityMapper facilityMapper;

    @Operation(summary = "Get all facilities", description = "Retrieves a list of facilities with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FacilityDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public List<FacilityDto> getFacilities(Pageable pageable) {
        return facilityMapper.mapListToDto(facilityService.getFacilities(pageable));
    }

    @Operation(summary = "Add a new facility", description = "Adds a new facility to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Facility created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Facility.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Facility addFacility(@RequestBody Facility facility) {
        return facilityService.addFacility(facility);
    }

    @Operation(summary = "Get facility by ID", description = "Retrieves a facility by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved facility",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FacilityDto.class))),
            @ApiResponse(responseCode = "404", description = "Facility not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public FacilityDto getFacility(@PathVariable Long id) {
        return facilityMapper.facilityToFacilityDto(facilityService.getFacilityById(id));
    }

    @Operation(summary = "Assign facility to doctor", description = "Assigns a facility to a doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Facility assigned to doctor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Facility.class))),
            @ApiResponse(responseCode = "404", description = "Facility or doctor not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PatchMapping("/{facilityId}/doctors/{doctorId}")
    public Facility assignFacilityToDoctor(@PathVariable Long facilityId, @PathVariable Long doctorId) {
        facilityService.assignFacilityToDoctor(facilityId, doctorId);
        return facilityService.getFacilityById(facilityId);
    }
}