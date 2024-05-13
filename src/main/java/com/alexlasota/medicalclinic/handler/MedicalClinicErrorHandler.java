package com.alexlasota.medicalclinic.handler;

import com.alexlasota.medicalclinic.exceptions.MedicalClinicException;
import com.alexlasota.medicalclinic.model.ErrorMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MedicalClinicErrorHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> onIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(illegalArgumentException.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> onException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unknown Error");
    }

    @ExceptionHandler(MedicalClinicException.class)
    public ResponseEntity<ErrorMessageDto> medicalException(MedicalClinicException medicalClinicException) {
        return ResponseEntity.status(medicalClinicException.getHttpStatus())
                .body(new ErrorMessageDto(medicalClinicException.getMessage(), medicalClinicException.getHttpStatus()));
    }

}