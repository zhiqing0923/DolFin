package com.example.dolfinloginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity2 extends AppCompatActivity {

    ImageButton IBhomepage,IBchart,IBwallet,IBuser;
    TextView TVName6,TVTotalExpense, TVTotalIncome, TVBalance;
    ImageView BtnAdd;

    private RecyclerView expenseRecycler;
    ExpenseAdapter2 adapter; // Create Object of the Adapter class
    DatabaseReference mbase; // Create object of the Firebase Realtime Database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        // Create a instance of the database and get its reference
        mbase = FirebaseDatabase.getInstance().getReference("ExpenseInfo");

        expenseRecycler = findViewById(R.id.incomerecycler);

        // To display the Recycler view linearly
        expenseRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));

        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<ExpenseInfo> options = new FirebaseRecyclerOptions.Builder<ExpenseInfo>().setQuery(mbase, ExpenseInfo.class).build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        adapter = new ExpenseAdapter2(options);
        // Connecting Adapter class with the Recycler view*/
        expenseRecycler.setAdapter(adapter);

        IBhomepage = findViewById(R.id.IBhomepage);
        IBchart = findViewById(R.id.IBchart);
        IBwallet = findViewById(R.id.IBwallet);
        IBuser = findViewById(R.id.IBuser);
        BtnAdd = findViewById(R.id.BtnAdd);
        TVTotalExpense = findViewById(R.id.totalExpenses);
        TVTotalIncome = findViewById(R.id.totalIncome);
        TVBalance = findViewById(R.id.totalBalance);

        mbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double totalExpenses =0;
                for( DataSnapshot ds :dataSnapshot.getChildren()) {
                    ExpenseInfo expenseInfo = ds.getValue(ExpenseInfo.class);
                    double expenses = Double.parseDouble(expenseInfo.getExpenseAmount());
                    totalExpenses = totalExpenses + expenses;
                }
                TVTotalExpense.setText("$ " + String.valueOf(totalExpenses));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Income");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double totalIncome =0;
                for( DataSnapshot ds :dataSnapshot.getChildren()) {
                    Income income = ds.getValue(Income.class);
                    double expenses = Double.parseDouble(income.getAmount());
                    totalIncome = totalIncome + expenses;
                }
                TVTotalIncome.setText("$ " + String.valueOf(totalIncome));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double totalExpenses =0;
                for( DataSnapshot ds :dataSnapshot.getChildren()) {
                    ExpenseInfo expenseInfo = ds.getValue(ExpenseInfo.class);
                    double expenses = Double.parseDouble(expenseInfo.getExpenseAmount());
                    totalExpenses = totalExpenses + expenses;
                }

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Income");
                double finalTotalExpenses = totalExpenses;
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        double totalIncome =0;
                        for( DataSnapshot ds :dataSnapshot.getChildren()) {
                            Income income = ds.getValue(Income.class);
                            double expenses = Double.parseDouble(income.getAmount());
                            totalIncome = totalIncome + expenses;
                        }
                        TVBalance.setText("$ " + String.valueOf(totalIncome-finalTotalExpenses));
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        IBuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passUserData();
            }
        });

        IBchart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passUserData3();
            }
        });

        IBwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passUserData2();
            }
        });

        BtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passUserData4();
            }
        });


    }

    @Override protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }

    private void passUserData() {
        String userName = "dolfin_user";
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

                    Intent intent = new Intent(HomeActivity2.this, ProfileActivity.class);
                    intent.putExtra("name", nameFromDB);
                    intent.putExtra("email", emailFromDB);
                    intent.putExtra("phoneNum", phnNumFromDB);
                    intent.putExtra("password", passwordFromDB);
                    startActivity(intent);
                }else{
                    Toast.makeText(HomeActivity2.this,"Error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void passUserData2(){
        String userName = "dolfin_user";
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

                    Intent intent = new Intent(HomeActivity2.this, WiseWallet.class);
                    intent.putExtra("name", nameFromDB);
                    intent.putExtra("email", emailFromDB);
                    intent.putExtra("phoneNum", phnNumFromDB);
                    intent.putExtra("password", passwordFromDB);
                    startActivity(intent);
                }else{
                    Toast.makeText(HomeActivity2.this,"Error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void passUserData3(){
        String userName = "dolfin_user";
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

                    Intent intent = new Intent(HomeActivity2.this, StatisticActivity.class);
                    intent.putExtra("name", nameFromDB);
                    intent.putExtra("email", emailFromDB);
                    intent.putExtra("phoneNum", phnNumFromDB);
                    intent.putExtra("password", passwordFromDB);
                    startActivity(intent);
                }else{
                    Toast.makeText(HomeActivity2.this,"Error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void passUserData4(){
        String userName = "dolfin_user";
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

                    Intent intent = new Intent(HomeActivity2.this, AddExpense.class);
                    intent.putExtra("name", nameFromDB);
                    intent.putExtra("email", emailFromDB);
                    intent.putExtra("phoneNum", phnNumFromDB);
                    intent.putExtra("password", passwordFromDB);
                    startActivity(intent);
                }else{
                    Toast.makeText(HomeActivity2.this,"Error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}