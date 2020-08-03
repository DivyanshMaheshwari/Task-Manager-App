package com.example.task_manager_app;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.LazyLoader;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.os.AsyncTask;

public class Login extends AppCompatActivity {
    Button callSignUp, login_btn,forgotpass;
    TextInputLayout username, password;
    TextInputEditText passwordedit, usernameedit;
    String usernamefromedittext, passwordfromedt;
    FirebaseAuth mauth;
    FirebaseUser CurrentUser;
    LottieAnimationView lottieanimationview;
    ProgressDialog progressDialog;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callSignUp = findViewById(R.id.callSignUp);

        forgotpass=findViewById(R.id.forgotpass);

//        lottieanimationview=findViewById(R.id.progressAnimationView);


        passwordedit = findViewById(R.id.PasswordEditText);
        usernameedit = findViewById(R.id.Login);
        login_btn = findViewById(R.id.login_btn);
        callSignUp = findViewById(R.id.callSignUp);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        mauth = FirebaseAuth.getInstance();
        CurrentUser = mauth.getCurrentUser();

//        final Loading_Dialog dialog=new Loading_Dialog();


        onStart();

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email =usernameedit.getText().toString();
                if(!(email.isEmpty()))
                {
                    mauth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Login.this, "Reset Link Send to " + email, Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                }

                else {
                    username.setError("Email Needed");
                }
            }
        });

        loading();

        login_btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        usernamefromedittext = usernameedit.getText().toString();
                        passwordfromedt = passwordedit.getText().toString();

                        if ((usernamefromedittext.isEmpty())) {


                            username.setError("Username Needed");

                        }
                        if ((passwordfromedt.isEmpty())) {
                            password.setError("Password Needed");
                        }

                        if ((!(usernamefromedittext.isEmpty()) && (!(passwordfromedt.isEmpty())))) {
                            progressDialog.show();

                            mauth.signInWithEmailAndPassword(usernamefromedittext, passwordfromedt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        if (mauth.getCurrentUser().isEmailVerified()) {
                                             progressDialog.dismiss();
                                            Intent i = new Intent(Login.this, MainActivity.class);
                                            startActivity(i);
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(Login.this, "Please Verify Email ", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                      progressDialog.dismiss();
                                        Toast.makeText(Login.this, "Please enter the right credential", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    }
                });
        callSignUp.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Intent intent = new Intent(Login.this, Signup.class);
        startActivity(intent);
        }

        });


    }


    public void onStart() {
        super.onStart();

        if (CurrentUser != null) {
            if (CurrentUser.isEmailVerified()) {
                Intent i = new Intent(Login.this, MainActivity.class);
                startActivity(i);
            }

        }
    }
    public void loading(){

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Welcome !!");
        progressDialog.setProgress(10);
        progressDialog.setMax(100);
        progressDialog.setMessage("Loading...");
        new MyTask().execute();

    }
    public static class MyTask extends AsyncTask<Void, Void, Void> {
        public void onPreExecute() {

        }
        public Void doInBackground(Void... unused) {
            return null;
        }
    }

}
