package ai.victorl.toda.screens.entry;

import ai.victorl.toda.data.DataComponent;
import ai.victorl.toda.utils.di.FragmentScope;
import dagger.Component;

@FragmentScope
@Component(
        dependencies = {
                DataComponent.class,
        },
        modules = {
                AddEditEntryModule.class,
        }
)
public interface AddEditEntryComponent extends AddEditEntryModule.Graph {
    void inject(AddEditEntryActivity target);
}
