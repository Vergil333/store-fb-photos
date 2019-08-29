package com.machava.demo.managers;

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

    public static void checkUserAgainstToken(Long userId, Long tokenUserId) {
        if (!tokenUserId.equals(userId)) {
            throw new IllegalArgumentException("Given token does not belong to given FB ID" +
                    "\nUser's Id of Token: " + userId +
                    "\nGiven User's Id: " + tokenUserId);
        }
    }

    public static List<Photo> getPhotos(String token, User user) throws Exception {
        List<Photo> photoList = FbApi.getUserPhotos(token);
        photoList.forEach(photo -> photo.setUser(user));

        return photoList;
    }

    public static User createUserEntity(ApiTemplateDto apiTemplateDto) throws Exception {
        Long givenId = apiTemplateDto.getId();
        String givenAccessToken = apiTemplateDto.getAccessToken();

        User user = FbApi.getUserDetails(givenAccessToken);

        RestApiManager.checkUserAgainstToken(user.getId(), givenId);

        // now let's get photos
        user.setPhotos(RestApiManager.getPhotos(givenAccessToken, user));

        return user;
    }

}
