package de.bachlorarbeit.helpers;

import de.bachlorarbeit.error.ErrorMessages;
import de.bachlorarbeit.exception.TableNotFoundException;

import java.sql.*;

/**
 * class to establish a connection to the Database in 'mySQL'
 */
public class DBConnection {
    //Change jdbURL, username, password according to your DB-Connection
    String jdbURL = "jdbc:mysql://localhost:3306/dfr";
    String username = "root";
    String password = "DFReadiness";
    private Connection connection;

    public DBConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection= DriverManager.getConnection(jdbURL,username,password);
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }
    public Connection getConnection() {
        return connection;
    }

    /**
     * Checks if the parameter tableName equals to the tables in the database
     *
     * @param tableName
     * @return boolean, whether the table with the given tableName is in the database
     */
    public boolean checkForTable(String tableName){
        tableName=tableName.toLowerCase();
        if(tableName.equals("enabler")||tableName.equals("indicator_value")||tableName.equals("department")
                ||tableName.equals("employee") ||tableName.equals("indicator")||tableName.equals("maturity_level")||tableName.equals("survey")
                ||tableName.equals("capability_level")){
            return true;
        }else{
            throw new TableNotFoundException(ErrorMessages.TableNotFound(tableName));
        }
    }
}
