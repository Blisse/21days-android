package ai.victorl.toda.screens.addeditentry;

import java.util.List;

import ai.victorl.toda.data.entry.Entry;
import ai.victorl.toda.ui.BasePresenter;
import ai.victorl.toda.ui.BaseView;
import rx.Observable;

public interface AddEditEntryContract {

    interface View extends BaseView<Presenter> {

        void showEntry(Entry entry);

        void setTitle(String title);

        void updateStatus(int status);

        void showChangesSaved();

        void returnToDashboardAsCancelled();

        void returnToDashboardAsSaved();

        void returnToDashboardAsDeleted();
    }

    interface Presenter extends BasePresenter {

        void load(String entryDate);

        void save(boolean showUi);

        void cancel();

        void delete();

        Observable<Entry> getEntry();

        void setEntryJournal(String journal);

        void setEntryGratitudes(List<String> gratitudes);

        void addEntryGratitude(String gratitude);

        void setEntryGratitude(int index, String gratitude);

        void removeEntryGratitudes(int index);

        void setEntryExercise(String exercise);

        void setEntryMeditation(String meditation);

        void setEntryKindness(String kindness);
    }

}
