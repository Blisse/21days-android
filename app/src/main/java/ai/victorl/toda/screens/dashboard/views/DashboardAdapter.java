package ai.victorl.toda.screens.dashboard.views;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ai.victorl.toda.R;
import ai.victorl.toda.data.entry.Entry;
import ai.victorl.toda.data.entry.EntryDateFormatter;
import ai.victorl.toda.ui.ColorCircleDayViewDecorator;
import ai.victorl.toda.ui.RecyclerViewHolder;
import ai.victorl.toda.ui.RecyclerViewHolderLayout;
import ai.victorl.toda.utils.CalendarUtil;
import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardAdapter<T extends DashboardAdapter.DashboardViewHolder> extends RecyclerView.Adapter<T> {

    private final List<RecyclerViewHolderLayout<T>> dashboardViewHolderLayouts = Arrays.asList(
            new RecyclerViewHolderLayout<T>(R.layout.item_dashboard_info, (Class<T>) DashboardInfoViewHolder.class),
            new RecyclerViewHolderLayout<T>(R.layout.item_dashboard_calendar, (Class<T>) DashboardCalendarViewHolder.class),
            new RecyclerViewHolderLayout<T>(R.layout.item_dashboard_stats, (Class<T>) DashboardStatsViewHolder.class)
    );

    private final DashboardListener listener;

    public DashboardAdapter(DashboardListener listener) {
        this.listener = listener;
    }

    private List<Entry> entries = new ArrayList<>();

    public void setEntries(List<Entry> entries) {
        this.entries.clear();
        this.entries.addAll(entries);
        notifyDataSetChanged();
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        return RecyclerViewHolder.create(dashboardViewHolderLayouts.get(viewType), parent.getContext(), parent);
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        holder.onBind(listener, entries);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return dashboardViewHolderLayouts.size();
    }

    static class DashboardCalendarViewHolder extends DashboardViewHolder {

        @BindView(R.id.dashboard_mcv) MaterialCalendarView dashboardCalendarView;
        @BindView(R.id.today_imageview) ImageView goToTodayImageView;

        @BindString(R.string.popup_today) String popupTodayString;

        @BindColor(R.color.darkgray) int darkGrayColour;
        @BindColor(R.color.red) int redColour;
        @BindColor(R.color.orange) int orangeColour;
        @BindColor(R.color.green) int greenColour;
        @BindColor(R.color.white) int whiteColour;

        public DashboardCalendarViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            RxView.clicks(goToTodayImageView)
                    .subscribe(aVoid -> {
                        dashboardCalendarView.setCurrentDate(CalendarDay.today(), true);
                    });
            RxView.longClicks(goToTodayImageView)
                    .subscribe(aVoid -> {
                        Toast.makeText(goToTodayImageView.getContext(), popupTodayString, Toast.LENGTH_SHORT)
                                .show();
                    });
        }

        private void setMinMaxDates(List<CalendarDay> dates) {
            Collections.sort(dates, (o1, o2) -> o1.toString().compareTo(o2.toString()));

            Calendar minDate = Calendar.getInstance();
            Calendar maxDate = Calendar.getInstance();
            if (!dates.isEmpty()) {
                minDate = CalendarDay.from(dates.get(0).getCalendar()).getCalendar();
                maxDate = CalendarDay.from(dates.get(dates.size()-1).getCalendar()).getCalendar();
            }

            minDate.add(Calendar.MONTH, -1);
            minDate.set(Calendar.DAY_OF_MONTH, minDate.getActualMinimum(Calendar.DAY_OF_MONTH));
            maxDate.add(Calendar.MONTH, 1);
            maxDate.set(Calendar.DAY_OF_MONTH, maxDate.getActualMaximum(Calendar.DAY_OF_MONTH));

            dashboardCalendarView.state()
                    .edit()
                    .setMinimumDate(CalendarDay.from(minDate))
                    .setMaximumDate(CalendarDay.from(maxDate))
                    .commit();
        }

        private void setDatesDecorators(final Map<Entry, CalendarDay> entryDays) {
            List<CalendarDay> unstartedDays = new ArrayList<>();
            List<CalendarDay> incompleteDays = new ArrayList<>();
            List<CalendarDay> completeDays = new ArrayList<>();

            for (Map.Entry<Entry, CalendarDay> entryDay: entryDays.entrySet()) {
                long percentageComplete = Entry.complete(entryDay.getKey());
                CalendarDay day = entryDay.getValue();
                if (day != null) {
                    if (percentageComplete < 33) {
                        unstartedDays.add(day);
                    } else if (percentageComplete < 100) {
                        incompleteDays.add(day);
                    } else {
                        completeDays.add(day);
                    }
                }
            }

            dashboardCalendarView.removeDecorators();
            dashboardCalendarView.addDecorator(new ColorCircleDayViewDecorator(whiteColour, darkGrayColour, Collections.singleton(CalendarDay.today())));
            dashboardCalendarView.addDecorator(new ColorCircleDayViewDecorator(whiteColour, redColour, unstartedDays));
            dashboardCalendarView.addDecorator(new ColorCircleDayViewDecorator(whiteColour, orangeColour, incompleteDays));
            dashboardCalendarView.addDecorator(new ColorCircleDayViewDecorator(whiteColour, greenColour, completeDays));
        }

        @Override
        public void onBind(DashboardListener listener, List<Entry> entries) {
            Map<Entry, CalendarDay> entryDays = new HashMap<>();

            for (Entry entry: entries) {
                entryDays.put(entry, EntryDateFormatter.parse(entry.date));
            }

            setMinMaxDates(new ArrayList<>(entryDays.values()));
            setDatesDecorators(entryDays);

            dashboardCalendarView.setCurrentDate(CalendarDay.today(), true);
            dashboardCalendarView.setOnDateChangedListener((widget, day, selected) -> listener.onDateSelected(day));
        }
    }

    static class DashboardStatsViewHolder extends DashboardViewHolder {

        @BindView(R.id.stats_current_streak) TextView currentStreakTextView;
        @BindView(R.id.stats_longest_streak) TextView longestStreakTextView;
        @BindView(R.id.stats_created_completed) TextView createdCompletedRatioTextView;

        @BindString(R.string.dashboard_stats_days_completed) String statsDaysCompletedString;
        @BindString(R.string.dashboard_stats_current_streak) String statsCurrentStreakString;
        @BindString(R.string.dashboard_stats_longest_streak) String statsLongestStreakString;
        @BindString(R.string.dashboard_stats_day) String statsDayString;
        @BindString(R.string.dashboard_stats_days) String statsDaysString;

        @BindColor(R.color.green) int greenColour;
        @BindColor(R.color.blue) int blueColour;

        public DashboardStatsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(DashboardListener listener, List<Entry> entries) {
            List<CalendarDay> days = new ArrayList<>();
            for (Entry entry: entries) {
                days.add(EntryDateFormatter.parse(entry));
            }

            Collections.sort(days, (o1, o2) -> o1.toString().compareTo(o2.toString()));
            List<List<CalendarDay>> streaks = CalendarUtil.getStreaks(days);

            int longestStreak = 0;
            int currentStreak = 0;
            for (List<CalendarDay> streak: streaks) {
                longestStreak = Math.max(longestStreak, streak.size());
                if (streak.contains(CalendarDay.today())) {
                    currentStreak = streak.size();
                }
            }

            currentStreakTextView.setText(new SpannableStringBuilder()
                    .append(statsCurrentStreakString)
                    .append(" ")
                    .append(String.valueOf(currentStreak), new ForegroundColorSpan(blueColour), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    .append(" ")
                    .append(currentStreak == 1 ? statsDayString : statsDaysString));

            longestStreakTextView.setText(new SpannableStringBuilder()
                    .append(statsLongestStreakString)
                    .append(" ")
                    .append(String.valueOf(longestStreak), new ForegroundColorSpan(blueColour), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    .append(" ")
                    .append(longestStreak == 1 ? statsDayString : statsDaysString));

            int completed = 0;
            for (Entry entry: entries) {
                if (Entry.complete(entry) == 100) {
                    completed += 1;
                }
            }

            createdCompletedRatioTextView.setText(new SpannableStringBuilder()
                    .append(statsDaysCompletedString)
                    .append(" ")
                    .append(String.format(Locale.getDefault(), "%d / %d", completed, entries.size()), new ForegroundColorSpan(completed == entries.size() ? greenColour : blueColour), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    .append(" ")
                    .append(completed == 1 ? statsDayString : statsDaysString));
        }
    }

    static class DashboardInfoViewHolder extends DashboardViewHolder {

        public DashboardInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(DashboardListener listener, List<Entry> entries) {

        }
    }

    static abstract class DashboardViewHolder extends RecyclerViewHolder {

        DashboardViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void onBind(DashboardListener listener, List<Entry> entries);
    }

    public interface DashboardListener {
        void onDateSelected(CalendarDay day);
    }
}
