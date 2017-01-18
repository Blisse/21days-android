package ai.victorl.toda.screens.entry;

import ai.victorl.toda.data.entry.source.EntryDataSource;

public class AddEditEntryPresenter implements AddEditEntryContract.Presenter {

    private final AddEditEntryContract.View entryView;
    private final String entryDate;
    private final EntryDataSource entryDataSource;

    public AddEditEntryPresenter(AddEditEntryContract.View entryView, EntryDataSource entryDataSource, String entryDate) {
        this.entryView = entryView;
        this.entryDataSource = entryDataSource;
        this.entryDate = entryDate;
    }

    @Override
    public void start() {
        load();
    }

    @Override
    public void stop() {

    }

    @Override
    public void load() {

    }

    @Override
    public void save() {

    }
}
