package ai.victorl.toda.screens.login;

import ai.victorl.toda.data.DataComponent;
import ai.victorl.toda.utils.di.ActivityScope;
import dagger.Component;

@ActivityScope
@Component(
        dependencies = {
                DataComponent.class,
        },
        modules = {
                LoginModule.class,
        }
)
public interface LoginComponent extends LoginModule.Graph {
    void inject(LoginActivity target);
}
