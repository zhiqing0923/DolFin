package com.example.dolfinloginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    TextView TVAccInfo, TVEdit, TVLogout, TVName4;
    ImageButton IBhomepage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TVAccInfo = findViewById(R.id.TVAccInfo);
        TVEdit = findViewById(R.id.TVEdit);
        TVLogout = findViewById(R.id.TVLogout);
        TVName4 = findViewById(R.id.TVName4);
        IBhomepage = findViewById(R.id.IBhomepage);

        IBhomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passUserData();
            }
        });


        TVLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        TVAccInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = TVName4.getText().toString().trim();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RegisterInfo");
                Query checkUserDatabase = reference.orderByChild("name").equalTo(userName);
                checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String nameFromDB = snapshot.child(userName).child("name").getValue(String.class);
                            String emailFromDB = snapshot.child(userName).child("email").getValue(String.class);
                            String phnNumFromDB = snapshot.child(userName).child("phoneNum").getValue(String.class);
                            String passwordFromDB = snapshot.child(userName).child("password").getValue(String.class);
                            Intent intent = new Intent(ProfileActivity.this, AccountInfoActivity.class);
                            intent.putExtra("name", nameFromDB);
                            intent.putExtra("email", emailFromDB);
                            intent.putExtra("phoneNum", phnNumFromDB);
                            intent.putExtra("password", passwordFromDB);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        TVEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = TVName4.getText().toString().trim();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RegisterInfo");
                Query checkUserDatabase = reference.orderByChild("name").equalTo(userName);
                checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String nameFromDB = snapshot.child(userName).child("name").getValue(String.class);
                            String emailFromDB = snapshot.child(userName).child("email").getValue(String.class);
                            String phnNumFromDB = snapshot.child(userName).child("phoneNum").getValue(String.class);
                            String passwordFromDB = snapshot.child(userName).child("password").getValue(String.class);
                            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                            intent.putExtra("name", nameFromDB);
                            intent.putExtra("email", emailFromDB);
                            intent.putExtra("phoneNum", phnNumFromDB);
                            intent.putExtra("password", passwordFromDB);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        TVName4.setText(name);
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Log Out");
        builder.setMessage("Are you sure you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void passUserData(){
        String userName = TVName4.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RegisterInfo");
        Query checkUserDatabase = reference.orderByChild("name").equalTo(userName);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String nameFromDB = snapshot.child(userName).child("name").getValue(String.class);
                    String emailFromDB = snapshot.child(userName).child("email").getValue(String.class);
                    String phnNumFromDB = snapshot.child(userName).child("phoneNum").getValue(String.class);

                    Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);

                    intent.putExtra("name",nameFromDB);
                    intent.putExtra("email",emailFromDB);
                    intent.putExtra("phoneNum",phnNumFromDB);

                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}