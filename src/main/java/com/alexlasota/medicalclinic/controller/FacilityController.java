package com.alexlasota.medicalclinic.controller;

import com.alexlasota.medicalclinic.mapper.FacilityMapper;
import com.alexlasota.medicalclinic.model.Facility;
import com.alexlasota.medicalclinic.model.FacilityDto;
import com.alexlasota.medicalclinic.service.FacilityService;
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

    @GetMapping
    public List<FacilityDto> getFacilities(Pageable pageable) {
        return facilityMapper.mapListToDto(facilityService.getFacilities(pageable));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Facility addFacility(@RequestBody Facility facility) {
        return facilityService.addFacility(facility);
    }

    @GetMapping("/{id}")
    public FacilityDto getFacility(@PathVariable Long id) {
        return facilityMapper.facilityToFacilityDto(facilityService.getFacilityById(id));
    }

    @PatchMapping("/{facilityId}/doctors/{doctorId}")
    public Facility assignFacilityToDoctor(@PathVariable Long facilityId, @PathVariable Long doctorId) {
        facilityService.assignFacilityToDoctor(facilityId, doctorId);
        return facilityService.getFacilityById(facilityId);
    }
}
