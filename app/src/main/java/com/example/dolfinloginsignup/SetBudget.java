package com.example.dolfinloginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SetBudget extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] budgetCategory = { "Category", "Travel", "Food", "Transport", "Shopping", "Other"};


    // creating variables for
    // EditText and buttons.
    private EditText budgetEdt;
    private Spinner spinner;
    private SeekBar seekbar;
    private TextView TVpercentage;
    private Button btnViewBudget;
    private Button btnSetBudget;

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    // creating a variable for
    // our object class
    BudgetInfo budgetInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_budget);

        budgetEdt = findViewById(R.id.ETbudget);
        btnViewBudget = findViewById(R.id.BtnViewBudget);
        btnSetBudget = findViewById(R.id.BtnSetBudget);


        btnViewBudget = (Button) findViewById(R.id.BtnViewBudget);
        btnViewBudget.setOnClickListener(view -> {
            Intent intent = new Intent(SetBudget.this, ViewBudget.class);
            startActivityForResult(intent, 1);
        });

        //spinner
        spinner = (Spinner)findViewById(R.id.budgetspinner);
        spinner.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,budgetCategory);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);

        //seek bar
        seekbar = (SeekBar)findViewById(R.id.seekBarP);
        TVpercentage = findViewById(R.id.TVseekbarpercentage);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int percentage, boolean b) {
                TVpercentage.setText((String.valueOf(percentage*10)) + "% of your budget");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("Budget");

        // initializing our object
        // class variable.
        budgetInfo = new BudgetInfo();

        btnSetBudget.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // getting text from our edittext fields.
                String budget = budgetEdt.getText().toString();
                String category = spinner.getSelectedItem().toString();
                String percentage = String.valueOf(seekbar.getProgress()*10);

                if (TextUtils.isEmpty(budget) && TextUtils.isEmpty(category)&& TextUtils.isEmpty(percentage) ) {
                    // if the text fields are empty
                    // then show the below message.
                    Toast.makeText(SetBudget.this, "Please add a budget.", Toast.LENGTH_SHORT).show();
                } else {
                    // else call the method to add
                    // data to our database.
                    addDatatoFirebase(budget, category, percentage);
                }
            }

        });
    }

//    public void openNewActivity(){
//        Intent intent = new Intent(this, ViewBudget.class);
//        startActivity(intent);
//    }

    private void addDatatoFirebase(String budget, String category, String percentage) {
        // below 3 lines of code is used to set
        // data in our object class.

        budgetInfo.setBudget(budget);
        budgetInfo.setCategory(category);
        budgetInfo.setPercentage(percentage);

        // we are use add value event listener method
        // which is called with database reference.
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                databaseReference.push().setValue(budgetInfo);

                // after adding this data we are showing toast message.
                Toast.makeText(SetBudget.this, "Budget added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(SetBudget.this, "Fail to add budget" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}