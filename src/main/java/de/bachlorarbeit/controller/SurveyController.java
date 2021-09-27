package de.bachlorarbeit.controller;

import com.sun.istack.NotNull;
import de.bachlorarbeit.service.DatabaseService;
import de.bachlorarbeit.service.SurveyService;
import org.json.simple.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

@RestController
public class SurveyController {
    private SurveyService surveyService;

    public SurveyController(SurveyService surveyService){

        this.surveyService=surveyService;
    }

    @RequestMapping(value = "/survey/{surveyId:.+}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.pfc.app-v1.0+json"}, method = RequestMethod.GET)
    public List<JSONObject> getTable(@PathVariable @NotNull String surveyId,
                                     @RequestParam(required = false) String fields,
                                     HttpServletRequest request) {
        try {
            return surveyService.getIndicatorsForSurvey(surveyId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

}

