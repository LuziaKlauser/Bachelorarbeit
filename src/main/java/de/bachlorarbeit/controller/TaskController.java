package de.bachlorarbeit.controller;

import com.sun.istack.NotNull;
import de.bachlorarbeit.service.TaskService;
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
public class TaskController {
    private TaskService taskService;

    public TaskController(TaskService taskService){

        this.taskService = taskService;
    }

    @RequestMapping(value = "/survey/upload",
            produces = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.pfc-v1.0+json"},
            method = RequestMethod.POST)
    public ResponseEntity<?> uploadFiles(@RequestParam HashMap<String, Object> formData) throws SQLException {
        taskService.postSurvey(formData);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                //TODO body
                .body("Survey successfully uploaded");
    }

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
}

