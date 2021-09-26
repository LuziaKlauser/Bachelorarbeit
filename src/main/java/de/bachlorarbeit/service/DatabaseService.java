package de.bachlorarbeit.service;


import de.bachlorarbeit.error.ErrorMessages;
import de.bachlorarbeit.exception.TableNotFoundException;
import de.bachlorarbeit.helpers.DBConnection;
import de.bachlorarbeit.utility.Converter;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;


import java.sql.*;
import java.sql.Connection;
import java.util.List;
import java.util.Locale;

@Service
public class DatabaseService {
    DBConnection db = new DBConnection();
    Connection connection= db.getConnection();
    Converter converter = new Converter();

    //Gets the Data from the given tableName in the database and and returns it as a JSONOBject
    public List<JSONObject> getTable(String tableName) throws SQLException {
        Statement query = connection.createStatement();
        try {
            String sql= this.prepareStatement(tableName);
            ResultSet s =query.executeQuery(sql);
            List<JSONObject> resultList= converter.convertToJson(s);
            System.out.println(resultList.get(1).get("DESCRIPTION")+"ID:"+resultList.get(1).get("CAPABILITY_ID"));
            return resultList;
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }return null;
    }
    //prepared Statement for selecting the tables
    public String prepareStatement(String tableName){
        String statement= "SELECT * FROM ";
        if(checkForTable(tableName)){
            statement+=tableName;
        }
        return statement;
    }
    //Checks if the parameter tableName equals to the tables in the database
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

    //Gets processing status in percent of the indicators
    public int getProcessingStatus(){
        int allAnswers=this.getLineCount("answer");
        int allIndicators=this.getLineCount("indicator");
        System.out.println(allAnswers);
        System.out.println(allIndicators);
        int percent= 100* allAnswers / allIndicators;
        return percent;
    }

    //Gets the number of lines of the given table
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

}
