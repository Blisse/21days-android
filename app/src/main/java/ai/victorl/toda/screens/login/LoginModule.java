package ai.victorl.toda.screens.login;

import dagger.Module;
import dagger.Provides;

@Module
class LoginModule {
    private final LoginContract.View loginView;

    LoginModule(LoginContract.View dashboardView) {
        this.loginView = dashboardView;
    }

    @Provides
    LoginContract.View provideLoginView() {
        return loginView;
    }

    @Provides
    LoginContract.Presenter provideLoginPresenter(LoginContract.View loginView) {
        return new LoginPresenter(loginView);
    }

    interface Graph {
        LoginContract.View view();
        LoginContract.Presenter presenter();
    }
}
