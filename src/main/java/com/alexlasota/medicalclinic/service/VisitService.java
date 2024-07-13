package com.alexlasota.medicalclinic.service;

import com.alexlasota.medicalclinic.exceptions.MedicalClinicException;
import com.alexlasota.medicalclinic.mapper.VisitMapper;
import com.alexlasota.medicalclinic.model.*;
import com.alexlasota.medicalclinic.repository.DoctorRepository;
import com.alexlasota.medicalclinic.repository.PatientRepository;
import com.alexlasota.medicalclinic.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;

    // TC1: W przypadku gdy zostanie wywolana metoda z visitRepo findAll to zostanie zwrocona lista wizyt.
    public List<Visit> getVisits(Pageable pageable) {
        return visitRepository.findAll(pageable).getContent();
    }

    //TC1: W przypadku gdy metoda validateQuarterHour zwraca prawidlowe dane, istnieje doctor o danym id, wizyty nie nachodzą na siebie oraz data start nie jest po dacie end
    //przydzielamy doctora do wizyty, ustawiamy date startu oraz endu wizyty oraz wywolujemy metode save z visitRepo gdzie zapisujemy wizyte
    //TC2: W przypadku gdy metoda validateQuarterHour nie zwraca prawidlowych danych poleci wyjatek
    //TC2: W przypadku gdy metoda validateQuarterHour zwraca prawidlowe dane, nie istnieje doktor o danym id powinien poleciec wyjatek
    //TC3: W przypadku gdy metoda validateQuarterHour zwraca prawidlowe dane, istnieje doktor o danym id ale wizyty pokrywają się godzinami to powinien poleciec wyjatek
    //TC4: W przypadku gdy metoda validateQuarterHour zwraca prawidlowe dane, istnieje doktor o danym id, wizyty nie pokrywają się godzinami

    @Transactional
    public SimpleVisitDto createVisit(VisitRequestDto visitRequestDto) {
        validateQuarterHour(visitRequestDto.getVisitStartDate());
        validateQuarterHour(visitRequestDto.getVisitEndDate());

        Doctor doctor = doctorRepository.findById(visitRequestDto.getDoctorId())
                .orElseThrow(() -> new MedicalClinicException(HttpStatus.BAD_REQUEST, "Doctor not found"));

        if (!visitRepository.findAllOverlapping(visitRequestDto.getDoctorId(), visitRequestDto.getVisitStartDate(), visitRequestDto.getVisitEndDate()).isEmpty()) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Cannot create visit. Visits are overlapping!");
        }

        if (visitRequestDto.getVisitStartDate().isAfter(visitRequestDto.getVisitEndDate())) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Visit start date cannot be after end date");
        }

        Visit visit = new Visit();
        visit.setDoctor(doctor);
        visit.setVisitStartDate(visitRequestDto.getVisitStartDate());
        visit.setVisitEndDate(visitRequestDto.getVisitEndDate());

        Visit savedVisit = visitRepository.save(visit);
        return visitMapper.visitToSimpleVisit(savedVisit);
    }

    //TC1: W przypadku gdy istnieje patient o danym id i wizyta o danym id oraz wizyta nie ma jeszcze przypisanego pacjenta
    //przydzielamy do wizyty pacjenta, wywolujemy metode save z visitRepo ktora zapisuje pacjenta
    //TC2: W przypadku gdy nie istnieje pacjent o danym id powinien poleciec wyjatek
    //TC3: W przypadku gdzy istnieje pacjent o danym id ale nie istnieje wizyta o danym id powinien poleciec wyjatek
    //TC4: W przypadku gdy istnieje pacjent o danym id oraz wizyta o danym id ale wizyta ma juz przypisanego pacjenta powinien poleciec wyjatek
    @Transactional
    public VisitDto assignPatientToVisit(Long visitId, Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new MedicalClinicException(HttpStatus.BAD_REQUEST, "Patient not found"));
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new MedicalClinicException(HttpStatus.BAD_REQUEST, "Visit not found"));

        if (visit.getPatient() != null) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Visit already assigned");
        }
        visit.setPatient(patient);

        visitRepository.save(visit);
        return visitMapper.visitToVisitDto(visit);
    }

    private void validateQuarterHour(LocalDateTime dateTime) {
        int minute = dateTime.getMinute();
        if (minute % 15 != 0) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Visit time must be on a quarter-hour mark (e.g., 14:00, 14:15, 14:30, etc.)");
        }
    }

    public List<VisitDto> getVisitsByPatientId(Long patientId) {
        return visitRepository.findVisitsByPatientId(patientId);
    }
    public List<VisitDto> getAvailableVisitsByDoctorId(Long doctorId) {
        return visitRepository.findAvailableVisitsByDoctorId(doctorId);
    }

    public List<VisitDto> getAvailableVisitsBySpecializationAndDate(String specialization, LocalDateTime startDate, LocalDateTime endDate) {
        return visitRepository.findAvailableVisitsBySpecializationAndDate(specialization, startDate, endDate);
    }
}