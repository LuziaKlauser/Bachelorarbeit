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

}
