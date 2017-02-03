package ai.victorl.toda.data.entry.source;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class EntryRepositoryModule {

    @Singleton
    @Provides
    EntryDataSource provideEntryDataSource() {
        return new FirebaseEntryDatabase();
    }

    public interface Graph {
        EntryDataSource entryDataSource();
    }
}
