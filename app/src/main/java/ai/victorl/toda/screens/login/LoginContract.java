package ai.victorl.toda.screens.login;

import ai.victorl.toda.ui.BasePresenter;
import ai.victorl.toda.ui.BaseView;

public interface LoginContract {

    interface View extends BaseView<Presenter> {

        void goToDashboard();

        void showFirebaseLoginFailed();

    }

    interface Presenter extends BasePresenter {

        void signInWithIdToken(String idToken);

    }
}
