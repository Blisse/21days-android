package ai.victorl.toda.screens.addeditentry;

import ai.victorl.toda.data.entry.source.EntryDataSource;
import dagger.Module;
import dagger.Provides;

@Module
class AddEditEntryModule {

    private final AddEditEntryContract.View entryView;

    AddEditEntryModule(AddEditEntryContract.View entryView) {
        this.entryView = entryView;
    }

    @Provides
    AddEditEntryContract.View provideEntryContractView() {
        return entryView;
    }

    @Provides
    AddEditEntryContract.Presenter provideEntryContractPresenter(EntryDataSource entryDataSource) {
        return new AddEditEntryPresenter(entryDataSource, entryView);
    }

    interface Graph {
        AddEditEntryContract.View view();
        AddEditEntryContract.Presenter presenter();
    }
}
