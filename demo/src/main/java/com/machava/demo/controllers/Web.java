package com.machava.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Web {

    @GetMapping("/photos")
    public String showClientPhotos(@RequestParam(name="fbToken", required = true) String fbToken, Model model) {
        model.addAttribute("fbToken", fbToken);
        return "photos";
    }

}
