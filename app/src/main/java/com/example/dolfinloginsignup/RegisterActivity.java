package com.example.dolfinloginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    TextView TVLoginAcc;
    EditText ETName, ETEmail, ETPhnNum, ETPassword, ETConfirmPassword;
    Button BtnRegister;
    String emailPattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$";
    String phnPattern = "^[0-9]{10,11}$";
    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RegisterInfo registerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("RegisterInfo");
        registerInfo = new RegisterInfo();

        ETName = findViewById(R.id.ETName);
        ETEmail = findViewById(R.id.ETEmail);
        ETPhnNum = findViewById(R.id.ETPhnNum);
        ETPassword = findViewById(R.id.ETPassword);
        ETConfirmPassword = findViewById(R.id.ETConfirmPassword);
        BtnRegister = findViewById(R.id.BtnLogin);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        TVLoginAcc = findViewById(R.id.TVLoginAcc);

        TVLoginAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
            }
        });

        BtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PerforAuth();

            }

        });
    }

    private void PerforAuth() {
        String name = ETName.getText().toString();
        String email = ETEmail.getText().toString();
        String phoneNum = ETPhnNum.getText().toString();
        String password = ETPassword.getText().toString();
        String confirmPsw = ETConfirmPassword.getText().toString();

        if(!email.matches(emailPattern)){
            ETEmail.setError("Please enter a valid email");
        }else if(name.isEmpty()){
            ETName.setError("Please enter your name");
        }else if(password.isEmpty() || password.length()<8){
            ETPassword.setError("Please enter a valid password");
        }else if(!password.equals(confirmPsw)){
            ETConfirmPassword.setError("Password not match");
        }else if(phoneNum.length()>11 || !phoneNum.matches(phnPattern)){
            ETPhnNum.setError("Please enter a valid phone number");
        }else{
            addDatatoFirebase(name, email, phoneNum,password);

            progressDialog.setMessage("Please wait for registration...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            startActivity(new Intent(RegisterActivity.this,MainActivity.class));

            /*firebaseAuth.createUserWithEmailAndPassword(email,psw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(RegisterActivity.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this,""+task.getException(),Toast.LENGTH_SHORT).show();
                    }
                }
            });*/
        }
    }

    private void addDatatoFirebase(String name, String email, String phoneNum,String password) {
        registerInfo.setName(name);
        registerInfo.setEmail(email);
        registerInfo.setPhoneNum(phoneNum);
        registerInfo.setPassword(password);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child(name).setValue(registerInfo);
                Toast.makeText(RegisterActivity.this, "Account Registered Successfully. ", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegisterActivity.this, "Registration failed." + error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void sendUserToNextActivity() {
        Intent intent = new Intent(RegisterActivity.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}