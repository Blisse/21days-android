package ai.victorl.toda.data.auth;

import android.content.Intent;
import android.content.IntentSender;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;

import ai.victorl.toda.BuildConfig;

import static android.app.Activity.RESULT_OK;

public class GoogleSignInClient {
    private static final int REQUEST_SIGN_IN = 1001;
    private static final int REQUEST_RESOLVE_CONNECTION_REQUEST = 1002;

    private GoogleSignInOptions googleSignInOptions;
    private GoogleApiClient googleApiClient;
    private SignInStatusListener listener;

    public GoogleSignInClient() {
    }

    public void bind(FragmentActivity activity, SignInStatusListener listener) {
        this.listener = listener;

        GoogleApiClient.OnConnectionFailedListener connectionFailedListener = connectionResult -> {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(activity, REQUEST_RESOLVE_CONNECTION_REQUEST);
                } catch (IntentSender.SendIntentException e) {
                    GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), activity, 0)
                            .show();
                }
            } else {
                GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), activity, 0)
                        .show();
            }
        };

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.GOOGLE_WEB_APP_CLIENT_ID)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(activity, connectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    public void startGoogleSignInActivityForResult(AppCompatActivity activity) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        activity.startActivityForResult(signInIntent, REQUEST_SIGN_IN);
    }

    public void onResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else if (requestCode == REQUEST_RESOLVE_CONNECTION_REQUEST) {
            if (resultCode == RESULT_OK) {
                listener.onGoogleServicesResolved();
            }
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            if (account != null) {
                listener.onSignInSucceeded(account.getIdToken());
            }
        } else {
            listener.onSignInFailed();
        }
    }

    public interface SignInStatusListener {

        void onGoogleServicesResolved();

        void onSignInFailed();

        void onSignInSucceeded(String accountIdToken);
    }
}
