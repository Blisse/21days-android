package ai.victorl.toda.data;

import javax.inject.Singleton;

import ai.victorl.toda.app.AppComponent;
import ai.victorl.toda.data.entry.source.EntryRepositoryModule;
import ai.victorl.toda.data.store.KeyStoreModule;
import dagger.Component;

@Singleton
@Component(
        modules = {
                KeyStoreModule.class,
                EntryRepositoryModule.class,
        },
        dependencies = {
                AppComponent.class,
        }
)
public interface DataComponent extends
        KeyStoreModule.Graph,
        EntryRepositoryModule.Graph {
}
