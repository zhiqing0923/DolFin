
package com.example.dolfinloginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewBudget extends AppCompatActivity {

    private RecyclerView recyclerView;
    BudgetAdapter adapter; // Create Object of the Adapter class
    DatabaseReference mbase; // Create object of the
    Button setNewBudget;
    private double totalExpenses;
    private int budget, percentage;

    public ViewBudget() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_budget);

        // Create a instance of the database and get its reference
        mbase = FirebaseDatabase.getInstance().getReference("Budget");

        recyclerView = findViewById(R.id.recycler1);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<BudgetInfo> options = new FirebaseRecyclerOptions.Builder<BudgetInfo>().setQuery(mbase, BudgetInfo.class).build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        adapter = new BudgetAdapter(options);
        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(adapter);


        setNewBudget = findViewById(R.id.BtnSetNewBudget);
        setNewBudget.setOnClickListener(view -> {
            Intent intent = new Intent(ViewBudget.this, SetBudget.class);
            startActivityForResult(intent, 1);
        });

    }

    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}