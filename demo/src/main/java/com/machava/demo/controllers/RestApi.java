package com.machava.demo.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.machava.demo.dtos.ApiTemplateDto;
import com.machava.demo.dtos.PhotoDto;
import com.machava.demo.entities.Photo;
import com.machava.demo.entities.User;
import com.machava.demo.managers.api.RestApiManager;
import com.machava.demo.repository.PhotoRepository;
import com.machava.demo.repository.UserRepository;

@RestController
@RequestMapping("/api")
public class RestApi {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private RestApiManager restApiManager;

    @PostMapping(value = "users")
    public ResponseEntity<?> storeUserAndPhotos(@RequestBody ApiTemplateDto apiTemplateDto) throws Exception {

        if (apiTemplateDto == null) {
            throw new IllegalArgumentException("Invalid request body!");
        }

        User user = restApiManager.createUserEntity(apiTemplateDto);
        boolean isNew = !userRepository.existsById(apiTemplateDto.getId());

        userRepository.save(user);

        if (isNew) {
            return new ResponseEntity<>("User and Photos has been stored in Database", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User and Photos has been updated", HttpStatus.OK);
        }
    }

    @GetMapping(value = "users/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        User user = userRepository.findFirstById(id);

        if (user != null) {
            return new ResponseEntity<>(user.toDto(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User with given id (" + id + ") is not stored in DB.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "users/{id}/photos")
    public ResponseEntity<?> getUserPhotos(@PathVariable Long id) {
        User user = userRepository.findFirstById(id);
        List<Photo> photoList = photoRepository.findAllByUser(user);

        if (photoList == null) {
            return new ResponseEntity<>("User with given id (" + id + ") is not stored in DB.", HttpStatus.NOT_FOUND);
        } else if (photoList.isEmpty()) {
            return new ResponseEntity<>("No photos are stored for FB user with given id (" + id + ").", HttpStatus.NOT_FOUND);
        } else {
            List<PhotoDto> photoDtoList = photoList.stream().map(photo -> photo.toDto()).collect(Collectors.toList());
            return new ResponseEntity<>(photoDtoList, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return new ResponseEntity<>("User with FB ID " + id + " has been deleted.", HttpStatus.OK);
    }
}
