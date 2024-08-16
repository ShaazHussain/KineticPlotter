package com.example.proj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReactionRateController {

    @GetMapping("/index")
    public String index() {
        // Return the name of the HTML template (index.html) without the file extension
        return "index";
    }
}