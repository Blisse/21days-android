package ai.victorl.toda.utils;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarUtil {

    public static List<List<CalendarDay>> getStreaks(List<CalendarDay> days) {
        List<List<CalendarDay>> streaks = new ArrayList<>();
        List<CalendarDay> currentStreak = new ArrayList<>();
        for (CalendarDay currentDay: days) {
            if (currentStreak.isEmpty()) {
                currentStreak.add(currentDay);
            } else {
                CalendarDay previousDay = currentStreak.get(currentStreak.size() - 1);
                if (CalendarUtil.daysAhead(previousDay, currentDay, 1)) {
                    currentStreak.add(currentDay);
                } else {
                    streaks.add(currentStreak);
                    currentStreak = new ArrayList<>();
                    currentStreak.add(currentDay);
                }
            }
        }
        streaks.add(currentStreak);
        return streaks;
    }

    public static boolean daysAhead(CalendarDay behindDay, CalendarDay aheadDay, int days) {
        Calendar ahead = Calendar.getInstance();
        ahead.setTime(aheadDay.getCalendar().getTime());
        Calendar behind = Calendar.getInstance();
        behind.setTime(behindDay.getCalendar().getTime());
        return daysAhead(behind, ahead, days);
    }

    public static boolean daysAhead(Calendar behind, Calendar ahead, int days) {
        behind.add(Calendar.DAY_OF_YEAR, days);
        return (behind.get(Calendar.YEAR) == ahead.get(Calendar.YEAR))
                && (behind.get(Calendar.DAY_OF_YEAR) == ahead.get(Calendar.DAY_OF_YEAR));
    }
}
