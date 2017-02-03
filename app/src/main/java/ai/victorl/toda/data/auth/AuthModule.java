package ai.victorl.toda.data.auth;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AuthModule {

    @Singleton
    @Provides
    GoogleSignInClient provideGoogleSignInClient() {
        return new GoogleSignInClient();
    }

    public interface Graph {
        GoogleSignInClient googleSignInClient();
    }
}
