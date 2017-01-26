package ai.victorl.toda.data.entry;

import android.support.annotation.NonNull;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Scanner;

public class EntryDateFormatter {

    private static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.CANADA);
    private static SimpleDateFormat prettyFormatter = new SimpleDateFormat("EEEE MMMM d, yyyy", Locale.CANADA);

    public static CalendarDay parse(@NonNull Entry entry) {
        return parse(entry.date);
    }

    public static CalendarDay parse(@NonNull String date) {
        Scanner scanner = new Scanner(date).useDelimiter("/");

        int day = scanner.nextInt();
        int month = scanner.nextInt() - 1;
        int year = scanner.nextInt();

        scanner.close();

        return CalendarDay.from(year, month, day);
    }

    public static String format(CalendarDay day) {
        return formatter.format(day.getDate());
    }

    public static String readableFormat(CalendarDay day) {
        return prettyFormatter.format(day.getDate());
    }
}
