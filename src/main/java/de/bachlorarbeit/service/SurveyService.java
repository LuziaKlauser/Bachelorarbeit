package de.bachlorarbeit.service;

import de.bachlorarbeit.helpers.DBConnection;
import de.bachlorarbeit.utility.Converter;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Service
public class SurveyService {
    DBConnection db = new DBConnection();
    Connection connection= db.getConnection();
    Converter converter = new Converter();

    public List<JSONObject> getIndicatorsForSurvey(String SurveyId) throws SQLException {

        Statement query = connection.createStatement();
        try {
            String sql= "SELECT indicator_id, question FROM indicator WHERE survey_id="+SurveyId;
            ResultSet s =query.executeQuery(sql);
            List<JSONObject> resultList= converter.convertToJson(s);
            JSONObject json= resultList.get(1);
            System.out.println(json.get("INDICATOR_ID"));
            return resultList;
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }return null;
    }
}
