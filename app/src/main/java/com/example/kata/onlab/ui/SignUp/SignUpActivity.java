package com.example.kata.onlab.ui.SignUp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kata.onlab.R;
import com.example.kata.onlab.db.Login;
import com.example.kata.onlab.network.NetworkManager;

public class SignUpActivity extends AppCompatActivity implements SignUpScreen{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        final EditText username = (EditText) findViewById(R.id.username);
        final EditText password1 = (EditText) findViewById(R.id.password1);
        final EditText password2 = (EditText) findViewById(R.id.password2);
        Button signUp = (Button) findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password1.getText().toString().equals(password2.getText().toString())) {
                    Login login = new Login(username.getText().toString(), password1.getText().toString());
                    NetworkManager.getInstance().signupData(login);
                }
            }
        });

    }

    @Override
    public void onSignUp() {
        Toast.makeText(this,"SignUp success",Toast.LENGTH_LONG).show();
    }
}
