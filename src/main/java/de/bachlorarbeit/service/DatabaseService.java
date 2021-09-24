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

    //Gets the Data from the given tableName in the database and and returns it as a JSONOBject
    public List<JSONObject> getTable(String tableName) throws SQLException {
        Connection connection= db.getConnection();
        Statement query = connection.createStatement();
        try {
            String sql= this.prepareStatement(tableName);
            ResultSet s =query.executeQuery(sql);
            Converter converter = new Converter();
            List<JSONObject> resultList= converter.convertToJson(s);
            return resultList;
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }return null;
    }
    //prepared Statement for selecting the tables
    public String prepareStatement(String tableName){
        String statement= "SELECT * FROM ";
        tableName=tableName.toLowerCase();
        if(tableName.equals("enabler")||tableName.equals("answer")||tableName.equals("department")
                ||tableName.equals("employee") ||tableName.equals("indicator")||tableName.equals("maturity_level")||tableName.equals("survey")
                ||tableName.equals("capability_level")){
            statement+=tableName;
        }else{
            throw new TableNotFoundException(ErrorMessages.TableNotFound(tableName));
        }
        return statement;
    }
}
