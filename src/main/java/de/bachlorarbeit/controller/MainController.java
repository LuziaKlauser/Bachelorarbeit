package de.bachlorarbeit.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/survey")
    public String getSurvey() {
        return "survey";
    }
}
