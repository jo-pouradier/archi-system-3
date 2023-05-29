package com.sp.controller;

import fr.dtos.common.user.LoginFormDTO;
import fr.dtos.common.user.UserDTO;
import fr.dtos.common.user.UserRegisterDTO;
import com.sp.service.AuthService;
import fr.dtos.common.auth.AuthType;
import fr.dtos.common.utils.EServices;
import fr.dtos.common.utils.Utils;
import org.apache.tomcat.jni.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RestAuthCtr {

    @Autowired
    private AuthService authService;

    @GetMapping(value = "/")
    public HttpStatus getStatus() {
        return HttpStatus.OK;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginFormDTO data) {
        System.out.println(data);
        UserDTO user = authService.login(data.getEmail(), data.getPassword());
        if (user == null) return new ResponseEntity<>(null,HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public UserDTO register(@RequestBody UserRegisterDTO data) {
        UserDTO userDTO = authService.register(data.getName(), data.getPassword(), data.getEmail()); // on renvoie l'uuid ou null;
        //System.out.println(userDTO);
        // create user in UserService
        userDTO = Utils.requestService(EServices.USER_SERVICE, "getUser/"+userDTO.getUUID().toString(), userDTO, UserDTO.class, HttpMethod.GET);
        if (userDTO == null) return null;
        // request card services to create a card set
        String response = Utils.requestService(EServices.CARD_SERVICE, "newUserSet/" + userDTO.getUUID(), null, String.class, HttpMethod.GET);
        return userDTO;
    }

    @GetMapping(value = "/isUser/{uuid}")
    public ResponseEntity<?> isUser(@PathVariable("uuid") String uuid) {
        if (uuid == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        UserDTO userDTO = Utils.getUser(uuid);
        if (userDTO == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/getAuthType/{uuid}")
    public ResponseEntity<AuthType> getAuthType(@PathVariable("uuid") String uuid) {
        if (uuid == null) return new ResponseEntity<>(AuthType.NO_AUTH, HttpStatus.BAD_REQUEST);
        AuthType authType = authService.getAuthType(uuid);
        if (authType == AuthType.NO_AUTH) return new ResponseEntity<>(AuthType.NO_AUTH, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(authType, HttpStatus.OK);
    }
}

