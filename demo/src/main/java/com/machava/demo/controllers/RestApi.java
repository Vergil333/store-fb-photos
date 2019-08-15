package com.machava.demo.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.machava.demo.dtos.UserDto;
import com.machava.demo.dtos.UserTemplateDto;
import com.machava.demo.entities.User;
import com.machava.demo.repository.UserRepository;

@RestController
@RequestMapping("/api")
public class RestApi {

    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "backup-photos")
    public User storeUserPhotos(@RequestBody UserTemplateDto userTemplateDto) throws IOException {

        if (userTemplateDto == null) {
            throw new IllegalArgumentException("Invalid request body!");
        }

        UserDto userDto = null;
        String givenFbId = userTemplateDto.getFbId();
        String givenAccessToken = userTemplateDto.getAccessToken();

        Boolean tokenIsOk = FbApi.verifyToken(givenAccessToken);

        if (tokenIsOk) {
            userDto = FbApi.getUserInfo(givenAccessToken);
        } else {
            throw new IllegalArgumentException("Token is invalid.");
        }

        if (!userDto.getFbId().equals(givenFbId)) {
            throw new IllegalArgumentException("Given token does not belong to given FB ID");
        } else if (!userRepository.existsByFbId(givenFbId)) {
            return userRepository.save(userDto.toEntity());
        } else {
            throw new IllegalArgumentException("Token does not match the user's FB ID.");
        }
    }
}
