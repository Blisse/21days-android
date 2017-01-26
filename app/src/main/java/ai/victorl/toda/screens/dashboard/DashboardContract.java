package ai.victorl.toda.screens.dashboard;

import java.util.List;

import ai.victorl.toda.data.entry.Entry;
import ai.victorl.toda.ui.BasePresenter;
import ai.victorl.toda.ui.BaseView;

public interface DashboardContract {

    interface View extends BaseView<Presenter> {

        void showEntries(List<Entry> entries);

    }

    interface Presenter extends BasePresenter {

        void load();

    }
}
