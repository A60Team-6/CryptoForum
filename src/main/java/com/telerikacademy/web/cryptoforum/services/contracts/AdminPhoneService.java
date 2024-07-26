package com.telerikacademy.web.cryptoforum.services.contracts;

import com.telerikacademy.web.cryptoforum.models.AdminPhone;
import com.telerikacademy.web.cryptoforum.models.User;

public interface AdminPhoneService {

    void addPhoneNumber(AdminPhone adminPhone, User user);

}
