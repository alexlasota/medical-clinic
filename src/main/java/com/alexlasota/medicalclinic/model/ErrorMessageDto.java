package com.alexlasota.medicalclinic.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Data
public class ErrorMessageDto {

    private final String message;
    private final HttpStatus httpStatus;
}
