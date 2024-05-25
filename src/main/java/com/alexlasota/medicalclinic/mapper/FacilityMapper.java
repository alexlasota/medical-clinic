package com.alexlasota.medicalclinic.mapper;

import com.alexlasota.medicalclinic.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FacilityMapper {

    @Mapping(source = "doctors", target = "doctorIds", qualifiedByName = "mapDoctorToIds")
    FacilityDto facilityToFacilityDto(Facility facility);

    List<FacilityDto> mapListToDto(List<Facility> facilityList);

    @Named("mapDoctorToIds")
    default List<Long> doctorsToIds(List<Doctor> doctors) {
        return doctors.stream()
                .map(Doctor::getId)
                .toList();
    }

    SimpleDoctorDto doctorToSimple(Doctor doctor);
}
