package de.bachlorarbeit.helpers;

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
}
