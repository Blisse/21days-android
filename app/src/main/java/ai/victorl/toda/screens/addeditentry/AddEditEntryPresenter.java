package ai.victorl.toda.screens.addeditentry;

import android.support.annotation.NonNull;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import ai.victorl.toda.data.entry.Entry;
import ai.victorl.toda.data.entry.EntryDateFormatter;
import ai.victorl.toda.data.entry.source.EntryDataSource;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class AddEditEntryPresenter implements AddEditEntryContract.Presenter {

    private final EntryDataSource entryDataSource;
    private final AddEditEntryContract.View entryView;

    private Entry currentEntry;

    AddEditEntryPresenter(EntryDataSource entryDataSource, AddEditEntryContract.View entryView) {
        this.entryDataSource = entryDataSource;
        this.entryView = entryView;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void load(@NonNull final String entryDate) {
        CalendarDay day = EntryDateFormatter.parse(entryDate);
        entryView.setTitle(EntryDateFormatter.readableFormat(day));
        entryDataSource.getEntry(entryDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(entry -> {
                    if (entry == null) {
                        currentEntry = new Entry(entryDate);
                    } else {
                        currentEntry = entry;
                    }

                    entryView.showEntry(currentEntry);
                });
    }

    @Override
    public void save() {
        entryDataSource.saveEntry(currentEntry);
        entryView.showChangesSaved();
    }

    @Override
    public void cancel() {
        entryView.returnToDashboardAsCancelled();
    }

    @Override
    public void delete() {
        entryDataSource.deleteEntry(currentEntry);
        entryView.returnToDashboardAsDeleted();
    }
}
