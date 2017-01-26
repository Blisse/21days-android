package ai.victorl.toda.screens.dashboard;

import ai.victorl.toda.data.entry.source.EntryDataSource;
import dagger.Module;
import dagger.Provides;

@Module
class DashboardModule {

    private final DashboardContract.View dashboardView;

    DashboardModule(DashboardContract.View dashboardView) {
        this.dashboardView = dashboardView;
    }

    @Provides
    DashboardContract.View provideDashboardView() {
        return dashboardView;
    }

    @Provides
    DashboardContract.Presenter provideDashboardPresenter(EntryDataSource entryDataSource, DashboardContract.View dashboardView) {
        return new DashboardPresenter(entryDataSource, dashboardView);
    }

    interface Graph {
        DashboardContract.View view();
        DashboardContract.Presenter presenter();
    }
}
