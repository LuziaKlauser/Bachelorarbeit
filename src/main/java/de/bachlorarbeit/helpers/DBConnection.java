package de.bachlorarbeit.helpers;

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
}
