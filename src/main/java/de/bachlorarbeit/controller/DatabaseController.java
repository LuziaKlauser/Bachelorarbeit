package de.bachlorarbeit.controller;

import com.sun.istack.NotNull;
import de.bachlorarbeit.service.DatabaseService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
}
