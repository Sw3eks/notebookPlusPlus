package de.mobicom.notebookplusplus.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

import androidx.room.TypeConverter;

public class LocalDateTimeConverter {

    @TypeConverter
    public static LocalDateTime toLocalDateTime(String dateString) {
        if (dateString == null) {
            return null;
        } else {
            return LocalDateTime.parse(dateString);
        }
    }

    @TypeConverter
    public static String toLocalDateTimeString(LocalDateTime date) {
        if (date == null) {
            return null;
        } else {
            return date.toString();
        }
    }

    @TypeConverter
    public static LocalDate toLocalDate(String dateString) {
        if (dateString == null) {
            return null;
        } else {
            return LocalDate.parse(dateString);
        }
    }

    @TypeConverter
    public static String toLocalDateString(LocalDate date) {
        if (date == null) {
            return null;
        } else {
            return date.toString();
        }
    }
}
