package ai.victorl.toda.screens.dashboard;

import android.net.Uri;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;

import ai.victorl.toda.data.entry.Entry;
import ai.victorl.toda.ui.BasePresenter;
import ai.victorl.toda.ui.BaseView;

public interface DashboardContract {

    interface View extends BaseView<Presenter> {

        void showEntries(List<Entry> entries);

        void goToAddEdit(CalendarDay day);

        void goToLogin();

        void showFirebaseUser(String name, String email, Uri photo);

        void showChangesSaved(CalendarDay day);

        void showChangesCancelled(CalendarDay day);

        void showEntryDeleted(CalendarDay day);
    }

    interface Presenter extends BasePresenter {

        void load();

        void result(int requestCode, int resultCode);

        void selectDay(CalendarDay day);

        void setAutosave(boolean autosave);

        void signOut();

    }
}
