package com.sp.controller;

import com.sp.model.User;
import com.sp.service.UserService;
import fr.dtos.common.user.UserAndDataDTO;
import fr.dtos.common.user.UserDTO;
import fr.dtos.common.user.UserRegisterDTO;
import fr.dtos.common.utils.AdminKeyDataDTO;
import fr.dtos.common.utils.EServices;
import fr.dtos.common.utils.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class RestUserCtr {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/createUser", produces = "application/json")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserRegisterDTO userRegisterDTO) {
        User user = userService.createUser(userRegisterDTO);
        userService.credit(user.getUUID(),5000);

        if (user == null) return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        System.out.println("Create user: "+userDTO);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    @GetMapping(value = "/depot", produces = "application/json")
    public ResponseEntity<UserDTO> credit(@RequestParam("uuid") String uuid, @RequestParam("amount") Float amount) {
        if (uuid == null) return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        User user = userService.credit(UUID.fromString(uuid), amount);
        if (user == null) return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        System.out.println("Credit user: "+userDTO + " amount: "+amount);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }
    @GetMapping(value = "/debit", produces = "application/json")
    public ResponseEntity<UserDTO> debit(@RequestParam("uuid") String uuid, @RequestParam("amount") Float amount) {
        if (uuid == null) return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        boolean debit = userService.debit(UUID.fromString(uuid), amount);
        User user = userService.getUser(UUID.fromString(uuid));
        if (user == null) return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        if (!debit) return new ResponseEntity<>(userDTO,HttpStatus.FORBIDDEN);
        System.out.println("Debit user: "+userDTO + " amount: "+amount);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }



    @GetMapping(value = "/getUser/{uuid}")
    public ResponseEntity<?> getUser(@PathVariable("uuid") String uuid) {
        if (uuid == null) return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        User user = userService.getUser(UUID.fromString(uuid));
        if (user == null) return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        System.out.println("Get user: " + userDTO);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }
    @GetMapping(value = "/getUserByEmail/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable("email") String email) {
        if (email == null) return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        User user = userService.getUserByEmail(email);
        if (user == null) return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }
}