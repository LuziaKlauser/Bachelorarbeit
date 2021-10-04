package de.bachlorarbeit.controller;

import com.sun.istack.NotNull;
import de.bachlorarbeit.model.ProcessStatusModel;
import de.bachlorarbeit.service.DatabaseService;
import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@RestController
public class DatabaseController {

    private DatabaseService databaseService;

    public DatabaseController(DatabaseService databaseService){

        this.databaseService=databaseService;
    }

    @RequestMapping(value = "/data/{tableName:.+}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.pfc.app-v1.0+json"}, method = RequestMethod.GET)
    public List<JSONObject> getTable(@PathVariable @NotNull String tableName,
                                     @RequestParam(required = false) String fields,
                                     HttpServletRequest request) {
        try {
            return databaseService.getTable(tableName);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/data/answer/check", produces = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.pfc.app-v1.0+json"}, method = RequestMethod.GET)
    public ResponseEntity<?> getProcessingStatus(@RequestParam(required = false) String fields,
                                     HttpServletRequest request) {

        int percent= databaseService.getProcessingStatus();
        JSONObject obj = new JSONObject();
        ProcessStatusModel processStatus= new ProcessStatusModel(percent);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(processStatus);

    }
    @RequestMapping(value = "/data/survey/{surveyId:.+}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.pfc.app-v1.0+json"}, method = RequestMethod.GET)
    public List<JSONObject> getSurvey(@PathVariable @NotNull String surveyId) {
        try {
            return databaseService.getIndicatorsForSurvey(surveyId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    @RequestMapping(value = "/data/survey/surveyIds", produces = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.pfc.app-v1.0+json"}, method = RequestMethod.GET)
    public List<JSONObject> getTable(HttpServletRequest request) {
        try {
            return databaseService.getSurveyId();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    @RequestMapping(value = "/data/answer/time", produces = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.pfc.app-v1.0+json"}, method = RequestMethod.GET)
    public List<JSONObject> getIndicatorTime(HttpServletRequest request) {
        try {
            return databaseService.getTimeIndicator();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}

