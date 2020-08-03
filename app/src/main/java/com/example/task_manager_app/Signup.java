package com.example.task_manager_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {
    Button reg_log_btn;
    TextInputEditText email,name,password;
    TextInputLayout emaillout,namelout,passwordlout;
    Button Register_btn;
    ProgressBar progressbar;
    private String emailstr,passstr,namestr,userUID;

    private FirebaseAuth mauth;
    private FirebaseDatabase rootNode;
    private DatabaseReference myref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        reg_log_btn=findViewById(R.id.reg_log_btn);
        email=findViewById(R.id.email);
        name=findViewById(R.id.fullname);
        password=findViewById(R.id.password);

        emaillout=findViewById(R.id.reg_email);
        namelout=findViewById(R.id.reg_name);
        passwordlout=findViewById(R.id.reg_password);

        Register_btn=findViewById(R.id.go_btn);

        progressbar=findViewById(R.id.progressBar);
//        final Loading_Dialog dialog=new Loading_Dialog().newInstance();
        mauth=FirebaseAuth.getInstance();
        rootNode=FirebaseDatabase.getInstance();
        myref=rootNode.getReference("Users");

        reg_log_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Signup.this,Login.class);
                startActivity(i);
            }
        });

        Register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               loadingregister.setVisibility(View.VISIBLE);
                emailstr=email.getText().toString();
                passstr=password.getText().toString();
                namestr=name.getText().toString();

              if(emailstr.isEmpty()){
                    emaillout.setError("Email Needed");

                }
                if((name.getText().toString().isEmpty())){
                    namelout.setError("Name Needed");

                }
                if((password.getText().toString().isEmpty())) {
                    passwordlout.setError("Password");
                }

                if(!(emailstr.isEmpty()) &&!(namestr.isEmpty())  &&!(passstr.isEmpty())){
                    Toast.makeText(Signup.this, emailstr, Toast.LENGTH_SHORT).show();


                    mauth.createUserWithEmailAndPassword(emailstr,passstr).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if(authResult != null){
                                mauth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(Signup.this, "Verification Link Send", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                                UserProfileChangeRequest updateProfile=new UserProfileChangeRequest.Builder().setDisplayName((name.getText().toString())).build();
                                mauth.getCurrentUser().updateProfile(updateProfile);
                                userUID=mauth.getCurrentUser().getUid();

                                myref.child(namestr).setValue(namestr).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){

                                            myref.child(namestr).child("Email").setValue(emailstr);
                                            myref.child(namestr).child("admin").setValue("0");
                                            myref.child(namestr).child("Name").setValue(namestr).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(Signup.this, "Registered Succesfully !!", Toast.LENGTH_SHORT).show();
//                                                        dialog.dismiss();
                                                        Handler handler=new Handler();

                                                        handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Intent i=new Intent(Signup.this, Login.class);
                                                                startActivity(i);

                                                            }
                                                        },3000);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });


                            }
                        }
                    });


                }

            }

        });
    }
}