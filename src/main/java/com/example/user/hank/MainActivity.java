package com.example.user.hank;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    private Button login;
    private Button signUp;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            setContentView(R.layout.activity_main);
            login = (Button) findViewById(R.id.login);
            signUp = (Button) findViewById(R.id.sign_up);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                }
            });
            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, SignUpActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        } else{
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, Main2Activity.class);
            startActivity(intent);
            finish();
        }

    }
}