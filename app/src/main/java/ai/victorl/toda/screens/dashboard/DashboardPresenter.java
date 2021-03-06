package ai.victorl.toda.screens.dashboard;

import com.google.firebase.auth.FirebaseAuth;
import com.kelvinapps.rxfirebase.RxFirebaseAuth;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import ai.victorl.toda.data.entry.source.EntryDataSource;
import ai.victorl.toda.screens.addeditentry.AddEditEntryActivity;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class DashboardPresenter implements DashboardContract.Presenter {

    private final EntryDataSource entryDataSource;
    private final DashboardContract.View dashboardView;

    private CalendarDay selectedDay;
    private Subscription authStateSubscription;

    DashboardPresenter(EntryDataSource entryDataSource, DashboardContract.View dashboardView) {
        this.entryDataSource = entryDataSource;
        this.dashboardView = dashboardView;
    }

    @Override
    public void subscribe() {
        authStateSubscription = RxFirebaseAuth.observeAuthState(FirebaseAuth.getInstance())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(firebaseUser -> {
                    if (firebaseUser != null) {
                        dashboardView.showFirebaseUser(firebaseUser.getDisplayName(), firebaseUser.getEmail(), firebaseUser.getPhotoUrl());
                    } else {
                        dashboardView.goToLogin();
                    }
                });
    }

    @Override
    public void unsubscribe() {
        if (authStateSubscription != null && !authStateSubscription.isUnsubscribed()) {
            authStateSubscription.unsubscribe();
        }
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
            } else if (resultCode == AddEditEntryActivity.RESULT_ADD_EDIT_CANCELLED) {
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

    @Override
    public void setAutosave(boolean autosave) {

    }

    @Override
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }
}
