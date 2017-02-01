package ai.victorl.toda.screens.addeditentry;

import android.support.annotation.NonNull;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.List;

import ai.victorl.toda.data.entry.Entry;
import ai.victorl.toda.data.entry.EntryDateFormatter;
import ai.victorl.toda.data.entry.source.EntryDataSource;
import rx.Observable;
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
                        currentEntry = new Entry(currentEntry.date);
                    } else {
                        currentEntry = entry;
                    }
                    entryView.showEntry(currentEntry);
                });
    }

    @Override
    public void save(boolean showUi) {
        entryDataSource.saveEntry(currentEntry);
        if (showUi) {
            entryView.showChangesSaved();
        }
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

    @Override
    public Observable<Entry> getEntry() {
        return Observable.just(currentEntry);
    }

    @Override
    public void setEntryJournal(String journal) {
        currentEntry.journal = journal;
        entryView.updateStatus(Entry.complete(currentEntry));
    }

    @Override
    public void setEntryGratitudes(List<String> gratitudes) {
        currentEntry.gratitudes = new ArrayList<>(gratitudes);
        entryView.updateStatus(Entry.complete(currentEntry));
    }

    @Override
    public void addEntryGratitude(String gratitude) {
        currentEntry.gratitudes.add(gratitude);
        entryView.updateStatus(Entry.complete(currentEntry));
    }

    @Override
    public void setEntryGratitude(int index, String gratitude) {
        if (0 <= index && index < currentEntry.gratitudes.size()) {
            currentEntry.gratitudes.set(index, gratitude);
            entryView.updateStatus(Entry.complete(currentEntry));
        }
    }

    @Override
    public void removeEntryGratitudes(int index) {
        if (index > 0) {
            currentEntry.gratitudes.remove(index);
            entryView.updateStatus(Entry.complete(currentEntry));
        }
    }

    @Override
    public void setEntryExercise(String exercise) {
        currentEntry.exercise = exercise;
        entryView.updateStatus(Entry.complete(currentEntry));
    }

    @Override
    public void setEntryMeditation(String meditation) {
        currentEntry.meditation = meditation;
        entryView.updateStatus(Entry.complete(currentEntry));
    }

    @Override
    public void setEntryKindness(String kindness) {
        currentEntry.kindness = kindness;
        entryView.updateStatus(Entry.complete(currentEntry));
    }
}
