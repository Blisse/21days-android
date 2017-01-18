package ai.victorl.toda.screens.entry;

import ai.victorl.toda.ui.BasePresenter;
import ai.victorl.toda.ui.BaseView;

public interface AddEditEntryContract {

    interface View extends BaseView<Presenter> {

        void setAddEditEntryAdapter(AddEditEntryAdapter adapter);

    }

    interface Presenter extends BasePresenter {

        void load();

        void save();

    }

}
