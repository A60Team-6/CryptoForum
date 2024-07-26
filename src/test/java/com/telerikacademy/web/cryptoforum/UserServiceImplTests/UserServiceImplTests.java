package com.telerikacademy.web.cryptoforum.UserServiceImplTests;

import com.telerikacademy.web.cryptoforum.HelperClass;
import com.telerikacademy.web.cryptoforum.exceptions.AuthorizationException;
import com.telerikacademy.web.cryptoforum.exceptions.DuplicateEntityException;
import com.telerikacademy.web.cryptoforum.exceptions.EntityNotFoundException;
import com.telerikacademy.web.cryptoforum.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.cryptoforum.models.FilteredUserOptions;
import com.telerikacademy.web.cryptoforum.models.User;
import com.telerikacademy.web.cryptoforum.repositories.contracts.UserRepository;
import com.telerikacademy.web.cryptoforum.services.UserServiceImpl;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @Mock
    UserRepository mockRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    public void get_Should_Return_AllUsers_WhenOptionsAreFulfilled(){
        User testUser = HelperClass.createMockUserAdmin();
        FilteredUserOptions mock = Mockito.mock(FilteredUserOptions.class);

        Mockito.when(mockRepository.getAll(mock)).thenReturn(List.of(testUser));

        assertEquals(1, userService.getAll(mock, testUser).size());
    }

    @Test
    public void get_Should_Return_Beer_WhenProperIdIsProvided(){
        User testUser = HelperClass.createMockUserAdmin();

        Mockito.when(mockRepository.getById(2)).thenReturn(testUser);

        assertEquals(userService.getById(testUser, 2), testUser);
    }

    @Test
    public void create_Should_ThrowException_When_UserWithSameNameExist() {
        User testUser = HelperClass.createMockUserAdmin();

        Mockito.when(mockRepository.getByUsername(testUser.getUsername())).thenReturn(testUser);

        Assertions.assertThrows(DuplicateEntityException.class,
                () -> userService.createUser(testUser));

    }

    @Test
    public void create_Should_ThrowException_When_UserWithSameEmailExist() {
        User testUser = HelperClass.createMockUserAdmin();

        Mockito.when(mockRepository.getByEmail(testUser.getEmail())).thenReturn(testUser);

        assertThrows(DuplicateEntityException.class,
                () -> userService.createUser(testUser));

    }


    @Test
    public void update_Should_Throw_When_UserIsNotCreatorOrAdmin(){

        User existingUser = HelperClass.createMockUserUser();
        User notCreator = HelperClass.createMockUserUser();
        notCreator.setId(100);
        notCreator.setUsername("jhgfdsdfghj");


        assertThrows(UnauthorizedOperationException.class, () -> userService.updateUser(existingUser, notCreator));

    }


    @Test
    public void update_Should_Throw_When_UserEmailIsTaken(){
        var mocksUser = HelperClass.createMockUserUser();
        var otherUser = HelperClass.createMockUserUser();
        otherUser.setId(5);

        Mockito.when(mockRepository.getByEmail(Mockito.anyString())).thenReturn(mocksUser);

        Assertions.assertThrows(DuplicateEntityException.class, () -> userService.updateUser(mocksUser, otherUser));
    }

    @Test
    public void update_Should_Pass_When_EmailIsUnique(){
        var mocksUser = HelperClass.createMockUserUser();


        Mockito.when(mockRepository.getByEmail(Mockito.anyString())).thenReturn(mocksUser);

        userService.updateUser(mocksUser, mocksUser);

        Mockito.verify(mockRepository, Mockito.times(1)).update(Mockito.any(User.class));
    }

    @Test
    public void update_Should_Pass_When_SameUserAnd_Diff_Email(){
        var mocksUser = HelperClass.createMockUserUser();


        Mockito.when(mockRepository.getByEmail(Mockito.anyString())).thenThrow(EntityNotFoundException.class);

        userService.updateUser(mocksUser, mocksUser);

        Mockito.verify(mockRepository, Mockito.times(1)).update(Mockito.any(User.class));
    }
}
