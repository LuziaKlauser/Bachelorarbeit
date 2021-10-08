package de.bachlorarbeit.service;

import de.bachlorarbeit.error.ErrorMessages;
import de.bachlorarbeit.exception.EmployeeNotFoundException;
import de.bachlorarbeit.exception.EnablerNotFoundException;
import de.bachlorarbeit.exception.SurveyNotFoundException;
import de.bachlorarbeit.exception.TableNotFoundException;
import de.bachlorarbeit.helpers.DBConnection;
import de.bachlorarbeit.utility.Converter;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;


import java.sql.*;
import java.sql.Connection;
import java.util.List;

@Service
public class DatabaseService {
    DBConnection db = new DBConnection();
    Connection connection= db.getConnection();
    Converter converter = new Converter();

    /**
     * Gets the Data from the given tableName in the database and and returns it as a JSONOBject
     *
     * @param tableName
     * @return table from the database
     * @throws SQLException
     */
    public List<JSONObject> getTable(String tableName) throws SQLException {
        Statement query = connection.createStatement();
        try {
            String sql= this.prepareStatement(tableName);
            ResultSet s =query.executeQuery(sql);
            List<JSONObject> resultList= converter.convertToJson(s);
            return resultList;
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }return null;
    }

    /**
     * Prepared Statement for selecting the tables
     *
     * @param tableName
     * @return the prepared statement
     */
    public String prepareStatement(String tableName){
        String statement= "SELECT * FROM ";
        if(checkForTable(tableName)){
            statement+=tableName;
        }
        return statement;
    }

    /**
     * Checks if the parameter tableName equals to the tables in the database
     *
     * @param tableName
     * @return boolean, whether the table with the given tableName is in the database
     */
    public boolean checkForTable(String tableName){
        tableName=tableName.toLowerCase();
        if(tableName.equals("enabler")||tableName.equals("answer")||tableName.equals("department")
                ||tableName.equals("employee") ||tableName.equals("indicator")||tableName.equals("maturity_level")||tableName.equals("survey")
                ||tableName.equals("capability_level")){
            return true;
        }else{
            throw new TableNotFoundException(ErrorMessages.TableNotFound(tableName));
        }
    }

