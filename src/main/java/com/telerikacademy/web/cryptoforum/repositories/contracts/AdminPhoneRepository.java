package com.telerikacademy.web.cryptoforum.repositories.contracts;

import com.telerikacademy.web.cryptoforum.models.AdminPhone;
import com.telerikacademy.web.cryptoforum.models.User;

public interface AdminPhoneRepository {

    AdminPhone getPhoneNumber(String phoneNumber);

    void addPhoneNumber(AdminPhone adminPhone);

    AdminPhone getPhoneNumberById(int phoneId);

    void removePhoneFromAdmin(AdminPhone adminPhone);
}

