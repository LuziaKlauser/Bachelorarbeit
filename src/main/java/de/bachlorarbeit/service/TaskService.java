package de.bachlorarbeit.service;

import de.bachlorarbeit.error.ErrorMessages;
import de.bachlorarbeit.exception.TableNotFoundException;
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

    /**
     * Uploads the values(fulfilled/unfulfilled) for the indicators to the database
     * @param formData
     * @throws SQLException
     */
    public void postSurvey(HashMap<String, Object> formData) throws SQLException {
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
    }

    /**
     * Deletes all in values(fulfilled/ unfulfilled) for the indicators with the given surveyId
     * @param surveyId
     * @throws SQLException
     */
    public void deleteAnswers(String surveyId) throws SQLException {
        Statement query = connection.createStatement();
            try {
                DatabaseService databaseService= new DatabaseService();
                List<JSONObject> json =databaseService.getIndicatorsForSurvey(surveyId);
                System.out.println(json);
                for(int i=0;i<json.size();i++){
                    String indicator_id= (String) json.get(i).get("INDICATOR_ID");
                    int numberOfRowsInserted = query.executeUpdate("DELETE FROM answer WHERE indicator_id="+indicator_id);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
    }

    /**
     * Calculates the MaturityLevel from the values in the database
     * @return the maturityLevel
     * @throws SQLException
     */
    public int calculateMaturityLevel() throws SQLException {
        DatabaseService databaseService= new DatabaseService();
            List<JSONObject> capLevelsForEnabler = this.calculateCapabilityLevel();
            System.out.println(capLevelsForEnabler);
            return calculate(capLevelsForEnabler);

    }

    /**
     * Calculates the the capabilityLevel for each enabler
     *
     * @return json with each enabler and their calculated capabilityLevel
     * @throws SQLException
     */
    public List<JSONObject> calculateCapabilityLevel() throws SQLException {
        DatabaseService databaseService= new DatabaseService();
        List<JSONObject> enablers= databaseService.getTable("enabler");
        List<JSONObject> capLevelsForEnabler = new ArrayList<JSONObject>();
        for(int enablerId=1; enablerId<=enablers.size();enablerId++){
            JSONObject capLevelForEnabler = new JSONObject();
            List<JSONObject> indicators= databaseService.getTableFromEnabler(enablerId);
            List<JSONObject> enabler= databaseService.getTable("enabler");
            for(int i=0;i<enabler.size();i++){
                String en_id= (String) enabler.get(i).get("ENABLER_ID");
                int id=Integer.parseInt(en_id);
                if(enablerId==id){
                    capLevelForEnabler.put("name", enabler.get(i).get("NAME"));
                }
            }
            int level= calculateCapabilityLevelForEnabler(enablerId, indicators);
            capLevelForEnabler.put("enabler",enablerId);
            capLevelForEnabler.put("capabilityLevel",level);
            capLevelsForEnabler.add(capLevelForEnabler);
        }
        return capLevelsForEnabler;
    }

    /**
     * Calculates the capability_level for the specific enabler with the given enablerId
     *
     * @param enablerId
     * @param indicators
     * @return the calculated capabilityLevel
     */
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

    /**
     * Calculates the maturityLevel with the help of the capabilityLevels from each enabler
     *
     * @param capLevelsForEnabler
     * @return the calculated maturityLevel
     */
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
        return maturityLevel;
    }

    /**
     *
     * @param answers
     * @return
     * @throws SQLException
     */
    //TODO complete
    public List<JSONObject> postAnswer(List<JSONObject> answers) throws SQLException {
        Statement query = connection.createStatement();
        for (int i = 0; i < answers.size(); i++) {
            try {
                JSONObject entry = answers.get(i);
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

}