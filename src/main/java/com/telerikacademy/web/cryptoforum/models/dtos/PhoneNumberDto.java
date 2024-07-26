package com.telerikacademy.web.cryptoforum.models.dtos;

import jakarta.validation.constraints.Size;

public class PhoneNumberDto {

    @Size(max = 15, message = "Phone number shout be valid")
    private String phoneNumber;

    public PhoneNumberDto() {
    }

    public PhoneNumberDto(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
