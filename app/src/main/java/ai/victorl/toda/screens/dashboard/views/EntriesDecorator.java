package ai.victorl.toda.screens.dashboard.views;

import android.graphics.PorterDuff;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

class EntriesDecorator implements DayViewDecorator {
    private final int color;
    private final Set<CalendarDay> days;

    EntriesDecorator(int color, Collection<CalendarDay> days) {
        this.color = color;
        this.days = new HashSet<>(days);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return days.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        if (!view.areDaysDisabled()) {
            ShapeDrawable circleDrawable = new ShapeDrawable(new OvalShape());
            circleDrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP );
            view.setBackgroundDrawable(circleDrawable);
        }
    }
}
