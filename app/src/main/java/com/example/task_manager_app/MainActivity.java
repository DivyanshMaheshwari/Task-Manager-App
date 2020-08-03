package com.example.task_manager_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.LazyLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton mMainAddFab, mAddUserFab, mAddContactFab;
    private TextView signoutText, addProText;
    private Animation mFabOpenAnim, mFabCloseAnim;
    private boolean isOpen;
    private DatabaseReference myref;
    private FirebaseAuth mauth;
    ProNameListAdapter adapter;
    ListView pname;
    public  String currentUser;
    int isadmin;
    private FirebaseDatabase Mydb;
    ProgressDialog progressDialog;

    ArrayList<ProListView> arrayList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loading();

        mMainAddFab = findViewById(R.id.main_add_fab);
        mAddUserFab = findViewById(R.id.add_user_fab);
        mAddContactFab = findViewById(R.id.add_contact_fab);

        signoutText = findViewById(R.id.add_user_text);
        addProText = findViewById(R.id.add_contact_text);

        mFabOpenAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab_open);
        mFabCloseAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab_close);

        pname = findViewById(R.id.projectListview);

        isOpen = false;
        mMainAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOpen) {

                    mAddUserFab.setAnimation(mFabCloseAnim);
                    mAddContactFab.setAnimation(mFabCloseAnim);

                    signoutText.setVisibility(View.INVISIBLE);
                    addProText.setVisibility(View.INVISIBLE);

                    isOpen = false;
                } else {

                    mAddUserFab.setAnimation(mFabOpenAnim);
                    mAddContactFab.setAnimation(mFabOpenAnim);

                    signoutText.setVisibility(View.VISIBLE);
                    addProText.setVisibility(View.VISIBLE);

                    isOpen = true;
                }

            }
        });
        mAddUserFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mauth.signOut();
                finish();
                progressDialog.show();
                Intent i = new Intent(MainActivity.this, Login.class);
                startActivity(i);
            }
        });
        mAddContactFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            if (dataSnapshot.exists()) {
                                isadmin = Integer.parseInt(dataSnapshot.child("admin").getValue().toString());
                                if (isadmin == 1) {
                                    mAddContactFab.setEnabled(true);
                                    Intent i = new Intent(MainActivity.this, addProject.class);
                                    startActivity(i);

                                } else if (isadmin == 0) {
                                    mAddContactFab.setEnabled(false);
                                    Toast.makeText(MainActivity.this, "Only admin can add projects , Please contact to admin !!", Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (NullPointerException e) {
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        adapter = new ProNameListAdapter(getApplicationContext(), arrayList);


        pname.setAdapter(adapter);
        pname.setClickable(true);
        pname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView clicked = view.findViewById(R.id.liTextview);
                Intent i = new Intent(MainActivity.this, projectDetails.class);
                i.putExtra("projectnameclicked", clicked.getText().toString().trim());
                startActivity(i);

            }
        });
        mauth = FirebaseAuth.getInstance();
        currentUser = mauth.getCurrentUser().getDisplayName();
        Mydb = FirebaseDatabase.getInstance();
        myref = Mydb.getReference().child("Users").child(currentUser);
        DatabaseReference Users = myref.child("Projects");
        Toast.makeText(this, currentUser, Toast.LENGTH_SHORT).show();

        Users.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                try {

                    String ProjName = dataSnapshot.child("Project_Name").getValue().toString();
                    String ProjDesc = dataSnapshot.child("Project_Description").getValue().toString();


                    arrayList.add(new ProListView(ProjName, ProjDesc));
                    adapter.notifyDataSetChanged();
                } catch (NullPointerException e) {
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String ProjName = dataSnapshot.child("Project_Name").getValue().toString();
                    String ProjDesc = dataSnapshot.child("Project_Description").getValue().toString();
                    arrayList.add(new ProListView(ProjName, ProjDesc));
                    adapter.notifyDataSetChanged();
                    progressDialog.show();
//                    loader.setVisibility(View.GONE);


                } catch (NullPointerException e) {
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                try {
                    String ProjName = dataSnapshot.child("Project_Name").getValue().toString();
                    String ProjDesc = dataSnapshot.child("Project_Description").getValue().toString();
                    arrayList.add(new ProListView(ProjName, ProjDesc));
                    adapter.notifyDataSetChanged();
                    progressDialog.show();

                } catch (NullPointerException e) {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

 }
    public void loading(){

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Welcome !!");
        progressDialog.setProgress(10);
        progressDialog.setMax(100);
        progressDialog.setMessage("Loading...");
        new Login.MyTask().execute();

    }
}
