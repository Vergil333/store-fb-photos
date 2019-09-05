package com.machava.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class Web {

    @Autowired
    FbApi fbApi;

    @GetMapping("/photos")
    public String showClientPhotos(@RequestParam(name="fbToken", required = true) String fbToken, Model model) throws Exception {
        if (fbToken == null || fbToken.equals("")) {
            throw new IllegalArgumentException("Token cannot be null!");
        }

        String tokenError = fbApi.verifyToken(fbToken);

        if (tokenError != null) {
            model.addAttribute("tokenError", tokenError);

            return "errorPage";
        } else {
            model.addAttribute("userDto", fbApi.getUserDetails(fbToken));
            model.addAttribute("fbToken", fbToken);

            return "photos";
        }
    }

}
