package ai.victorl.toda.data.settings;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class SettingsModule {
    @Provides
    TodaSettings provideTodaSettings(Context context) {
        return new SettingsStore(context);
    }

    public interface Graph {
        TodaSettings todaSettings();
    }
}
