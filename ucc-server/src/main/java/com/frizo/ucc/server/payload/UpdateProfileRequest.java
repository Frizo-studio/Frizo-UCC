package com.frizo.ucc.server.payload;

import org.springframework.web.multipart.MultipartFile;

public class UpdateProfileRequest {
    private String name;
    private Character gender;
    private int phoneNumber;
    private String address;
    private String collageLocation;
    private String collageName;
    private String majorSubject;
    private String grade;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCollageLocation() {
        return collageLocation;
    }

    public void setCollageLocation(String collageLocation) {
        this.collageLocation = collageLocation;
    }

    public String getCollageName() {
        return collageName;
    }

    public void setCollageName(String collageName) {
        this.collageName = collageName;
    }

    public String getMajorSubject() {
        return majorSubject;
    }

    public void setMajorSubject(String majorSubject) {
        this.majorSubject = majorSubject;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }
}
