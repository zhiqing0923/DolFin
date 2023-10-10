package com.example.dolfinloginsignup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditIncomeActivity extends AppCompatActivity {

    private RecyclerView incomeRecycler;
    IncomeAdapter adapter; // Create Object of the Adapter class
    DatabaseReference mbase; // Create object of the Firebase Realtime Database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_income);

        // Create a instance of the database and get its reference
        mbase = FirebaseDatabase.getInstance().getReference("Income");

        incomeRecycler = findViewById(R.id.incomerecycler);

        // To display the Recycler view linearly
        incomeRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));

        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<Income> options = new FirebaseRecyclerOptions.Builder<Income>().setQuery(mbase, Income.class).build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        adapter = new IncomeAdapter(options);
        // Connecting Adapter class with the Recycler view*/
        incomeRecycler.setAdapter(adapter);
    }

    // Function to tell the app to start getting
    // data from database on starting of the activity
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
}