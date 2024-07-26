package com.telerikacademy.web.cryptoforum.services;

import com.telerikacademy.web.cryptoforum.exceptions.DuplicateEntityException;
import com.telerikacademy.web.cryptoforum.exceptions.EntityNotFoundException;
import com.telerikacademy.web.cryptoforum.helpers.PermissionHelper;
import com.telerikacademy.web.cryptoforum.models.AdminPhone;
import com.telerikacademy.web.cryptoforum.models.User;
import com.telerikacademy.web.cryptoforum.repositories.contracts.AdminPhoneRepository;
import com.telerikacademy.web.cryptoforum.services.contracts.AdminPhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminPhoneServiceImpl implements AdminPhoneService {

    private final AdminPhoneRepository adminPhoneRepository;

    @Autowired
    public AdminPhoneServiceImpl(AdminPhoneRepository adminPhoneRepository) {
        this.adminPhoneRepository = adminPhoneRepository;
    }

    @Override
    public void addPhoneNumber(AdminPhone adminPhone, User user) {
        PermissionHelper.isAdmin(user, "Only admins can have phone number.");
        boolean duplicateExists = true;
        try {
            adminPhoneRepository.getPhoneNumber(adminPhone.getPhoneNumber());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Phone", "number", adminPhone.getPhoneNumber());
        }

        adminPhone.setUser(user);
        adminPhoneRepository.addPhoneNumber(adminPhone);
    }

}
