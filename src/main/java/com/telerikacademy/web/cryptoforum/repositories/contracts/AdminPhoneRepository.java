package com.telerikacademy.web.cryptoforum.repositories.contracts;

import com.telerikacademy.web.cryptoforum.models.AdminPhone;

public interface AdminPhoneRepository {
    AdminPhone getPhoneNumber(String phoneNumber);


    void addPhoneNumber(AdminPhone adminPhone);
}

