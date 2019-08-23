package com.machava.demo.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.machava.demo.dtos.ApiTemplateDto;
import com.machava.demo.entities.Photo;
import com.machava.demo.entities.User;
import com.machava.demo.repository.UserRepository;

@RestController
@RequestMapping("/api")
public class RestApi {

    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "backup-photos")
    public User storeUserPhotos(@RequestBody ApiTemplateDto apiTemplateDto) throws IOException {

        if (apiTemplateDto == null) {
            throw new IllegalArgumentException("Invalid request body!");
        }

        User user;
        String givenAccessToken = apiTemplateDto.getAccessToken();

        Boolean tokenIsOk = FbApi.verifyToken(givenAccessToken);

        if (tokenIsOk) {
            user = FbApi.getUserDetails(givenAccessToken);
        } else {
            throw new IllegalArgumentException("Token is invalid.");
        }

        if (userRepository.existsById(user.getId())) {
            System.out.println("User is already registered in DB. Updating entity.");
        }

        // now let's get photos
        List<Photo> photoList = FbApi.getUserPhotos(givenAccessToken);
        User finalUser = user;
        photoList.forEach(photo -> photo.setUser(finalUser));

        user.setPhotos(photoList);

        userRepository.save(user);
        return null;
    }
}
