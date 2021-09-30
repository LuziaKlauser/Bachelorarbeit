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
import java.util.ArrayList;
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
        List<JSONObject> capLevelsForEnabler = new ArrayList<JSONObject>();
        for(int enablerId=1; enablerId<=enablers.size();enablerId++){
            JSONObject capLevelForEnabler = new JSONObject();
            List<JSONObject> indicators= databaseService.getTableFromEnabler(enablerId);
            int level= calculateCapabilityLevelForEnabler(enablerId, indicators);
            capLevelForEnabler.put("enabler",enablerId);
            capLevelForEnabler.put("capabilityLevel",level);
            capLevelsForEnabler.add(capLevelForEnabler);
        }
        System.out.println(capLevelsForEnabler);
        return calculate(capLevelsForEnabler);
    }
    //calculates the capability_level for the specific enabler
    public int calculateCapabilityLevelForEnabler(int enablerId,List<JSONObject> indicators) {
        int capability_level = 0;
        for (int i=1; i<4;i++){
            boolean levelAchieved=true;
            for(int j=0; j<indicators.size(); j++){
                JSONObject indicator= indicators.get(j);
                if(indicator.get("MAX_CONTRIBUTION").equals(Integer.toString(i))
                        &&indicator.get("INDICATOR_TYPE").equals("verpflichtend")
                        &&indicator.get("TYPE").equals("no")) {
                            levelAchieved=false;
                            break;
                }
            }
            if(levelAchieved){
                capability_level++;
            }else {
                break;
            }
        }
        return capability_level;
    }
    public int calculate(List<JSONObject> capLevelsForEnabler){
        int maturityLevel=1;
        int checkLevel =2;
        boolean maturityLevelAchieved=true;
        while(maturityLevelAchieved&&checkLevel<6){
            switch (checkLevel){
                case 2:
                case 5:
                    for(int i=0;i<capLevelsForEnabler.size();i++){
                        int level= (int) capLevelsForEnabler.get(i).get("capabilityLevel");
                        if(checkLevel==2&&level<1||checkLevel==5&&level<3) {
                            maturityLevelAchieved = false;
                            break;
                        }
                    }if(maturityLevelAchieved){
                        checkLevel++;
                        maturityLevel++;
                    }
                    break;
                case 3:
                    for(int i=0;i<capLevelsForEnabler.size();i++){
                        int level= (int) capLevelsForEnabler.get(i).get("capabilityLevel");
                        if(level<2||level<1&&capLevelsForEnabler.get(i).get("enabler").equals("5")) {
                            maturityLevelAchieved = false;
                            break;
                        }
                    }
                    if(maturityLevelAchieved){
                        checkLevel++;
                        maturityLevel++;
                    }
                    break;
                case 4:
                    for(int i=0;i<capLevelsForEnabler.size();i++){
                        int level= (int) capLevelsForEnabler.get(i).get("capabilityLevel");
                        if(level<3||
                                level<2&&capLevelsForEnabler.get(i).get("enabler").equals("1")||
                                level<2&&capLevelsForEnabler.get(i).get("enabler").equals("7")) {
                            maturityLevelAchieved = false;
                            break;
                        }
                    }
                    if(maturityLevelAchieved){
                        checkLevel++;
                        maturityLevel++;
                    }
                    break;
            }
        }
        System.out.println("LEvel: "+maturityLevel);
        return maturityLevel;
    }
}