    /**
     * Gets processing status in percent of the indicators
     *
     * @return the percentage of the answered indicators compared to all indicators
     */
    public int getProcessingStatus(){
        int allAnswers=this.getLineCount("answer");
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
        if(this.checkForTable(tableName)){
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

    /**
     * Gets all indicators with the given surveyId
     *
     * @param SurveyId
     * @return jsoon with all indicators with the given surveyId
     * @throws SQLException
     */
    public List<JSONObject> getIndicatorsForSurvey(String SurveyId) throws SQLException {
        Statement query = connection.createStatement();
        try {
            String sql = "SELECT indicator_id, question FROM indicator WHERE survey_id=" + SurveyId;
            ResultSet s = query.executeQuery(sql);
            List<JSONObject> resultList = converter.convertToJson(s);
            if(resultList.size()==0){
                throw new SurveyNotFoundException(ErrorMessages.SurveyNotFound(SurveyId));
            }else{
                return resultList;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets indicators and also their value(fullfilled/unfullfilled) for the given enabler_id
     * @param enabler_id
     * @return json filled with indicators, their value
     * @throws SQLException
     */
    public List<JSONObject> getTableFromEnabler(int enabler_id) throws SQLException {
        Statement query = connection.createStatement();
        try {
            String sql = "SELECT max_contribution, indicator.indicator_id, answer.type AS answer_type, " +
                    "indicator.indicator_type AS indicator_type, indicator.enabler_id  "+
                    "FROM indicator " +
                    "INNER JOIN answer ON indicator.indicator_id=answer.indicator_id WHERE indicator.enabler_id="+enabler_id;
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
     * Gets the employee_id which is connected to the given surveyId
     * @param surveyId
     * @return json with employee_id, surveyId
     * @throws SQLException
     */
    public List<JSONObject> getEmployeeIdForSurvey(String surveyId) throws SQLException {
        Statement query = connection.createStatement();
        try {
            String sql = "SELECT department.employee_id, survey.survey_id "+
                    "FROM department " +
                    "INNER JOIN survey ON survey.department_id=department.department_id WHERE survey.survey_id="+surveyId;
            ResultSet s = query.executeQuery(sql);
            List<JSONObject> resultList = converter.convertToJson(s);
            if(resultList.size()==0){
                throw new SurveyNotFoundException(ErrorMessages.SurveyNotFound(surveyId));
            }else{
                return resultList;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the email with is connected to the employee in the database for the given employeeId
     * @param employeeId
     * @return email from the employee
     * @throws SQLException
     */
    public String getEmail(String employeeId) throws SQLException {
        Statement query = connection.createStatement();
        try {
            String sql = "SELECT email FROM employee WHERE employee_id=" + employeeId;
            ResultSet s = query.executeQuery(sql);
            List<JSONObject> resultList = converter.convertToJson(s);
            if(resultList.size()==0){
                throw new EmployeeNotFoundException(ErrorMessages.EmployeeNotFound(employeeId));
            }else{
                String email= (String) resultList.get(0).get("EMAIL");
                return email;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets alls surveys from the database
     * @return all surveyIds
     * @throws SQLException
     */
    public List<JSONObject> getSurveyId() throws SQLException {
        Statement query = connection.createStatement();
        try {
            String sql = "SELECT survey_id FROM survey";
            ResultSet resultSet = query.executeQuery(sql);
            List<JSONObject> resultList = converter.convertToJson(resultSet);
            if(resultList.size()==0){
                throw new TableNotFoundException(ErrorMessages.TableNotFound("survey"));
            }else{
                return resultList;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the time to the surveys when they were filled into the database
     *
     * @return surveyId with their times
     * @throws SQLException
     */
    public List<JSONObject> getTimeIndicator() throws SQLException {
        Statement query = connection.createStatement();
        try {
            String sql = "SELECT answer.time, indicator.survey_id "+
                    "FROM  answer " +
                    "INNER JOIN indicator ON answer.indicator_id=indicator.indicator_id GROUP BY indicator.survey_id";
            ResultSet s = query.executeQuery(sql);
            List<JSONObject> resultList = converter.convertToJson(s);
            if(resultList.size()==0){
                //TODO change that exceoption and add
                throw new TableNotFoundException(ErrorMessages.TableNotFound("indicator"));
            }else{
                return resultList;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets indicators ordered by their value(fullfilled/unfullfilled)
     *
     * @return json with indicators, their description, enaber_id and type
     * @throws SQLException
     */
    public List<JSONObject> getAnswerDescription() throws SQLException {
        Statement query = connection.createStatement();
        try {
            String sql = "SELECT answer.type, indicator.description, indicator.enabler_id "+
                    "FROM  answer " +
                    "INNER JOIN indicator ON answer.indicator_id=indicator.indicator_id ORDER BY answer.type DESC, indicator.enabler_id ";
            ResultSet s = query.executeQuery(sql);
            List<JSONObject> resultList = converter.convertToJson(s);
            String sql2 = "SELECT enabler_id, name FROM enabler";
            ResultSet s2 = query.executeQuery(sql2);
            List<JSONObject> resultList2 = converter.convertToJson(s2);
            if(resultList.size()==0&&resultList2.size()==0){
                //TODO change that exceoption and add
                throw new TableNotFoundException(ErrorMessages.TableNotFound("indicator"));
            }else{
                for(int i=0;i<resultList.size();i++){
                    for(int j=0; j<resultList2.size();j++){
                        if(resultList.get(i).get("ENABLER_ID").equals(resultList2.get(j).get("ENABLER_ID"))){
                            resultList.get(i).put("ENABLER_NAME",resultList2.get(j).get("NAME"));
                        }
                    }
                }
                return resultList;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
