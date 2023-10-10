package com.example.dolfinloginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dolfinloginsignup.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    EditText ETName, ETEmail, ETPhnNum, ETPassword, ETConfirmPassword;
    Button BtnSave;
    ImageView IVBackBtn;
    DatabaseReference databaseReference;
    String nameUser,emailUser,phnNumUser,passwordUser,confPswUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ETName = findViewById(R.id.ETName);
        ETEmail = findViewById(R.id.ETEmail);
        ETPhnNum = findViewById(R.id.ETPhnNum);
        ETPassword = findViewById(R.id.ETPassword);
        ETConfirmPassword = findViewById(R.id.ETConfirmPassword);
        BtnSave = findViewById(R.id.BtnSave);
        IVBackBtn = findViewById(R.id.IVBackBtn);

        databaseReference = FirebaseDatabase.getInstance().getReference("RegisterInfo");

        showData();

        BtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPhoneChanged() || isPasswordChanged() || isEmailChanged()){
                    Toast.makeText(EditProfileActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileActivity.this, "No Changes Found", Toast.LENGTH_SHORT).show();
                }
            }
        });


        IVBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passUserData();
            }
        });


    }

    private boolean isPhoneChanged() {
        if (!phnNumUser.equals(ETPhnNum.getText().toString())){
            databaseReference.child(nameUser).child("phoneNum").setValue(ETName.getText().toString());
            phnNumUser = ETPhnNum.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isEmailChanged() {
        if (!emailUser.equals(ETEmail.getText().toString())){
            databaseReference.child(nameUser).child("email").setValue(ETEmail.getText().toString());
            emailUser =ETEmail.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isPasswordChanged() {
        if (ETConfirmPassword.getText().toString().equals(ETPassword.getText().toString())){
            if(!passwordUser.equals(ETPassword.getText().toString())) {
                databaseReference.child(nameUser).child("password").setValue(ETPassword.getText().toString());
                passwordUser = ETPassword.getText().toString();
            }else{
                return false;
            }
        } else {
            Toast.makeText(EditProfileActivity.this,"Password not match",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    private void showData() {
        Intent intent = getIntent();

        nameUser = intent.getStringExtra("name");
        emailUser = intent.getStringExtra("email");
        phnNumUser = intent.getStringExtra("phoneNum");
        passwordUser = intent.getStringExtra("password");

        ETName.setText(nameUser);
        ETEmail.setText(emailUser);
        ETPhnNum.setText(phnNumUser);
        ETPassword.setText(passwordUser);

        if(ETName.getText().toString().equals("dolfin_user")){
            ETName.setFocusable(false);
            ETEmail.setFocusable(false);
            ETPassword.setFocusable(false);
            ETConfirmPassword.setFocusable(false);
        }
    }

    public void passUserData(){
        String userName = ETName.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RegisterInfo");
        Query checkUserDatabase = reference.orderByChild("name").equalTo(userName);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String nameFromDB = snapshot.child(userName).child("name").getValue(String.class);
                    String emailFromDB = snapshot.child(userName).child("email").getValue(String.class);
                    String phnNumFromDB = snapshot.child(userName).child("phoneNum").getValue(String.class);

                    Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);

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