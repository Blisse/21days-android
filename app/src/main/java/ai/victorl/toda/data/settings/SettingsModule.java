package ai.victorl.toda.data.settings;

import ai.victorl.toda.data.store.LocalKeyStore;
import dagger.Module;
import dagger.Provides;

@Module
public class SettingsModule {
    @Provides
    TodaSettings provideTodaSettings(LocalKeyStore localKeyStore) {
        return new SettingsStore(localKeyStore);
    }

    public interface Graph {
        TodaSettings todaSettings();
    }
}
