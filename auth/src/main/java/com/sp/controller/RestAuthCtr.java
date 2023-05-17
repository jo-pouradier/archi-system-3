package com.sp.controller;

import com.sp.model.User;
import com.sp.model.UserDTO;
import com.sp.model.UserRegisterDTO;
import com.sp.model.loginFormDTO;
import com.sp.service.AuthService;
import com.sp.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins={"http://localhost:8080", "http://localhost:8081", "http://localhost:8082", "http://localhost:8083", "http://localhost:8084", "http://localhost:8085"}, allowedHeaders = "*")
@RequestMapping("/auth")
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
    public ResponseEntity<UserDTO> login(@RequestBody loginFormDTO data) {
        System.out.println(data);
        User user = authService.login(data.getEmail(), data.getPassword());
        if (user == null) return new ResponseEntity<>(null,HttpStatus.FORBIDDEN);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public UserDTO register(@RequestBody UserRegisterDTO data) {
        User user = authService.register(data.getUsername(),data.getPassword(), data.getEmail()); // on renvoie l'uuid ou null;
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
}

