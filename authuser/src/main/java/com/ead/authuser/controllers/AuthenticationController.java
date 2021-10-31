package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDTO;
import com.ead.authuser.emuns.UserStatus;
import com.ead.authuser.emuns.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody
                                                  @Validated(UserDTO.UserView.RegistrationPost.class) @JsonView(UserDTO.UserView.RegistrationPost.class) UserDTO userDTO){

        if(userService.existsByUsername(userDTO.getUsername())){
            return  ResponseEntity.status(HttpStatus.CONFLICT).body("Username already used");
        }

        if(userService.existsByEmail(userDTO.getEmail())){
            return  ResponseEntity.status(HttpStatus.CONFLICT).body("Email already used");
        }

        var userModel = new UserModel();
        BeanUtils.copyProperties(userDTO,userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
        userModel.setCreationDate(now);
        userModel.setLastUpdateDate(now);
        userService.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }

}
