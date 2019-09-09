package com.machava.demo.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.machava.demo.dtos.PhotoDto;
import com.machava.demo.dtos.UserDto;
import com.machava.demo.entities.Photo;
import com.machava.demo.managers.api.WebApiManager;
import com.machava.demo.repository.PhotoRepository;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class Web {

    @Autowired
    private FbApi fbApi;
    @Autowired
    private WebApiManager webApiManager;
    @Autowired
    private PhotoRepository photoRepository;

    @GetMapping("/photos")
    public String showClientPhotos(@RequestParam(name="fbToken", required = false) String fbToken, Model model) throws Exception {

        String tokenError = fbApi.verifyToken(fbToken);

        if (tokenError != null) {
            model.addAttribute("tokenError", tokenError);

            return "error-page";
        } else {
            UserDto userDto = webApiManager.createSaveAndReturnUserEntity(fbToken).toDto();
            model.addAttribute("user", userDto);
            List<Photo> photoList = photoRepository.findAllByUserId(userDto.getId());
            List<PhotoDto> photoDtoList = photoList.stream().map(photo -> photo.toDto()).collect(Collectors.toList());
            model.addAttribute("photos", photoDtoList);

            return "photos";
        }
    }

}
