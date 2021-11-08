package de.bachlorarbeit.controller;

import com.sun.istack.NotNull;
import de.bachlorarbeit.model.ProcessStatusModel;
import de.bachlorarbeit.service.DatabaseService;
import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

@RestController
@Validated
public class DatabaseController {

    private DatabaseService databaseService;

    public DatabaseController(DatabaseService databaseService){

        this.databaseService=databaseService;
    }

    /**
     *Gets the table from the database with the given tableName
     *
     * @param tableName
     * @param request
     * @return the table with the given tableName in json from
     */
    @RequestMapping(value = "/data/{tableName:.+}",
            produces = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.pfc.app-v1.0+json"},
            method = RequestMethod.GET)
    public List<JSONObject> getTable(@PathVariable @NotNull String tableName,
                                     HttpServletRequest request) {
        try {
            return databaseService.getTable(tableName);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
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
     * @return all surveys from the database with their surveyd in a json format
     */
    @RequestMapping(value = "/data/survey/surveyId", produces = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.pfc.app-v1.0+json"}, method = RequestMethod.GET)
    public List<JSONObject> getTable() {
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
    @RequestMapping(value = "/data/indicator-value/surveyId/time", produces = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.pfc.app-v1.0+json"}, method = RequestMethod.GET)
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
    @RequestMapping(value = "/data/indicator-value/enabler", produces = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.pfc.app-v1.0+json"}, method = RequestMethod.GET)
    public List<JSONObject> getAnswerAndDescription(HttpServletRequest request) {
        try {
            return databaseService.getIndicatorValueDescription();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}


