package com.telerikacademy.web.cryptoforum.AdminPhoneServiceImplTests;

import com.telerikacademy.web.cryptoforum.HelperClass;
import com.telerikacademy.web.cryptoforum.exceptions.DuplicateEntityException;
import com.telerikacademy.web.cryptoforum.exceptions.EntityNotFoundException;
import com.telerikacademy.web.cryptoforum.models.AdminPhone;
import com.telerikacademy.web.cryptoforum.models.Tag;
import com.telerikacademy.web.cryptoforum.models.User;
import com.telerikacademy.web.cryptoforum.repositories.AdminPhoneRepositoryImpl;
import com.telerikacademy.web.cryptoforum.repositories.contracts.AdminPhoneRepository;
import com.telerikacademy.web.cryptoforum.repositories.contracts.UserRepository;
import com.telerikacademy.web.cryptoforum.services.AdminPhoneServiceImpl;
import com.telerikacademy.web.cryptoforum.services.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminPhoneServiceImplTests {


    @Mock
    AdminPhoneRepository mockRepository;

    @InjectMocks
    AdminPhoneServiceImpl phoneService;

    @Test
    public void add_Should_ThrowException_When_SamePhoneNumberExist() {
        User user = HelperClass.createMockUserAdmin();
        AdminPhone adminPhone = HelperClass.createAdminPhone();

        adminPhone.setUser(user);

        when(mockRepository.getPhoneNumber(adminPhone.getPhoneNumber())).thenReturn(adminPhone);

        Assertions.assertThrows(DuplicateEntityException.class,
                () -> phoneService.addPhoneNumber(adminPhone, user));
    }

    @Test
    public void add_Should_Pass_When_PhoneNumber_NotFound(){
        User user = HelperClass.createMockUserAdmin();

        AdminPhone adminPhone = HelperClass.createAdminPhone();

        when(mockRepository.getPhoneNumber(adminPhone.getPhoneNumber())).thenThrow(EntityNotFoundException.class);

        phoneService.addPhoneNumber(adminPhone, user);

        verify(mockRepository, Mockito.times(1)).addPhoneNumber(adminPhone);
    }

}
