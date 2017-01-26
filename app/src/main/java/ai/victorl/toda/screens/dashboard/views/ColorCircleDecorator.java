package ai.victorl.toda.screens.dashboard.views;

import android.graphics.PorterDuff;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

class ColorCircleDecorator implements DayViewDecorator {
    private final int foregroundColour;
    private final int backgroundColour;
    private final Set<CalendarDay> days;

    ColorCircleDecorator(int foregroundColour, int backgroundColour, Collection<CalendarDay> days) {
        this.foregroundColour = foregroundColour;
        this.backgroundColour = backgroundColour;
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
            circleDrawable.setColorFilter(backgroundColour, PorterDuff.Mode.SRC_ATOP );
            view.setBackgroundDrawable(circleDrawable);
            view.addSpan(new ForegroundColorSpan(foregroundColour));
        }
    }
}
