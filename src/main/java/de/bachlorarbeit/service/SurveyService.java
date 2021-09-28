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
public class SurveyService {
    DBConnection db = new DBConnection();
    Connection connection = db.getConnection();
    Converter converter = new Converter();

    public List<JSONObject> getIndicatorsForSurvey(String SurveyId) throws SQLException {

        Statement query = connection.createStatement();
        try {
            String sql = "SELECT indicator_id, question FROM indicator WHERE survey_id=" + SurveyId;
            ResultSet s = query.executeQuery(sql);
            List<JSONObject> resultList = converter.convertToJson(s);
            return resultList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public List<JSONObject> postSurvey(HashMap<String, Object> formData) throws SQLException {
        List<JSONObject> surveyResult=converter.convertFormdataToJson(formData);
        System.out.println(surveyResult);
        Statement query = connection.createStatement();
        for(int i=0; i<surveyResult.size();i++) {
            try {
                //TODO change Tablename answer
                JSONObject entry = surveyResult.get(i);
                String indicator_id = (String) entry.get("indicator_id");
                String answer = (String) entry.get("answer");
                System.out.println(indicator_id);
                LocalDate date = LocalDate.now();
                String sql2 = "INSERT INTO answer (answer_id, type, time, indicator_id) VALUES (4, 'yes', '2019-11-1' , 6)";

                String sql = "SELECT indicator_id, question FROM indicator WHERE survey_id=1 ";
                ResultSet s = query.executeQuery(sql2);
                //List<JSONObject> resultList = converter.convertToJson(s);
                //JSONObject json = resultList.get(1);
                //System.out.println(json.get("INDICATOR_ID"));
                //return resul;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return null;
    }
}