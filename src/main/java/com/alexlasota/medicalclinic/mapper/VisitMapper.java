package com.alexlasota.medicalclinic.mapper;

import com.alexlasota.medicalclinic.model.Visit;
import com.alexlasota.medicalclinic.model.VisitDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")

public interface VisitMapper {

    VisitDto visitToVisitDto (Visit visit);

    List<VisitDto> mapListToDto (List<Visit> visits);
}
