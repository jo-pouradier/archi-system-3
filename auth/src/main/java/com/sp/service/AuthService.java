package com.sp.service;

import fr.dtos.common.auth.AuthType;
import fr.dtos.common.user.UserDTO;
import fr.dtos.common.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
public class AuthService {

    public boolean logUser(String cookie) {
        return true;
    }

    public UserDTO login(String email, String password) {
        UserDTO user = Utils.getUserByEmail(email);
        if (isNull(user)) return null;
        return user.getPassword().equals(password) ? user : null;
    }

    public UserDTO register(String username, String password, String email) {
        return Utils.createUser(new UserDTO(username, password, email));
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
