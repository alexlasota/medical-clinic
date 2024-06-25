package com.alexlasota.medicalclinic.mapper;

import com.alexlasota.medicalclinic.model.MedicalUser;
import com.alexlasota.medicalclinic.model.Patient;
import com.alexlasota.medicalclinic.model.PatientDto;
import com.alexlasota.medicalclinic.model.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    @Mapping(source = "medicalUser", target = "userDto")
    PatientDto patientToPatientDto(Patient patient);

    UserDto userToUserDto(MedicalUser medicalUser);

    List<PatientDto> mapListToDto(List<Patient> patientList);
}