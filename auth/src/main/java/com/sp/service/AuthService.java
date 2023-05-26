package com.sp.service;

import com.sp.model.User;
import fr.dtos.common.auth.AuthType;
import fr.dtos.common.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.Objects.isNull;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    public boolean logUser(String cookie) {
        return true;
    }

    public User login(String email, String password) {
        User user = userService.getUserByEmail(email);
        if (isNull(user)) return null;
        return user.getPassword().equals(password) ? user : null;
    }

    public User register(String username, String password, String email) {
        return userService.addUser(new User(username, password, email));
    }

    public AuthType getAuthType(String uuid) {
        if (Utils.isSuperAdminKey(uuid)) {
            return AuthType.SERVICE;
        } else if (Utils.isUserKey(uuid)) {
            return AuthType.USER;
        }else{
            return AuthType.NO_AUTH;
        }
    }
}
