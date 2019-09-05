package com.machava.demo.managers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.machava.demo.controllers.FbApi;
import com.machava.demo.dtos.ApiTemplateDto;
import com.machava.demo.entities.Photo;
import com.machava.demo.entities.User;
import com.machava.demo.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RestApiManager {

    @Autowired
    UserRepository userRepository;
    @Autowired
    FbApi fbApi;

    private static void checkUserAgainstToken(Long userId, Long tokenUserId) {
        if (!tokenUserId.equals(userId)) {
            throw new IllegalArgumentException("Given token does not belong to given FB ID" +
                    "\nUser's Id of Token: " + userId +
                    "\nGiven User's Id: " + tokenUserId);
        }
    }

    private List<Photo> getPhotos(String token, User user) throws Exception {
        List<Photo> photoList = fbApi.getUserPhotos(token);
        photoList.forEach(photo -> photo.setUser(user));

        return photoList;
    }

    public User createUserEntity(ApiTemplateDto apiTemplateDto) throws Exception {
        Long givenId = apiTemplateDto.getId();
        String givenAccessToken = apiTemplateDto.getAccessToken();

        User user = fbApi.getUserDetails(givenAccessToken);

        checkUserAgainstToken(user.getId(), givenId);

        // now let's get photos
        user.setPhotos(getPhotos(givenAccessToken, user));

        return user;
    }

}
