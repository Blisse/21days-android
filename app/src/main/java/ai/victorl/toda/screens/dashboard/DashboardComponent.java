package ai.victorl.toda.screens.dashboard;

import ai.victorl.toda.data.DataComponent;
import ai.victorl.toda.utils.di.ActivityScope;
import dagger.Component;

@ActivityScope
@Component(
        dependencies = {
                DataComponent.class,
        },
        modules = {
                DashboardModule.class,
        }
)
interface DashboardComponent extends DashboardModule.Graph {
    void inject(DashboardActivity target);
}
