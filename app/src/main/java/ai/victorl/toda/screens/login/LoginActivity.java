package ai.victorl.toda.screens.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.common.SignInButton;

import javax.inject.Inject;

import ai.victorl.toda.R;
import ai.victorl.toda.app.TodaApp;
import ai.victorl.toda.data.auth.GoogleSignInClient;
import ai.victorl.toda.screens.dashboard.DashboardActivity;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    @BindString(R.string.login_firebase_failed) String loginFirebaseFailedString;
    @BindString(R.string.login_google_error_resolved) String loginGoogleErrorResolvedString;
    @BindString(R.string.login_google_failed) String loginGoogleFailedString;

    @BindView(R.id.coordinator) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.google_signinbutton) SignInButton googleSignInButton;

    @Inject LoginContract.Presenter loginPresenter;
    @Inject GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        DaggerLoginComponent.builder()
                .dataComponent(TodaApp.from(this).getDataComponent())
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);

        setSupportActionBar(toolbar);

        googleSignInButton.setSize(SignInButton.SIZE_STANDARD);
        googleSignInButton.setOnClickListener(v -> {
            googleSignInClient.startGoogleSignInActivityForResult(this);
        });

        googleSignInClient.bind(this, new GoogleSignInClient.SignInStatusListener() {
            @Override
            public void onGoogleServicesResolved() {
                Snackbar.make(coordinatorLayout, loginGoogleErrorResolvedString, Snackbar.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onSignInFailed() {
                Snackbar.make(coordinatorLayout, loginGoogleFailedString, Snackbar.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onSignInSucceeded(String accountIdToken) {
                loginPresenter.signInWithIdToken(accountIdToken);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginPresenter.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        loginPresenter.unsubscribe();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        googleSignInClient.onResult(requestCode, resultCode, data);
    }

    @Override
    public void goToDashboard() {
        DashboardActivity.startActivity(this);
    }

    @Override
    public void showFirebaseLoginFailed() {
        Snackbar.make(coordinatorLayout, loginFirebaseFailedString, Snackbar.LENGTH_LONG)
                .show();
    }

    public static void startActivity(AppCompatActivity activity) {
        Intent loginIntent = new Intent(activity, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(loginIntent);
    }
}

