package de.bachlorarbeit.service;

import de.bachlorarbeit.helpers.DBConnection;
import de.bachlorarbeit.utility.Converter;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Service
public class TaskService {
    DBConnection db = new DBConnection();
    Connection connection = db.getConnection();
    Converter converter = new Converter();

    public List<JSONObject> postSurvey(HashMap<String, Object> formData) throws SQLException {
        Statement query = connection.createStatement();
        List<JSONObject> surveyResult = converter.convertFormdataToJson(formData);
        for (int i = 0; i < surveyResult.size(); i++) {
            try {
                JSONObject entry = surveyResult.get(i);
                String indicator_id = (String) entry.get("indicator_id");
                String answer = (String) entry.get("answer");
                LocalDate date = LocalDate.now();
                int numberOfRowsInserted = query.executeUpdate("INSERT into answer(answer_id,type, time, indicator_id)"
                        + "SELECT * FROM (SELECT " + indicator_id + " AS answer_id, '" + answer + "' as type, '" + date + "'AS time,"
                        + indicator_id + " as indicator_id) AS temp " +
                        "WHERE NOT EXISTS (SELECT  indicator_id FROM answer WHERE indicator_id=" + indicator_id + ")");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return null;
    }
    //TODO delete method
    public List<JSONObject> postSurve(HashMap<String, Object> formData) throws SQLException {
        Statement query = connection.createStatement();
            try {
                //int numberOfRowsInserted = query.executeUpdate("INSERT into survey(survey_id, description, department_id)"
                //+"values (3, 'ffe', 1)");
                int numberOfRowsInserted = query.executeUpdate("DELETE FROM survey WHERE survey_id=3");

                System.out.println(numberOfRowsInserted);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        return null;
    }

    public int calculateMaturityLevel() throws SQLException {
        Statement query = connection.createStatement();
        DatabaseService databaseService= new DatabaseService();
        List<JSONObject> enablers= databaseService.getTable("enabler");
        List<JSONObject> capabilityLevels= databaseService.getTable("capability_level");
        List<JSONObject> answers= databaseService.getTable("answer");
        List<JSONObject> indicators= databaseService.getTable("indicator");
        JSONObject capabilityLevelForEnabler= new JSONObject();
        for(int i=0;i<enablers.size();i++){
            String enablerId = (String) enablers.get(i).get("ENABLER_ID");
            calculateCapabilityLevelForEnabler(enabler, answers,);
        }
        for (int i=0; i<answers.size();i++){
            JSONObject answerEntity= answers.get(i);
            String answerIndicatorId= (String) answerEntity.get("INDICATOR_ID");
            for(int j=0;j<indicators.size();j++){
                JSONObject indicatorEntity=indicators.get(j);
                String indicatorId= (String) indicatorEntity.get("INDICATOR_ID");
                if(indicatorId.equals(answerIndicatorId)){

                    break;
                }
            }

        }
        return 0;
    }
    public int calculateCapabilityLevelForEnabler(String enablerId,List<JSONObject> answers,List<JSONObject> indicator) {
        int capability_level = 0;
        for (int i = 0; i < answers.size(); i++) {
            JSONObject answerEntity = answers.get(i);
            if (!answerEntity.get("ENABLER_ID").equals(enablerId)) {
                break;
            } else {
                String answerIndicatorId = (String) answerEntity.get("INDICATOR_ID");
            }
        }
        return capability_level;
    }
}