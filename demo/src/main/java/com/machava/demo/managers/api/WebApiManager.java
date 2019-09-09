package com.machava.demo.managers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.machava.demo.controllers.FbApi;
import com.machava.demo.entities.User;
import com.machava.demo.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WebApiManager {

    @Autowired
    UserRepository userRepository;
    @Autowired
    FbApi fbApi;

    public User createSaveAndReturnUserEntity(String givenAccessToken) throws Exception {

        User user = fbApi.getUserDetails(givenAccessToken);

        // store new user
        if (!userRepository.existsById(user.getId())) {
            user.setPhotos(fbApi.getPhotos(givenAccessToken, user));
            userRepository.save(user);
        }

        return userRepository.findFirstById(user.getId());
    }

}
