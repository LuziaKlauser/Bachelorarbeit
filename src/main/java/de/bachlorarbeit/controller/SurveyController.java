package de.bachlorarbeit.controller;

import com.sun.istack.NotNull;
import de.bachlorarbeit.service.DatabaseService;
import de.bachlorarbeit.service.SurveyService;
import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.HashMap;
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

    @RequestMapping(value = "/survey/upload",
            produces = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.pfc-v1.0+json"},
            method = RequestMethod.POST)
    public ResponseEntity<?> uploadFiles(@RequestParam HashMap<String, Object> formData) throws SQLException {
        surveyService.postSurvey(formData);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body("hello");
    }
}

