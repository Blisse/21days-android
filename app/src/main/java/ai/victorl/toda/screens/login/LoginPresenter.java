package ai.victorl.toda.screens.login;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginPresenter implements LoginContract.Presenter {

    private final LoginContract.View loginView;

    private FirebaseAuth.AuthStateListener authStateListener;

    public LoginPresenter(LoginContract.View loginView) {
        this.loginView = loginView;

        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                loginView.goToDashboard();
            }
        };
    }

    @Override
    public void subscribe() {
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
    }

    @Override
    public void unsubscribe() {
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
    }

    @Override
    public void signInWithIdToken(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        loginView.showFirebaseLoginFailed();
                    }
                });
    }
}
