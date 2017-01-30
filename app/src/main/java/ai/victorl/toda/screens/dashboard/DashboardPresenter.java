package ai.victorl.toda.screens.dashboard;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import ai.victorl.toda.data.entry.source.EntryDataSource;
import ai.victorl.toda.screens.addeditentry.AddEditEntryActivity;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class DashboardPresenter implements DashboardContract.Presenter {

    private final EntryDataSource entryDataSource;
    private final DashboardContract.View dashboardView;

    private CalendarDay selectedDay;

    DashboardPresenter(EntryDataSource entryDataSource, DashboardContract.View dashboardView) {
        this.entryDataSource = entryDataSource;
        this.dashboardView = dashboardView;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void load() {
        entryDataSource.getAllEntries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dashboardView::showEntries);
    }

    @Override
    public void result(int requestCode, int resultCode) {
        if (requestCode == AddEditEntryActivity.REQUEST_ADD_EDIT) {
            if (resultCode == AddEditEntryActivity.RESULT_ADD_EDIT_SUCCESS) {
                dashboardView.showChangesSaved(selectedDay);
            } else if (resultCode == AddEditEntryActivity.RESULT_CANCELED) {
                dashboardView.showChangesCancelled(selectedDay);
            } else if (resultCode == AddEditEntryActivity.RESULT_ADD_EDIT_DELETED) {
                dashboardView.showEntryDeleted(selectedDay);
            }
        }
    }

    @Override
    public void selectDay(CalendarDay day) {
        selectedDay = CalendarDay.from(day.getCalendar());
        dashboardView.goToAddEdit(selectedDay);
    }
}
