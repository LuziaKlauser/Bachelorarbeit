package de.bachlorarbeit.utility;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to format Strings.
 */
public class StringFormatHelper {

    /**
     * Private constructor so no instances of this class can be created.
     */
    private StringFormatHelper() {}

    /**
     * Formats a string with word dividers underscore or minus and converts all words to uppercase.
     *
     * @param string the string to convert
     * @return a converted string
     */
    public static String formatEachWordUppercase(String string) {
        string = string.toLowerCase();

        String[] words = string.split("[_|-]");

        StringBuilder s = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            s.append(words[i].substring(0, 1).toUpperCase());
            if (words[i].length() > 1) s.append(words[i].substring(1));
            if (i < words.length - 1) s.append(" ");
        }
        return s.toString();
    }

    /**
     * Parses fields string to get a list of all individual values separated by commas in a query string value list.
     *
     * @param s the string containing attributes
     * @return a list of attributes to include
     */
    public static List<String> extractValueListFromQueryValueString(String s) {
        if (s == null || s.length() == 0) return new ArrayList<>();
        try {
            s = new String(s.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (s.charAt(0) == '(') s = s.substring(1); //Remove brace
        if (s.charAt(s.length() - 1) == ')') s = s.substring(0, s.length() - 1); //Remove brace
        s = s.replaceAll(" ", ""); //Remove spaces
        String[] filtersComma = s.split(",");
        return new ArrayList<>(List.of(filtersComma));
    }
}
