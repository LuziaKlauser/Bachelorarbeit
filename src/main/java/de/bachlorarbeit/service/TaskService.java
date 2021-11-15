package de.bachlorarbeit.service;

import de.bachlorarbeit.error.ErrorMessages;
import de.bachlorarbeit.exception.EnablerNotFoundException;
import de.bachlorarbeit.exception.MissingDataException;
import de.bachlorarbeit.helpers.DBConnection;
import de.bachlorarbeit.helpers.Converter;
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
        if(formData.isEmpty()){
            throw new MissingDataException(ErrorMessages.MissingData());
        }
        Statement query = connection.createStatement();
        List<JSONObject> surveyResult = converter.convertFormdataToJson(formData);
        for (int i = 0; i < surveyResult.size(); i++) {
            try {
                JSONObject entry = surveyResult.get(i);
                String indicator_id = (String) entry.get("indicator_id");
                String indicator_value = (String) entry.get("indicator_value");
                LocalDate date = LocalDate.now();
                int numberOfRowsInserted = query.executeUpdate("INSERT into indicator_value(value_id,type, time, indicator_id)"
                        + "SELECT * FROM (SELECT " + indicator_id + " AS value_id, '" + indicator_value + "' as type, '" + date + "'AS time,"
                        + indicator_id + " as indicator_id) AS temp " +
                        "WHERE NOT EXISTS (SELECT  indicator_id FROM indicator_value WHERE indicator_id=" + indicator_id + ")");
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
    public int deleteIndicatorValue(String surveyId) throws SQLException {
        Statement query = connection.createStatement();
        int rowsDeleted=0;
            try {
                DatabaseService databaseService= new DatabaseService();
                List<JSONObject> json =databaseService.getIndicatorsForSurvey(surveyId);
                for(int i=0;i<json.size();i++){
                    String indicator_id= (String) json.get(i).get("INDICATOR_ID");
                    int numberOfRowsInserted = query.executeUpdate("DELETE FROM indicator_value WHERE indicator_id="+indicator_id);
                    rowsDeleted=rowsDeleted+numberOfRowsInserted;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return rowsDeleted;
    }

    /**
     * Calculates the MaturityLevel from the values in the database
     * @return the maturityLevel
     * @throws SQLException
     */
    public int calculateMaturityLevel() throws SQLException {
        DatabaseService databaseService= new DatabaseService();
            List<JSONObject> capLevelsForEnabler = this.calculateCapabilityLevel();
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
            List<JSONObject> indicators= this.getIndicatorValuesForEnabler(enablerId);
            int level= calculateCapabilityLevelForEnabler(enablerId, indicators);

            capLevelForEnabler.put("name", enablers.get(enablerId-1).get("NAME"));
            capLevelForEnabler.put("enabler",enablerId);
            capLevelForEnabler.put("capabilityLevel",level);
            capLevelsForEnabler.add(capLevelForEnabler);
        }
        return capLevelsForEnabler;
    }

    /**
     * Gets indicators and also their value(fulfilled/unfulfilled) for the given enabler_id
     * @param enabler_id
     * @return json filled with indicators, their value
     * @throws SQLException
     */
    public List<JSONObject> getIndicatorValuesForEnabler(int enabler_id) throws SQLException {
        Statement query = connection.createStatement();
        try {
            String sql = "SELECT max_contribution, indicator.indicator_id, indicator_value.type AS answer_type, " +
                    "indicator.indicator_type AS indicator_type, indicator.enabler_id  "+
                    "FROM indicator " +
                    "INNER JOIN indicator_value ON indicator.indicator_id=indicator_value.indicator_id WHERE indicator.enabler_id="+enabler_id;
            ResultSet s = query.executeQuery(sql);
            List<JSONObject> resultList = converter.convertToJson(s);
            if(resultList.size()==0){
                throw new EnablerNotFoundException(ErrorMessages.EnablerNotFound(enabler_id));
            }else{
                return resultList;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
     * Gets processing status in percent of the indicators
     *
     * @return the percentage of the answered indicators compared to all indicators
     */
    public int getProcessingStatus(){
        int allAnswers=this.getLineCount("indicator_value");
        int allIndicators=this.getLineCount("indicator");
        int percent= 100* allAnswers / allIndicators;
        return percent;
    }

    /**
     * Gets the number of lines of the given table
     *
     * @param tableName
     * @return the number of lines
     */
    public int getLineCount(String tableName){
        int count=0;
        if(db.checkForTable(tableName)){
            try {
                Statement query = connection.createStatement();
                String sql="SELECT * FROM "+tableName;
                ResultSet resultSet =query.executeQuery(sql);
                List<JSONObject> json= converter.convertToJson(resultSet);
                count= json.size();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return count;
    }
}
