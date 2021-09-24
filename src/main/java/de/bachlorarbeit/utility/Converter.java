package de.bachlorarbeit.utility;

import com.fasterxml.jackson.databind.util.JSONPObject;
import springfox.documentation.spring.web.json.Json;
import org.json.simple.JSONObject;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Converter {

    public List<JSONObject> convertToJson(ResultSet resultSet){
        List<JSONObject> resultList = new ArrayList<JSONObject>();
        try {
            ResultSetMetaData metaData =resultSet.getMetaData();
            int columnCnt = metaData.getColumnCount();
            List<String> columnNames = new ArrayList<String>();
            for(int i=1;i<=columnCnt;i++) {
                columnNames.add(metaData.getColumnName(i).toUpperCase());
            }
            //Converts each object to a JSON object
            while(resultSet.next()) {
                JSONObject obj = new JSONObject();
                for(int i=1;i<=columnCnt;i++) {
                    String key = columnNames.get(i - 1);
                    String value = resultSet.getString(i);
                    obj.put(key, value);
                }
                resultList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
