package com.machava.demo.controllers;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Web {

    @GetMapping("/photos")
    public String showClientPhotos(@RequestParam(name="fbToken", required = true) String fbToken, Model model) throws IOException {
        if (fbToken == null || fbToken.equals("")) {
            throw new IllegalArgumentException("Token cannot be null!");
        }

        Boolean permissionsOk = FbApi.verifyToken(fbToken);

        model.addAttribute("isTokenValid", permissionsOk);
        if (permissionsOk) {
        }
        model.addAttribute("userDto", FbApi.getUserDetails(fbToken));
        model.addAttribute("fbToken", fbToken);
        return "photos";
    }

}
