package com.example.dolfinloginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class SetSavingsGoal extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button setNewSavingsGoal;
    GoalsAdapter adapter; // Create Object of the Adapter class
    DatabaseReference mbase; // Create object of the Firebase Realtime Database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_savings_goal);


        // Create a instance of the database and get its reference
        mbase = FirebaseDatabase.getInstance().getReference("Goals");

        recyclerView = findViewById(R.id.recycler1);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<Goals> options = new FirebaseRecyclerOptions.Builder<Goals>().setQuery(mbase, Goals.class).build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        adapter = new GoalsAdapter(options);
        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(adapter);

        setNewSavingsGoal = findViewById(R.id.BtnSetNewSavingsGoal);
        setNewSavingsGoal.setOnClickListener(view -> {
            Intent intent = new Intent(SetSavingsGoal.this, EditSavingsGoal.class);
            startActivityForResult(intent, 1);
        });


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