package com.example.dolfinloginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    TextView TVRegisterAcc;
    EditText ETEmail, ETPassword;
    Button BtnLogin;
    String emailPattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$";
    String phnPattern = "^[0-9]{10}$";
    ProgressDialog progressDialog;

    ImageView IVGoogle;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseDatabase.getInstance();

        ETEmail = findViewById(R.id.ETEmail);
        ETPassword = findViewById(R.id.ETPassword);
        BtnLogin = findViewById(R.id.BtnLogin);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        IVGoogle = findViewById(R.id.IVGoogle);

        TVRegisterAcc = findViewById(R.id.TVRegisterAcc);

        TVRegisterAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUser();
                progressDialog.setMessage("Please wait while login...");
                progressDialog.setTitle("Login");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }
        });

        IVGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,GoogleSignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });


    }

    /*private void perforLogin() {
        String email = ETEmail.getText().toString();
        String psw = ETPassword.getText().toString();

        String encodeUserEmail = encodeUserEmail(email);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RegisterInfo");
        Query checkUserDB = databaseReference.orderByChild("name").equalTo(encodeUserEmail);

        if (!email.matches(emailPattern)) {
            ETEmail.setError("Please enter a valid email");
        } else if (psw.isEmpty() || psw.length() < 8) {
            ETPassword.setError("Please enter a valid password");
        } else {
            progressDialog.setMessage("Please wait while login...");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email,psw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        //sendUserToNextActivity();
                        Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();

                        checkUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String nameFromDB = snapshot.child(encodeUserEmail).child("name").getValue(String.class);
                                String emailFromDB = snapshot.child(encodeUserEmail).child("email").getValue(String.class);
                                String phnNumFromDB = snapshot.child(encodeUserEmail).child("phoneNum").getValue(String.class);

                                Intent intent = new Intent(MainActivity.this,HomeActivity.class);

                                intent.putExtra("name",nameFromDB);
                                intent.putExtra("email",emailFromDB);
                                intent.putExtra("phoneNum",phnNumFromDB);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this,""+task.getException(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }*/

    public void checkUser(){
        String userUsername = ETEmail.getText().toString().trim();
        String userPassword = ETPassword.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RegisterInfo");
        Query checkUserDatabase = reference.orderByChild("name").equalTo(userUsername);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ETEmail.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);
                    if (passwordFromDB.equals(userPassword)) {
                        ETEmail.setError(null);
                        progressDialog.dismiss();
                        String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                        String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                        String phnNumFromDB = snapshot.child(userUsername).child("phoneNum").getValue(String.class);
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("phoneNum", phnNumFromDB);
                        intent.putExtra("password", passwordFromDB);
                        startActivity(intent);
                    } else {
                        ETPassword.setError("Invalid Credentials");
                        ETPassword.requestFocus();
                        progressDialog.dismiss();
                    }
                } else {
                    ETEmail.setError("User does not exist");
                    ETEmail.requestFocus();
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void sendUserToNextActivity() {
        Intent intent = new Intent(MainActivity.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    static String encodeUserEmail(String userEmail){
        return userEmail.replace(".",",");
    }
}