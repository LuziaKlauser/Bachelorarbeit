package de.bachlorarbeit.controller;

import de.bachlorarbeit.model.GeneralAnswerModel;
import de.bachlorarbeit.model.ProcessStatusModel;
import de.bachlorarbeit.service.TaskService;
import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@RestController
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
    public ResponseEntity<?> uploadFiles(@RequestParam HashMap<String, Object> formData) throws SQLException {
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
    @RequestMapping(value = "/data/calculate", produces = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.pfc.app-v1.0+json"}, method = RequestMethod.GET)
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
    @RequestMapping(value = "/data/indicator_value/{surveyId:.+}", method = RequestMethod.DELETE)
    public ResponseEntity<?> clearDirectory(@PathVariable String surveyId) throws SQLException {
        System.out.println("hhh");
        taskService.deleteAnswers(surveyId);
        GeneralAnswerModel answer= new GeneralAnswerModel("Deletion successfull");
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
    @RequestMapping(value = "/data/enabler/calculate", produces = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.pfc.app-v1.0+json"}, method = RequestMethod.GET)
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
     * Uploads a json file, which holds values for the indicators
     *
     * @return ResponseEntity
     * @throws SQLException
     */
    @RequestMapping(value = "/answer/upload",
            produces = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.pfc-v1.0+json"},
            method = RequestMethod.POST)
    public ResponseEntity<?> uploadAnswer(@RequestParam("file") MultipartFile file) throws SQLException {
        taskService.postAnswer(file);
        GeneralAnswerModel answer= new GeneralAnswerModel("Survey successfully uploaded");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(answer);
    }
}

