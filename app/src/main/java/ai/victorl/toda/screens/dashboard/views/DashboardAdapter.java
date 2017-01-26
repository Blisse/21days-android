package ai.victorl.toda.screens.dashboard.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ai.victorl.toda.R;
import ai.victorl.toda.data.entry.Entry;
import ai.victorl.toda.data.entry.EntryDateFormatter;
import ai.victorl.toda.screens.addeditentry.AddEditEntryActivity;
import ai.victorl.toda.ui.RecyclerViewHolder;
import ai.victorl.toda.ui.RecyclerViewHolderLayout;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardAdapter<T extends DashboardAdapter.DashboardViewHolder> extends RecyclerView.Adapter<T> {

    private final List<RecyclerViewHolderLayout<T>> dashboardViewHolderLayouts = Arrays.asList(
            new RecyclerViewHolderLayout<T>(R.layout.item_dashboard_calendar, (Class<T>) DashboardCalendarViewHolder.class),
            new RecyclerViewHolderLayout<T>(R.layout.item_dashboard_stats, (Class<T>) DashboardStatsViewHolder.class)
    );

    private final Context context;

    public DashboardAdapter(Context context) {
        this.context = context;
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
        holder.onBind(context, entries);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return dashboardViewHolderLayouts.size();
    }

    public static class DashboardCalendarViewHolder extends DashboardViewHolder {

        @BindView(R.id.dashboard_mcv) MaterialCalendarView dashboardCalendarView;
        @BindColor(R.color.redpink) int redColour;
        @BindColor(R.color.orange) int orangeColour;
        @BindColor(R.color.green) int greenColour;
        @BindColor(R.color.blue) int blueColour;

        public DashboardCalendarViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setMinMaxDates(List<CalendarDay> dates) {
            Collections.sort(dates, new Comparator<CalendarDay>() {
                @Override
                public int compare(CalendarDay o1, CalendarDay o2) {
                    return o1.toString().compareTo(o2.toString());
                }
            });

            Calendar minDate = Calendar.getInstance();
            Calendar maxDate = Calendar.getInstance();
            if (!dates.isEmpty()) {
                minDate = dates.get(0).getCalendar();
                maxDate = dates.get(dates.size() - 1).getCalendar();
            }

            minDate.add(Calendar.MONTH, -1);
            maxDate.add(Calendar.MONTH, 1);

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
                long percentageComplete = entryDay.getKey().percentageComplete();
                CalendarDay day = entryDay.getValue();
                if (day != null) {
                    if (percentageComplete < 20) {
                        unstartedDays.add(day);
                    } else if (percentageComplete < 100) {
                        incompleteDays.add(day);
                    } else {
                        completeDays.add(day);
                    }
                }
            }

            dashboardCalendarView.removeDecorators();
            dashboardCalendarView.addDecorator(new EntriesDecorator(redColour, unstartedDays));
            dashboardCalendarView.addDecorator(new EntriesDecorator(orangeColour, incompleteDays));
            dashboardCalendarView.addDecorator(new EntriesDecorator(greenColour, completeDays));
        }

        @Override
        public void onBind(final Context context, List<Entry> entries) {
            Map<Entry, CalendarDay> entryDays = new HashMap<>();

            for (Entry entry: entries) {
                entryDays.put(entry, EntryDateFormatter.parse(entry.date));
            }

            setMinMaxDates(new ArrayList<>(entryDays.values()));
            setDatesDecorators(entryDays);

            dashboardCalendarView.setOnDateChangedListener((widget, day, selected) -> AddEditEntryActivity.start(context, day));
        }
    }

    private static class DashboardStatsViewHolder extends DashboardViewHolder {

        public DashboardStatsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(Context context, List<Entry> entries) {

        }
    }

    static abstract class DashboardViewHolder extends RecyclerViewHolder {

        DashboardViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void onBind(final Context context, List<Entry> entries);
    }
}
