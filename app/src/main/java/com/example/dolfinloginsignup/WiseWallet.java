package com.example.dolfinloginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class WiseWallet extends AppCompatActivity {

    private ImageButton addIncomeBtn, setBudgetBtn, setSavingsGoalBtn;
    private ImageView previous2;
    private TextView textView7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wise_wallet);

        addIncomeBtn = findViewById(R.id.BtnAddIncome);
        setBudgetBtn = findViewById(R.id.BtnSetBudgetReminder);
        setSavingsGoalBtn = findViewById(R.id.BtnSetSavingsGoal2);
        textView7 = findViewById(R.id.textView7);

        Intent intent1 = getIntent();
        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        textView7.setText(name);

        previous2 = findViewById(R.id.previous2);

        previous2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passUserData();
            }
        });

        addIncomeBtn.setOnClickListener(view -> {
            passUserData2();
        });

        setBudgetBtn.setOnClickListener(view -> {
            Intent intent = new Intent(WiseWallet.this, SetBudget.class);
            startActivityForResult(intent, 1);
        });

        setSavingsGoalBtn.setOnClickListener(view -> {
            Intent intent = new Intent(WiseWallet.this, SetSavingsGoal.class);
            startActivityForResult(intent, 1);
        });
    }

    public void passUserData(){
        String userName = textView7.getText().toString().trim();
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

                    Intent intent = new Intent(WiseWallet.this, HomeActivity.class);
                    intent.putExtra("name", nameFromDB);
                    intent.putExtra("email", emailFromDB);
                    intent.putExtra("phoneNum", phnNumFromDB);
                    intent.putExtra("password", passwordFromDB);
                    startActivity(intent);
                }else{
                    Toast.makeText(WiseWallet.this,"Error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void passUserData2(){
        String userName = textView7.getText().toString().trim();
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

                    Intent intent = new Intent(WiseWallet.this, testActivity.class);
                    intent.putExtra("name", nameFromDB);
                    intent.putExtra("email", emailFromDB);
                    intent.putExtra("phoneNum", phnNumFromDB);
                    intent.putExtra("password", passwordFromDB);
                    startActivity(intent);
                }else{
                    Toast.makeText(WiseWallet.this,"Error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    }
