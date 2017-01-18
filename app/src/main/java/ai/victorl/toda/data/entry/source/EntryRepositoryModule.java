package ai.victorl.toda.data.entry.source;

import javax.inject.Singleton;

import ai.victorl.toda.data.store.LocalKeyStore;
import dagger.Module;
import dagger.Provides;

@Module
public class EntryRepositoryModule {

    @Singleton
    @Provides
    EntryDataSource provideEntryDataSource(LocalKeyStore localKeyStore) {
        return new EntryRepository(localKeyStore);
    }

    public interface Graph {
        EntryDataSource entryDataSource();
    }
}
