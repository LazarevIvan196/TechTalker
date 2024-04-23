package com.example.techtalker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SimplePagesController {

    @GetMapping("/contacts")
    public String contacts() {
        return "contacts";
    }
    @GetMapping("/FAQ")
    public String faq() {
        return "FAQ";
    }
    @GetMapping("/first_aid")
    public String firstAid() {
        return "first_aid";
    }
}
