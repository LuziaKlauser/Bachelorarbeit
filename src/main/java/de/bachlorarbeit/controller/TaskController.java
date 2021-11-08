package de.bachlorarbeit.controller;

import de.bachlorarbeit.model.GeneralAnswerModel;
import de.bachlorarbeit.model.ProcessStatusModel;
import de.bachlorarbeit.service.TaskService;
import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@RestController
@Validated
public class TaskController {
    private TaskService taskService;

    public TaskController(TaskService taskService){

        this.taskService = taskService;
    }


    /**
     * Uploads the answers to a survey into the database
     *
     * @param formData
     * @return ResponseEntity
     * @throws SQLException
     */
    @RequestMapping(value = "/survey/upload",
            produces = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.pfc-v1.0+json"},
            method = RequestMethod.POST)
    public ResponseEntity<?> uploadData(@RequestParam @NotNull HashMap<String, Object> formData) throws SQLException {
        taskService.postSurvey(formData);
        GeneralAnswerModel answer= new GeneralAnswerModel("Survey successfully uploaded");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(answer);
    }

    /**
     * Calculates the maturity level with the indicators from the database
     * @param fields
     * @param request
     * @return maturitylevel
     */
    @RequestMapping(value = "/data/indicator-value/calculate", produces = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.pfc.app-v1.0+json"}, method = RequestMethod.GET)
    public int calculate(@RequestParam(required = false) String fields,
                                     HttpServletRequest request) {
        try {
            return taskService.calculateMaturityLevel();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    /**
     * Deletes all indicator_values to their indicators with the given surveyId
     * @param surveyId
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/data/indicator-value/{surveyId:.+}/delete",
            method = RequestMethod.DELETE)
    public ResponseEntity<?> clearIndicatorValue(@PathVariable String surveyId) throws SQLException {
        int deletedRows= taskService.deleteIndicatorValue(surveyId);
        GeneralAnswerModel answer= new GeneralAnswerModel("Deletion successful. "+deletedRows+" entries deleted");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(answer);
    }

    /**
     * Calculates the capabilityLevel for each enabler
     *
     * @param fields
     * @param request
     * @return json with the calculated capabilityLevel and the enabler_ id, enabler_name
     */
    @RequestMapping(value = "/data/indicator-value/enabler/calculate", produces = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.pfc.app-v1.0+json"}, method = RequestMethod.GET)
    public List<JSONObject> calculateEnabler(@RequestParam(required = false) String fields,
                         HttpServletRequest request) {
        try {
            return taskService.calculateCapabilityLevel();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    /**
     * Checks if all indicators are checked if they are fulfilled or not
     * Compares the number of actually answered indicators to all indicators
     *
     * @return percentage of the answered indicators to all indicators
     */
    @RequestMapping(value = "/data/indicator-value/check", produces = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.pfc.app-v1.0+json"}, method = RequestMethod.GET)
    public ResponseEntity<?> getProcessingStatus() {

        int percent= taskService.getProcessingStatus();
        ProcessStatusModel processStatus= new ProcessStatusModel(percent);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(processStatus);

    }

    /**
     * Uploads a json file, which holds values for the indicators
     *
     * @return ResponseEntity
     * @throws SQLException
     */
    //TODO class
    @RequestMapping(value = "/indicator-value/upload",
            produces = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.pfc-v1.0+json"},
            method = RequestMethod.GET)
    public ResponseEntity<?> uploadIndicatorValue(@RequestParam("file") MultipartFile file) throws SQLException {
        return null;
    }


}

