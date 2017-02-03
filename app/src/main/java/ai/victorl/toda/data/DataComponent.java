package ai.victorl.toda.data;

import javax.inject.Singleton;

import ai.victorl.toda.app.AppComponent;
import ai.victorl.toda.data.auth.AuthModule;
import ai.victorl.toda.data.entry.source.EntryRepositoryModule;
import ai.victorl.toda.data.settings.SettingsModule;
import dagger.Component;

@Singleton
@Component(
        modules = {
                SettingsModule.class,
                AuthModule.class,
                EntryRepositoryModule.class,
        },
        dependencies = {
                AppComponent.class,
        }
)
public interface DataComponent extends
        SettingsModule.Graph,
        AuthModule.Graph,
        EntryRepositoryModule.Graph {
}
