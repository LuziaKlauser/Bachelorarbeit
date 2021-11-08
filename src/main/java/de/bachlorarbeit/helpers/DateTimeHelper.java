package de.bachlorarbeit.helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utility class to process date and time information.
 */
public class DateTimeHelper {

    /**
     * Private constructor so no instances of this class can be created.
     */
    private DateTimeHelper() {}


    /**
     * Returns the current date and time in the ISO 8601 format.
     * @return current date and time as ISO 8601 formatted String.
     */
    public static String getNowISO() {
        TimeZone tz = TimeZone.getTimeZone("UTC");

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // Quoted "Z" to indicate UTC, no timezone offset

        df.setTimeZone(tz);

        return  df.format(new Date());
    }

}
