package com.alexlasota.medicalclinic.mapper;

import com.alexlasota.medicalclinic.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    @Mapping(source = "facilities", target = "facilityIds", qualifiedByName = "mapFacilityToIds")
    @Mapping(source = "medicalUser", target = "userDto")
    DoctorDto doctorToDoctorDto(Doctor doctor);


    SimpleDoctorDto doctorToSimpleDoctorDto(Doctor doctor);

    List<DoctorDto> mapListToDto(List<Doctor> doctorList);

    SimpleFacilityDto facilityToSimple(Facility facility);

    UserDto userToUserDto(MedicalUser medicalUser);

    @Named("mapFacilityToIds")
    default List<Long> facilitiesToIds(List<Facility> facilities) {
        return facilities.stream()
                .map(Facility::getId)
                .toList();
    }
}