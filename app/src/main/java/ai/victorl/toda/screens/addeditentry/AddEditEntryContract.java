package ai.victorl.toda.screens.addeditentry;

import ai.victorl.toda.data.entry.Entry;
import ai.victorl.toda.ui.BasePresenter;
import ai.victorl.toda.ui.BaseView;

public interface AddEditEntryContract {

    interface View extends BaseView<Presenter> {

        void showEntry(Entry entry);

        void setTitle(String title);

    }

    interface Presenter extends BasePresenter {

        void load(String entryDate);

        void save();

    }

}
