package ai.victorl.toda.screens.entry;

import ai.victorl.toda.data.entry.source.EntryDataSource;
import dagger.Module;
import dagger.Provides;

@Module
public class AddEditEntryModule {

    private final String entryDate;
    private final AddEditEntryContract.View view;

    public AddEditEntryModule(String entryDate, AddEditEntryContract.View view) {
        this.entryDate = entryDate;
        this.view = view;
    }

    @Provides
    AddEditEntryContract.View provideEntryContractView() {
        return view;
    }

    @Provides
    AddEditEntryContract.Presenter provideEntryContractPresenter(EntryDataSource entryDataSource) {
        return new AddEditEntryPresenter(view, entryDataSource, entryDate);
    }

    @Provides
    AddEditEntryAdapter provideEntryAdapter(AddEditEntryContract.Presenter presenter) {
        return new AddEditEntryAdapter(presenter);
    }

    interface Graph {
        AddEditEntryContract.View view();
        AddEditEntryContract.Presenter presenter();
        AddEditEntryAdapter entryAdapter();
    }
}
