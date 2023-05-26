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
//    @PostMapping(value ="/debit", produces = "application/json")
//    public ResponseEntity<?> debit(@RequestBody AdminKeyDataDTO<UserAndDataDTO<Integer>> adminKeyDataDTO) {
//        adminKeyDataDTO.isAdminKeyValid();
//        User user = null;
//        System.out.println("debit");
//        System.out.println(adminKeyDataDTO);
//        return new ResponseEntity<>(user, HttpStatus.OK);
//    }
//    @PostMapping(value ="/credit", produces = "application/json")
//    public ResponseEntity<?> credit(@RequestBody AdminKeyDataDTO<UserAndDataDTO<Integer>> adminKeyDataDTO) {
//        adminKeyDataDTO.isAdminKeyValid();
//        User user = null;
//        System.out.println("credit");
//        System.out.println(adminKeyDataDTO);
//        Utils.requestService(EServices.USER_SERVICE, "debit", adminKeyDataDTO.getData(), UserDTO.class);
//        return new ResponseEntity<>(user, HttpStatus.OK);
//    }

    @PostMapping(value = "/createUser", produces = "application/json")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserRegisterDTO userRegisterDTO) {
        User user = userService.createUser(userRegisterDTO);
        if (user == null) return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        Utils.requestService(EServices.CARD_SERVICE, "newUserSet/"+userDTO.getUUID().toString(), null, UserDTO.class);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    @GetMapping(value = "/credit/{uuid}/{amount}", produces = "application/json")
    public ResponseEntity<UserDTO> credit(@PathVariable("uuid") String uuid, @PathVariable("amount") Float amount) {
        if (uuid == null) return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        User user = userService.credit(UUID.fromString(uuid), amount);
        if (user == null) return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }
    @GetMapping(value = "/debit/{uuid}/{amount}", produces = "application/json")
    public ResponseEntity<UserDTO> debit(@PathVariable("uuid") String uuid, @PathVariable("amount") Float amount) {
        if (uuid == null) return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        boolean debit = userService.debit(UUID.fromString(uuid), amount);
        User user = userService.getUser(UUID.fromString(uuid));
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        if (!debit) return new ResponseEntity<>(userDTO,HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }



    @GetMapping(value = "/getUser/{uuid}")
    public ResponseEntity<?> getUser(@PathVariable("uuid") String uuid) {
        if (uuid == null) return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        User user = userService.getUser(UUID.fromString(uuid));
        if (user == null) return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
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