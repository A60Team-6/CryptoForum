package com.telerikacademy.web.cryptoforum.helpers;

import com.telerikacademy.web.cryptoforum.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.cryptoforum.models.User;

public class PermissionHelper {

    public static final String POSITION_ADMIN = "admin";

    private static final String POSITION_MODERATOR = "moderator";

    public static void isAdmin(User authenticatedUser, String message) {
        boolean isAdmin = false;
        if (authenticatedUser.getPosition().getName().equals(POSITION_ADMIN)) {
            isAdmin = true;
        }

        if (!isAdmin) {
            throw new UnauthorizedOperationException(message);
        }
    }

    public static void isAdminOrSameUser(User authenticatedUser, User user, String message) {
        boolean isAuthorized = false;
        if (authenticatedUser.getPosition().getName().equals(POSITION_ADMIN) || authenticatedUser.equals(user)) {
            isAuthorized = true;
        }

        if (!isAuthorized) {
            throw new UnauthorizedOperationException(message);
        }
    }

    public static void isSameUser(User user, User postCreator, String message) {
        if (!user.equals(postCreator)) {
            throw new UnauthorizedOperationException(message);
        }
    }

    public static void isNotSameUser(User userForUpdate, User authorizedUser, String message) {
        if (userForUpdate.equals(authorizedUser)) {
            throw new UnauthorizedOperationException(message);
        }
    }

    public static void isBlocked(User authorizedUser, String message) {
        if (authorizedUser.isBlocked()) {
            throw new UnauthorizedOperationException(message);
        }
    }

    public static void isModerator(User authenticatedUser, String message) {
        boolean isModerator = false;

        if (authenticatedUser.getPosition().getName().equals(POSITION_MODERATOR)) {
            isModerator = true;
        }

        if (!isModerator) {
            throw new UnauthorizedOperationException(message);
        }
    }

    public static void isAdminOrModerator(User authenticatedUser, String message) {
        boolean isModeratorOrAdmin = false;

        if (authenticatedUser.getPosition().getName().equals(POSITION_MODERATOR) || authenticatedUser.getPosition().getName().equals(POSITION_ADMIN)) {
            isModeratorOrAdmin = true;
        }

        if (!isModeratorOrAdmin) {
            throw new UnauthorizedOperationException(message);
        }
    }

    public static void isAdminOrModeratorOrPostCreator(User authenticatedUser, User postCreator, String message) {
        boolean isAdminOrModeratorOrPostCreator = false;

        if(authenticatedUser.getPosition().getName().equals(POSITION_ADMIN) ||
                authenticatedUser.getPosition().getName().equals(POSITION_MODERATOR) ||
                authenticatedUser.equals(postCreator)) {
            isAdminOrModeratorOrPostCreator = true;
        }

        if (!isAdminOrModeratorOrPostCreator) {
            throw new UnauthorizedOperationException(message);
        }
    }

    public static void isAdminOrModeratorOrPostOrCommentCreator(User authenticatedUser, User postCreator, User commentCreator, String message) {
        boolean isAdminOrModeratorOrPostOrCommentCreator = false;

        if(authenticatedUser.getPosition().getName().equals(POSITION_ADMIN) ||
                authenticatedUser.getPosition().getName().equals(POSITION_MODERATOR) ||
                authenticatedUser.equals(postCreator) ||
                authenticatedUser.equals(commentCreator)) {
            isAdminOrModeratorOrPostOrCommentCreator = true;
        }

        if (!isAdminOrModeratorOrPostOrCommentCreator) {
            throw new UnauthorizedOperationException(message);
        }
    }
}
