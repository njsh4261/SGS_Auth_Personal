package com.personalproject.integrated.logic.role;

import com.personalproject.integrated.exception.CannotDeleteSelfException;
import com.personalproject.integrated.exception.NoPermissionException;
import com.personalproject.integrated.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class UserRole {
    @Autowired
    private AuthRepository authRepository;

    private enum UserRoleType {
        UNAUTHORIZED("unauthorized"),
        NORMAL_USER("normal_user"),
        ADMIN("admin");

        private final String value;
        UserRoleType(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }

    private UserRoleType userRoleType;
    private long userId;

    public UserRole() {
        this.userRoleType = UserRoleType.UNAUTHORIZED;
        this.userId = -1;
    }

    private void setRole(String role) {
        userRoleType = UserRoleType.valueOf(role.toUpperCase(Locale.ROOT));
    }

    private void setUserId(long userId) {
        this.userId = userId;
    }

    private void setRoleToUnauthorized() {
        userRoleType = UserRoleType.UNAUTHORIZED;
    }

    public long getUserId() {
        return userId;
    }

    public boolean isCurrentUserAuthorized() {
        return userRoleType != UserRoleType.UNAUTHORIZED;
    }

    public void updateCurrentUserRole(long id) {
        authRepository.findById(id).ifPresentOrElse(
                user -> { setRole(user.getRole()); },
                () -> { setRoleToUnauthorized(); }
        );
        setUserId(id);
    }

    public String getRole() {
        return userRoleType.toString();
    }

    public void checkCurrentUserNotAdmin() {
        if(userRoleType != UserRoleType.ADMIN) {
            throw new NoPermissionException();
        }
    }

    public void checkDeletingSelf(long id) {
        if(getUserId() == id) {
            throw new CannotDeleteSelfException();
        }
    }
}
