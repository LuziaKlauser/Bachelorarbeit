package de.bachlorarbeit.error;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ErrorMessages {

    public static String TableNotFound(String tableName) {
        return "There is no table with the name "+tableName;
    }

    public static String SurveyNotFound(String SurveyId) {
        return "There is no survey with the id "+ SurveyId;
    }
    public static String EnablerNotFound(int enabler_id) {
        return "There is no entry with the enabler_id: "+ enabler_id;
    }

}
