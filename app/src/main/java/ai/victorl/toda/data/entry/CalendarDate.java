package ai.victorl.toda.data.entry;

import android.support.annotation.Nullable;

import com.google.common.base.Objects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarDate {

    private final Date date;
    private static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    private CalendarDate(Date date) {
        this.date = date;
    }

    private String getFormattedString() {
        return formatter.format(date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalendarDate otherCalendarDate = (CalendarDate) o;
        return getFormattedString().equals(otherCalendarDate.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(date);
    }

    @Override
    public String toString() {
        return getFormattedString();
    }

    public static class Builder {

        public CalendarDate forToday() {
            return new CalendarDate(Calendar.getInstance().getTime());
        }

        public CalendarDate fromDate(int year, int month, int day) {
            final Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            return new CalendarDate(calendar.getTime());
        }

        @Nullable
        public CalendarDate fromDateString(String dateString) {
            try {
                return new CalendarDate(formatter.parse(dateString));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
