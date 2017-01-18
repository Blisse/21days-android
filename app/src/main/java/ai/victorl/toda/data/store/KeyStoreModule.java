package ai.victorl.toda.data.store;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class KeyStoreModule {

    @Singleton
    @Provides
    public LocalKeyStore provideHawkStore(Context context) {
        return new HawkKeyStore(context);
    }

    public interface Graph {
        LocalKeyStore localKeyStore();
    }
}
