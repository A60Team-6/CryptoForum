package com.telerikacademy.web.cryptoforum.services;

import com.telerikacademy.web.cryptoforum.exceptions.DuplicateEntityException;
import com.telerikacademy.web.cryptoforum.exceptions.EntityNotFoundException;
import com.telerikacademy.web.cryptoforum.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.cryptoforum.helpers.PermissionHelper;
import com.telerikacademy.web.cryptoforum.models.AdminPhone;
import com.telerikacademy.web.cryptoforum.models.User;
import com.telerikacademy.web.cryptoforum.repositories.contracts.AdminPhoneRepository;
import com.telerikacademy.web.cryptoforum.repositories.contracts.UserRepository;
import com.telerikacademy.web.cryptoforum.services.contracts.AdminPhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminPhoneServiceImpl implements AdminPhoneService {

    public static final String ADMIN = "You are not an admin";
    private final AdminPhoneRepository adminPhoneRepository;
    private final UserRepository userRepository;

    @Autowired
    public AdminPhoneServiceImpl(AdminPhoneRepository adminPhoneRepository, UserRepository userRepository) {
        this.adminPhoneRepository = adminPhoneRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AdminPhone getPhoneNumberById(int phoneNumberId, User user){
        PermissionHelper.isAdmin(user, ADMIN);
        return adminPhoneRepository.getPhoneNumberById(phoneNumberId);
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

    @Override
    public void removePhoneFromAdmin(User user, int phoneId) {

        PermissionHelper.isAdmin(user, ADMIN);
        AdminPhone phoneNumber = adminPhoneRepository.getPhoneNumberById(phoneId);

        if (phoneNumber.getUser().getId() != user.getId()) {
            throw new UnauthorizedOperationException("You do not have permission to remove this phone number.");
        }
        user.getAdminPhones().remove(phoneNumber);
        userRepository.update(user);
        adminPhoneRepository.removePhoneFromAdmin(phoneNumber);
    }

}
