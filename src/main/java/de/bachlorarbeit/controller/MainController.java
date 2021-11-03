package de.bachlorarbeit.controller;


import de.bachlorarbeit.error.ErrorMessages;
import de.bachlorarbeit.exception.SurveyNotFoundException;
import de.bachlorarbeit.service.DatabaseService;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.SQLException;
import java.util.List;

@Controller
public class MainController {
    private DatabaseService databaseService;

    public MainController(DatabaseService databaseService){
        this.databaseService=databaseService;
    }

    @GetMapping("/survey/{surveyId}")
    public String getSurvey(@PathVariable String surveyId) {
        String id= surveyId;
        try {
            List<JSONObject> resultList= databaseService.getSurveyId();
            for(int i=0; i<resultList.size();i++){
                if(id.equals(resultList.get(i).get("SURVEY_ID"))) {
                    return "survey";
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        throw new SurveyNotFoundException(ErrorMessages.SurveyNotFound(id));
    }

    @GetMapping("/DFRTool")
    public String getStart() {
        return "start";
    }

    @GetMapping("/DFRTool/evaluation")
    public String getEvaluation() {
        return "evaluation";
    }

    @GetMapping("/DFRTool/analysis")
    public String getAnalysis() {
        return "analysis";
    }

    @GetMapping("/documentation")
    public String documentation() {
        return "documentation";
    }

    @GetMapping("/openapi")
    public String openapi() {
        return "openapi.json";
    }
}
