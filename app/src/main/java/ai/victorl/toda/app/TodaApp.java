package ai.victorl.toda.app;

import android.app.Application;
import android.content.Context;

import ai.victorl.toda.data.DaggerDataComponent;
import ai.victorl.toda.data.DataComponent;
import ai.victorl.toda.data.entry.source.EntryRepositoryModule;
import ai.victorl.toda.data.settings.SettingsModule;

public class TodaApp extends Application {
    private AppComponent appComponent;
    private DataComponent dataComponent;

    public AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
        }
        return appComponent;
    }

    public DataComponent getDataComponent() {
        if (dataComponent == null) {
            dataComponent = DaggerDataComponent.builder()
                    .appComponent(getAppComponent())
                    .settingsModule(new SettingsModule())
                    .entryRepositoryModule(new EntryRepositoryModule())
                    .build();
        }
        return dataComponent;
    }

    public static TodaApp from(Context context) {
        return (TodaApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        getAppComponent().inject(this);
    }
}
