package com.alexlasota.medicalclinic;

import java.util.List;

public class PatientManager {

    private List<Patient> patients;

    public List<Patient> getPatients() {
        return patients;
    }

    public Patient getPatientByEmail(String email) {
        for (Patient patient : patients) {
            if (patient.getEmail().equals(email)) {
                return patient;
            }
        }
        return null;
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
    }
    public void removePatientByEmail(String email){
        for (Patient patient : patients) {
            if (patient.getEmail().equals(email)){
                patients.remove(patient);
            }
        }
    }

    public void updatePatientByEmail(String email){

    }
}

