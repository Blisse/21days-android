package ai.victorl.toda.screens.addeditentry;

import android.support.annotation.NonNull;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import ai.victorl.toda.data.entry.Entry;
import ai.victorl.toda.data.entry.EntryDateFormatter;
import ai.victorl.toda.data.entry.source.EntryDataSource;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void load(@NonNull final String entryDate) {
        CalendarDay day = EntryDateFormatter.parse(entryDate);

        entryView.setTitle(EntryDateFormatter.readableFormat(day));

        entryDataSource.getEntry(entryDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Entry>() {
                        @Override
                        public void call(Entry entry) {
                            if (entry == null) {
                                currentEntry = new Entry(entryDate);
                            } else {
                                currentEntry = entry;
                            }

                            entryView.showEntry(currentEntry);
                        }
                });
    }

    @Override
    public void save() {
        entryDataSource.saveEntry(currentEntry);
    }
}
