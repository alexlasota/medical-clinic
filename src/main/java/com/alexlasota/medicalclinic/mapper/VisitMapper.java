package com.alexlasota.medicalclinic.mapper;

import com.alexlasota.medicalclinic.model.SimpleVisitDto;
import com.alexlasota.medicalclinic.model.Visit;
import com.alexlasota.medicalclinic.model.VisitDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VisitMapper {

    @Mapping(target = "patient.id", source = "patient.id")
    @Mapping(target = "patient.firstName", source = "patient.medicalUser.firstName")
    @Mapping(target = "patient.lastName", source = "patient.medicalUser.lastName")
    @Mapping(target = "doctor.id", source = "doctor.id")
    @Mapping(target = "doctor.firstName", source = "doctor.medicalUser.firstName")
    VisitDto visitToVisitDto(Visit visit);

    List<VisitDto> mapListToDto(List<Visit> visits);

    @Mapping(target = "doctor.id", source = "doctor.id")
    @Mapping(target = "doctor.firstName", source = "doctor.medicalUser.firstName")
    @Mapping(target = "doctor.phoneNumber", source = "doctor.phoneNumber")
    @Mapping(target = "doctor.specialization", source = "doctor.specialization")
    SimpleVisitDto visitToSimpleVisit(Visit visit);
}
