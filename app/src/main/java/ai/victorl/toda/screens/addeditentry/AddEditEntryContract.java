package ai.victorl.toda.screens.addeditentry;

import ai.victorl.toda.data.entry.Entry;
import ai.victorl.toda.ui.BasePresenter;
import ai.victorl.toda.ui.BaseView;

public interface AddEditEntryContract {

    interface View extends BaseView<Presenter> {

        void showEntry(Entry entry);

        void setTitle(String title);

        void showChangesSaved();

        void returnToDashboardAsCancelled();

        void returnToDashboardAsSaved();

        void returnToDashboardAsDeleted();
    }

    interface Presenter extends BasePresenter {

        void load(String entryDate);

        void save();

        void cancel();

        void delete();

    }

}
