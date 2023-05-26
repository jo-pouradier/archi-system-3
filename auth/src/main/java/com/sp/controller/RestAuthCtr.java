package com.sp.controller;

import com.sp.model.User;
import fr.dtos.common.user.UserDTO;
import fr.dtos.common.user.UserRegisterDTO;
import fr.dtos.common.user.LoginFormDTO;
import com.sp.service.AuthService;
import com.sp.service.UserService;
import fr.dtos.common.auth.AuthType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RestAuthCtr {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @GetMapping(value = "/")
    public HttpStatus getStatus() {
        return HttpStatus.OK;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginFormDTO data) {
        System.out.println(data);
        User user = authService.login(data.getEmail(), data.getPassword());
        if (user == null) return new ResponseEntity<>(null,HttpStatus.FORBIDDEN);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public UserDTO register(@RequestBody UserRegisterDTO data) {
        User user = authService.register(data.getName(),data.getPassword(), data.getEmail()); // on renvoie l'uuid ou null;
        System.out.println(user);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    @GetMapping(value = "/isUser/{uuid}")
    public ResponseEntity<?> isUser(@PathVariable("uuid") String uuid) {
        if (uuid == null) return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        User user = userService.getUser(uuid);
        if (user == null) return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    @GetMapping(value = "/getAuthType/{uuid}")
    public ResponseEntity<AuthType> getAuthType(@PathVariable("uuid") String uuid) {
        if (uuid == null) return new ResponseEntity<>(AuthType.NO_AUTH,HttpStatus.BAD_REQUEST);
        AuthType authType = authService.getAuthType(uuid);
        if (authType == AuthType.NO_AUTH) return new ResponseEntity<>(AuthType.NO_AUTH,HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(authType,HttpStatus.OK);
    }
}

