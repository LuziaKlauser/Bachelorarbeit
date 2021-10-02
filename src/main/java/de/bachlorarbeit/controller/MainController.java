package de.bachlorarbeit.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {
    private DatabaseController databaseController;

    public MainController(DatabaseController databaseController){
        this.databaseController=databaseController;
    }

    @GetMapping("/survey/{surveyId}")
    public String getSurvey(@PathVariable String surveyId) {
        String id= surveyId;
        databaseController.getSurvey(id);
        return "survey";
    }

    @GetMapping("/DFRTool")
    public String getStart() {
        return "start";
    }


    @GetMapping("/DFRTool/evaluation")
    public String getEvaluatio() {
        return "evaluation";
    }


}
