package com.sp.controller;

import com.sp.model.User;
import fr.dtos.common.user.UserAndDataDTO;
import fr.dtos.common.user.UserDTO;
import fr.dtos.common.user.UserRegisterDTO;
import fr.dtos.common.utils.AdminKeyDataDTO;
import fr.dtos.common.utils.EServices;
import fr.dtos.common.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RestUserCtr {
    @PostMapping(value ="/debit", produces = "application/json")
    public ResponseEntity<?> debit(@RequestBody AdminKeyDataDTO<UserAndDataDTO<Integer>> adminKeyDataDTO) {
        adminKeyDataDTO.isAdminKeyValid();
        User user = null;
        System.out.println("debit");
        System.out.println(adminKeyDataDTO);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @PostMapping(value ="/credit", produces = "application/json")
    public ResponseEntity<?> credit(@RequestBody AdminKeyDataDTO<UserAndDataDTO<Integer>> adminKeyDataDTO) {
        adminKeyDataDTO.isAdminKeyValid();
        User user = null;
        System.out.println("credit");
        System.out.println(adminKeyDataDTO);
        Utils.requestService(EServices.USER_SERVICE, "debit", adminKeyDataDTO.getData(), UserDTO.class);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}