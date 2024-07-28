package com.telerikacademy.web.cryptoforum.UserServiceImplTests;

import com.telerikacademy.web.cryptoforum.HelperClass;
import com.telerikacademy.web.cryptoforum.exceptions.*;
import com.telerikacademy.web.cryptoforum.models.FilteredUserOptions;
import com.telerikacademy.web.cryptoforum.models.Position;
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
    public void get_Should_Return_User_WhenProperIdIsProvided(){
        User testUser = HelperClass.createMockUserAdmin();

        Mockito.when(mockRepository.getById(2)).thenReturn(testUser);

        assertEquals(userService.getById(testUser, 2), testUser);
    }


    @Test
    public void get_Should_Return_User_WhenProperUsernameIsProvided(){
        User testUser = HelperClass.createMockUserAdmin();

        Mockito.when(mockRepository.getByUsername("Gotogoto")).thenReturn(testUser);

        assertEquals(userService.getByUsername("Gotogoto"), testUser);
    }

    @Test
    public void get_Should_Return_User_WhenProperEmailIsProvided(){
        User testUser = HelperClass.createMockUserAdmin();
        User user = HelperClass.createMockUserUser();

        Mockito.when(mockRepository.getByEmail("ppp.jjj@example.com")).thenReturn(user);

        assertEquals(userService.getByEmail("ppp.jjj@example.com", testUser), user);
    }

    @Test
    public void get_Should_Return_User_WhenProperFirstNameIsProvided(){
        User testUser = HelperClass.createMockUserAdmin();
        User user = HelperClass.createMockUserUser();

        Mockito.when(mockRepository.getByFirstName("Peter")).thenReturn(user);

        assertEquals(userService.getByFirstName("Peter", testUser), user);
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
    public void create_Should_Throw_When_User_NotFound(){

        User newUser = HelperClass.createMockUserUser();
        newUser.setUsername("uniqueUsername");
        newUser.setEmail("uniqueEmail@example.com");

        Mockito.when(mockRepository.getByUsername(newUser.getUsername())).thenThrow(EntityNotFoundException.class);

        userService.createUser(newUser);

        Mockito.verify(mockRepository, Mockito.times(1)).createUser(newUser);
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


    @Test
    public void updateToModerator_Should_Throw_When_UserIsNotAdmin(){

        User existingUser = HelperClass.createMockUserUser();
        User userToModerate = HelperClass.createMockUserUser();
        userToModerate.setId(100);
        userToModerate.setUsername("jhgfdsdfghj");

        assertThrows(UnauthorizedOperationException.class, () -> userService.userToBeModerator(existingUser, userToModerate));

    }

    @Test
    public void updateToModerator_Should_Throw_When_UserIsAlreadyModerator(){

        User existingUser = HelperClass.createMockUserAdmin();

        User userToBeModerator = HelperClass.createMockUserUser();
        userToBeModerator.setId(100);
        userToBeModerator.setUsername("testUser");

         userToBeModerator.setPosition(new Position(2, "moderator"));

        Mockito.when(mockRepository.getByUsername("testUser")).thenReturn(userToBeModerator);

        // Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> userService.userToBeModerator(existingUser, userToBeModerator));

    }

    @Test
    public void updateToModerator_Should_Throw_When_UserIsAdmin(){

        User existingUser = HelperClass.createMockUserAdmin();

        User userToBeModerator = HelperClass.createMockUserAdmin();
        userToBeModerator.setId(100);
        userToBeModerator.setUsername("testUser");


        Mockito.when(mockRepository.getByUsername("testUser")).thenReturn(userToBeModerator);

        // Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> userService.userToBeModerator(existingUser, userToBeModerator));

    }


    @Test
    public void update_Should_Throw_When_UserDoesNot_Exist(){
        var mocksUser = HelperClass.createMockUserUser();
        var otherUser = HelperClass.createMockUserAdmin();


        Mockito.when(mockRepository.getByUsername(Mockito.anyString())).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.userToBeModerator(otherUser, mocksUser));

    }

    @Test
    public void updateTo_Moderator_Position_When_UserIsNotModerator(){
        User existingUser = HelperClass.createMockUserAdmin();

        User userToBeModerator = HelperClass.createMockUserUser();
        userToBeModerator.setId(100);
        userToBeModerator.setUsername("testUser");

        Mockito.when(mockRepository.getByUsername("testUser")).thenReturn(userToBeModerator);

        userService.userToBeModerator(existingUser, userToBeModerator);

        Assertions.assertEquals("moderator", userToBeModerator.getPosition().getName());
        Assertions.assertEquals(2, userToBeModerator.getPosition().getId());
        Mockito.verify(mockRepository).updateTo(userToBeModerator);
    }



    @Test
    public void updateFromModerator_Should_Throw_When_UserIsNotAdmin(){

        User existingUser = HelperClass.createMockUserUser();
        User userToModerate = HelperClass.createMockUserUser();
        userToModerate.setId(100);
        userToModerate.setUsername("jhgfdsdfghj");
        userToModerate.setPosition(new Position(2, "moderator"));

        assertThrows(UnauthorizedOperationException.class, () -> userService.userToBeNotModerator(existingUser, userToModerate));

    }



    @Test
    public void updateFromModerator_Should_Throw_When_UserIsUser(){

        User existingUser = HelperClass.createMockUserAdmin();

        User userToBeModerator = HelperClass.createMockUserUser();
        userToBeModerator.setId(100);
        userToBeModerator.setUsername("testUser");

        Mockito.when(mockRepository.getByUsername("testUser")).thenReturn(userToBeModerator);

        Assertions.assertThrows(UnsupportedOperationException.class, () -> userService.userToBeNotModerator(existingUser, userToBeModerator));

    }

    @Test
    public void updateFromModerator_Should_Throw_When_UserIsAdmin(){

        User existingUser = HelperClass.createMockUserAdmin();

        User userToBeModerator = HelperClass.createMockUserAdmin();
        userToBeModerator.setId(100);
        userToBeModerator.setUsername("testUser");


        Mockito.when(mockRepository.getByUsername("testUser")).thenReturn(userToBeModerator);

        // Act & Assert
        Assertions.assertThrows(UnsupportedOperationException.class, () -> userService.userToBeNotModerator(existingUser, userToBeModerator));

    }


    @Test
    public void updateFrom_Should_Throw_When_UserDoesNot_Exist(){
        var mocksUser = HelperClass.createMockUserUser();
        var otherUser = HelperClass.createMockUserAdmin();


        Mockito.when(mockRepository.getByUsername(Mockito.anyString())).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.userToBeNotModerator(otherUser, mocksUser));

    }



    @Test
    public void updateFrom_Moderator_Position_When_UserIsModerator(){
        User existingUser = HelperClass.createMockUserAdmin();

        User userToBeModerator = HelperClass.createMockUserUser();
        userToBeModerator.setId(100);
        userToBeModerator.setUsername("testUser");
        userToBeModerator.setPosition(new Position(2, "moderator"));

        Mockito.when(mockRepository.getByUsername("testUser")).thenReturn(userToBeModerator);

        userService.userToBeNotModerator(existingUser, userToBeModerator);

        Assertions.assertEquals("user", userToBeModerator.getPosition().getName());
        Assertions.assertEquals(3, userToBeModerator.getPosition().getId());
        Mockito.verify(mockRepository).updateTo(userToBeModerator);
    }



    @Test
    public void block_Should_Throw_When_UserIsNotAdminOrModerator(){

        User existingUser = HelperClass.createMockUserUser();
        User userToModerate = HelperClass.createMockUserUser();
        userToModerate.setId(100);
        userToModerate.setUsername("jhgfdsdfghj");

        assertThrows(UnauthorizedOperationException.class, () -> userService.blockUser(existingUser, userToModerate.getId()));

    }


    @Test
    public void block_Should_Throw_When_UserDoesNot_Exist(){
        var mocksUser = HelperClass.createMockUserUser();
        var otherUser = HelperClass.createMockUserAdmin();


        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.blockUser(otherUser, mocksUser.getId()));

    }

    @Test
    public void block_user_When_UserIsNotBlocked(){
        User existingUser = HelperClass.createMockUserAdmin();

        User userToBeModerator = HelperClass.createMockUserUser();
        userToBeModerator.setId(100);
        userToBeModerator.setUsername("testUser");
       userToBeModerator.setBlocked(false);

        Mockito.when(mockRepository.getById(100)).thenReturn(userToBeModerator);

        userService.blockUser(existingUser, userToBeModerator.getId());

        Assertions.assertTrue(userToBeModerator.isBlocked());
        Mockito.verify(mockRepository).block(userToBeModerator);
    }

    @Test
    public void block_Throw_When_UserIsNotBlocked(){
        User existingUser = HelperClass.createMockUserAdmin();

        User userToBeModerator = HelperClass.createMockUserUser();
        userToBeModerator.setId(100);
        userToBeModerator.setUsername("testUser");
        userToBeModerator.setBlocked(true);

        Mockito.when(mockRepository.getById(100)).thenReturn(userToBeModerator);

        Assertions.assertThrows(BlockedException.class, () -> userService.blockUser(existingUser, userToBeModerator.getId()));
    }


    @Test
    public void unblock_Should_Throw_When_UserIsNotAdminOrModerator(){

        User existingUser = HelperClass.createMockUserUser();
        User userToModerate = HelperClass.createMockUserUser();
        userToModerate.setId(100);
        userToModerate.setUsername("jhgfdsdfghj");
        userToModerate.setBlocked(true);

        assertThrows(UnauthorizedOperationException.class, () -> userService.unblockUser(existingUser, userToModerate.getId()));

    }

    @Test
    public void unblock_Should_Throw_When_UserDoesNot_Exist(){
        var mocksUser = HelperClass.createMockUserUser();
        var otherUser = HelperClass.createMockUserAdmin();


        Mockito.when(mockRepository.getById(Mockito.anyInt())).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.unblockUser(otherUser, mocksUser.getId()));
    }


    @Test
    public void unblock_user_When_UserIsNotBlocked(){
        User existingUser = HelperClass.createMockUserAdmin();

        User userToBeModerator = HelperClass.createMockUserUser();
        userToBeModerator.setId(100);
        userToBeModerator.setUsername("testUser");
        userToBeModerator.setBlocked(true);

        Mockito.when(mockRepository.getById(100)).thenReturn(userToBeModerator);

        userService.unblockUser(existingUser, userToBeModerator.getId());

        Assertions.assertFalse(userToBeModerator.isBlocked());
        Mockito.verify(mockRepository).unblock(userToBeModerator);
    }

    @Test
    public void unblock_Throw_When_UserIsNotBlocked(){
        User existingUser = HelperClass.createMockUserAdmin();

        User userToBeModerator = HelperClass.createMockUserUser();
        userToBeModerator.setId(100);
        userToBeModerator.setUsername("testUser");
        userToBeModerator.setBlocked(false);

        Mockito.when(mockRepository.getById(100)).thenReturn(userToBeModerator);

        Assertions.assertThrows(BlockedException.class, () -> userService.unblockUser(existingUser, userToBeModerator.getId()));
    }

}
