package ai.victorl.toda.screens.dashboard;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;

import ai.victorl.toda.data.entry.Entry;
import ai.victorl.toda.ui.BasePresenter;
import ai.victorl.toda.ui.BaseView;

public interface DashboardContract {

    interface View extends BaseView<Presenter> {

        void showEntries(List<Entry> entries);

        void showAddEditTask(CalendarDay day);

    }

    interface Presenter extends BasePresenter {

        void load();

        void result(int requestCode, int resultCode);

    }
}
