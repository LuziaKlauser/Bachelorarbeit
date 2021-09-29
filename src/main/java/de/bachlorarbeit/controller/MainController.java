package de.bachlorarbeit.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {

    @GetMapping("/survey/{surveyId}")
    public String getSurvey(@PathVariable String surveyId) {

        return "survey";
    }
}
