package de.bachlorarbeit.service;


import de.bachlorarbeit.exception.TableNotFoundException;
import de.bachlorarbeit.helpers.DBConnection;
import org.springframework.stereotype.Service;


import java.sql.*;
import java.sql.Connection;

@Service
public class DatabaseService {
    DBConnection db = new DBConnection();

    //Gets the Data from the given tableName in the database
    public String getTable(String tableName) throws SQLException {
        Connection connection= db.getConnection();
        Statement query = connection.createStatement();
        String table= "";
        try {
            String sql = "SELECT * FROM enabler";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet s =preparedStatement.executeQuery();
            while (s.next()){
                // Get the column values
                String name = s.getString("name");
                String id = s.getString("enabler_id");
                System.out.println(id+":"+name);
                table= table+id+":"+name+"    ";
                ResultSetMetaData meta    = null;
                meta = s.getMetaData();
                int columns = meta.getColumnCount(  );
                System.out.println(columns);
            }
            System.out.println("table"+table);
            return table;
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }return null;

    }
    //prepared Statement for selecting the tables
    //As it is not possible to parameterize table names for prepared statements
    public String prepareStatment(String tableName){
        String statement= "SELECT FROM ";
        String name = tableName;

        if(tableName=="enabler"||tableName=="answer"||tableName=="department"||tableName=="employee"
                ||tableName=="indicator"||tableName=="maturity_level"||tableName=="survey"
                ||tableName=="capability_level"){
            statement+=tableName;
            return statement;
        }else{
        }

    }
}
