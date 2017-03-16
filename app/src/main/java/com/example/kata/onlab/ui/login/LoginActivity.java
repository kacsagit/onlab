package com.example.kata.onlab.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kata.onlab.R;
import com.example.kata.onlab.network.LoginData;
import com.example.kata.onlab.network.NetworkManager;
import com.example.kata.onlab.ui.main.MainActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import static com.example.kata.onlab.R.id.sigIn;

public class LoginActivity extends AppCompatActivity implements LoginScreen, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN =2000;
    private static final String TAG ="LoginActivity";
    private CallbackManager callbackManager;
    GoogleApiClient mGoogleApiClient;
    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        info= (TextView)findViewById(R.id.info);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d("Login", loginResult.getAccessToken().getToken());
                Toast.makeText(getApplicationContext(),"Fb Login Success",Toast.LENGTH_LONG);
                info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );

                NetworkManager.getInstance().postToken(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getApplicationContext(),"cancel",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        Button signIn= (Button) findViewById(sigIn);

        final EditText username= (EditText) findViewById(R.id.username);
        final EditText password= (EditText) findViewById(R.id.password);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,username.getText().toString());
                NetworkManager.getInstance().logIn(username.getText().toString(),password.getText().toString());
            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //facebook
        if ( FacebookSdk.isFacebookRequestCode(requestCode)){
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            String idToken = result.getSignInAccount().getIdToken();
            GoogleSignInAccount acct = result.getSignInAccount();
            info.setText(idToken);
            Log.d(TAG,idToken);
            NetworkManager.getInstance().postTokenGoogle(idToken);
            Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_LONG).show();
            // Signed out, show unauthenticated UI.

        }
    }




    @Override
    protected void onStart() {
        super.onStart();
        LoginPresenter.getInstance().attachScreen(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LoginPresenter.getInstance().detachScreen();
    }


    @Override
    public void logedIn(LoginData data) {
        Toast.makeText(this,data.mail,Toast.LENGTH_LONG).show();
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
