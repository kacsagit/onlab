package com.example.kata.onlab.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import io.realm.Realm;

public class LoginActivity extends AppCompatActivity implements LoginScreen {

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d("Login", loginResult.getAccessToken().getToken());
                TextView info = (TextView)findViewById(R.id.info);
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
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //facebook
        if ( FacebookSdk.isFacebookRequestCode(requestCode)){
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        /*if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }*/
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
}
