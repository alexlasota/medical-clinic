package com.alexlasota.medicalclinic;

import org.springframework.stereotype.Component;

@Component
public class Patient {

    private String email;
    private String password;
    private Long idCardNo;
    private String firstName;
    private String lastName;
    private int phoneNumber;
    private String birthday;

    public Patient(String email, String password, Long idCardNo, String firstName, String lastName, int phoneNumber, String birthday) {
        this.email = email;
        this.password = password;
        this.idCardNo = idCardNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
    }

    public void updateDetails(String email, String password, Long idCardNo,
                              String firstName, String lastName,
                              int phoneNumber, String birthday) {
        this.email = email;
        this.password = password;
        this.idCardNo = idCardNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(Long idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "PatientModel{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", idCardNo=" + idCardNo +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", birthday='" + birthday + '\'' +
                '}';
    }
}
