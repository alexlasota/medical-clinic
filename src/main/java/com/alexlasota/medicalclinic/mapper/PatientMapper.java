package com.alexlasota.medicalclinic.mapper;

import com.alexlasota.medicalclinic.model.Patient;
import com.alexlasota.medicalclinic.model.PatientDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientDto patientToPatientDto(Patient patient);

    List<PatientDto> mapListToDto(List<Patient> patientList);
}

