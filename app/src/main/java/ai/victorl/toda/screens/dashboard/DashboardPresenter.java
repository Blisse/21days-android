package ai.victorl.toda.screens.dashboard;

import ai.victorl.toda.data.entry.source.EntryDataSource;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class DashboardPresenter implements DashboardContract.Presenter {

    private final EntryDataSource entryDataSource;
    private final DashboardContract.View dashboardView;

    DashboardPresenter(EntryDataSource entryDataSource, DashboardContract.View dashboardView) {
        this.entryDataSource = entryDataSource;
        this.dashboardView = dashboardView;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

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

    }
}
