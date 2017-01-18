package ai.victorl.toda.app;

import dagger.Component;

@Component(modules = {
        AppModule.class,
})
public interface AppComponent extends AppModule.Graph {
    void inject(TodaApp app);
}
