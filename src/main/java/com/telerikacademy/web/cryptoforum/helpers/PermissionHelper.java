package com.telerikacademy.web.cryptoforum.helpers;

import com.telerikacademy.web.cryptoforum.exceptions.AuthorizationException;
import com.telerikacademy.web.cryptoforum.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.cryptoforum.models.User;

public class PermissionHelper {

    public static final String ROLE_ADMIN = "admin";
    private static final String ROLE_MODERATOR = "moderator";

    public static void isAdmin(User authenticatedUser, String message){
        boolean isAdmin = false;
        if(authenticatedUser.getPosition().getName().equals(ROLE_ADMIN)){
            isAdmin = true;
        }

        if(!isAdmin){
            throw new UnauthorizedOperationException(message);
        }
    }

    public static void isAdminOrSameUser(User authenticatedUser, User user, String message){
        boolean isAuthorized = false;
        if(authenticatedUser.getPosition().getName().equals(ROLE_ADMIN) || authenticatedUser.equals(user) || authenticatedUser.getPosition().getName().equals(ROLE_MODERATOR)){
            isAuthorized = true;
        }

        if(!isAuthorized){
            throw new UnauthorizedOperationException(message);
        }
    }

    public static void isSameUser(User user, User postCreator, String message){
        if(!user.equals(postCreator)){
            throw new UnauthorizedOperationException(message);
        }
    }

    public static void isNotSameUser(User userForUpdate, User authorizedUser, String message){
        if(userForUpdate.equals(authorizedUser)){
            throw new UnauthorizedOperationException(message);
        }
    }

    public static void isBlocked(User authorizedUser, String message){
        if(authorizedUser.isBlocked()){
            throw new UnauthorizedOperationException(message);
        }
    }

    public static void isModerator(User authenticatedUser, String message){
        boolean isModerator = false;

        if(authenticatedUser.getPosition().getName().equals(ROLE_MODERATOR)){
            isModerator = true;
        }

        if(!isModerator){
            throw new UnauthorizedOperationException(message);
        }
    }
}
