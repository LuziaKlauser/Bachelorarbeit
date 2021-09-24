package de.bachlorarbeit.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
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

    /**
     * Returns a human readable String format for a given duration.
     * @param d the duration to convert to String
     * @return a formatted String for the duration with hours to nano seconds
     */
    public static String formatDuration(Duration d) {
        return String.format("%dh %dm %ds %dms %dns", d.toHoursPart(), d.toMinutesPart(), d.toSecondsPart(),
                d.toMillisPart(), d.toNanosPart() - (d.toMillisPart() * 1000000));
    }
}
