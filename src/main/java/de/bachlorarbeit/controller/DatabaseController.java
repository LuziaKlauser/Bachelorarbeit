package de.bachlorarbeit.controller;

import com.sun.istack.NotNull;
import de.bachlorarbeit.model.ProcessStatusModel;
import de.bachlorarbeit.service.DatabaseService;
import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

@RestController
public class DatabaseController {

    private DatabaseService databaseService;

    public DatabaseController(DatabaseService databaseService){

        this.databaseService=databaseService;
    }

    /**
     *Gets the table from the database with the given tableName
     *
     * @param tableName
     * @param fields
     * @param request
     * @return the table with the given tableName in json from
     */
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

    /**
     * Checks if all indicators are checked if they are fulfilled or not
     * Compares the number of actually answered indicators to all indicators
     *
     * @param fields
     * @param request
     * @return percentage of the answered indicators to all indicators
     */
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

    /**
     * Returns all indicators who belong to the survey with the given surveyId
     *
     * @param surveyId
     * @return indicators as a json file with the keys "question" and "indicator_id"
     */
    @RequestMapping(value = "/data/survey/{surveyId:.+}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.pfc.app-v1.0+json"}, method = RequestMethod.GET)
    public List<JSONObject> getSurvey(@PathVariable @NotNull String surveyId) {
        try {
            return databaseService.getIndicatorsForSurvey(surveyId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Checks how many Surveys are in the database and returns them
     *
     * @param request
     * @return all surveys from the database with their surveyd in a json format
     */
    @RequestMapping(value = "/data/survey/surveyIds", produces = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.pfc.app-v1.0+json"}, method = RequestMethod.GET)
    public List<JSONObject> getTable(HttpServletRequest request) {
        try {
            return databaseService.getSurveyId();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Checks the date when a survey was answered
     *
     * @param request
     * @return a json file with the date and the surveyId
     */
    @RequestMapping(value = "/data/answer/time", produces = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.pfc.app-v1.0+json"}, method = RequestMethod.GET)
    public List<JSONObject> getIndicatorTime(HttpServletRequest request) {
        try {
            return databaseService.getTimeIndicator();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Returns all indicators if they are fulfilled or not and their descriptions
     * It also connects them to their enabler_id with the specific enabler name
     *
     * @param request
     * @return json with fulfilled or not fullfilled indicators, their description, enabler_id and enabler_name
     */
    @RequestMapping(value = "/data/answers", produces = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.pfc.app-v1.0+json"}, method = RequestMethod.GET)
    public List<JSONObject> getAnswerAndDescription(HttpServletRequest request) {
        try {
            return databaseService.getAnswerDescription();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}

