package ai.victorl.toda.screens.addeditentry;

import ai.victorl.toda.data.DataComponent;
import ai.victorl.toda.utils.di.ActivityScope;
import dagger.Component;

@ActivityScope
@Component(
        dependencies = {
                DataComponent.class,
        },
        modules = {
                AddEditEntryModule.class,
        }
)
interface AddEditEntryComponent extends AddEditEntryModule.Graph {
    void inject(AddEditEntryActivity target);
}
