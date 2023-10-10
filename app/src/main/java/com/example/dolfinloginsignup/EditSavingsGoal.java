package com.example.dolfinloginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import android.os.Bundle;

public class EditSavingsGoal extends AppCompatActivity {

    private EditText nameEdt;
    private EditText amountEdt;
    private EditText startDateEdt;
    private EditText endDateEdt;
    private Button setSavingsGoalBtn;
    private ImageView IVbackButton2;

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    // creating a variable for
    // our object class
    Goals goals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_savings_goal);

        // on below line we are initializing our variables.
        nameEdt = findViewById(R.id.ETSavingsGoalName);
        amountEdt = findViewById(R.id.ETAmount);
        startDateEdt = findViewById(R.id.ETStartDate);
        endDateEdt = findViewById(R.id.ETEndDate);
        setSavingsGoalBtn = findViewById(R.id.BtnSetSavingsGoal);

        // below line is used to get the
        // instance of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("Goals");

        // initializing our object
        // class variable.
        goals = new Goals();

        // on below line we are adding click listener

        // for pick start date button
        startDateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        EditSavingsGoal.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our edit text.
                                startDateEdt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }


        });

        IVbackButton2 = findViewById(R.id.IVbackButton2);
        IVbackButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditSavingsGoal.this, SetSavingsGoal.class);
                startActivityForResult(intent, 1);
            }
        });

        // for pick end date button
        endDateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        EditSavingsGoal.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our edit text.
                                endDateEdt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });

        // for set savings goal button
        setSavingsGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getting text from EditText fields
                String name = nameEdt.getText().toString();
                String amount = amountEdt.getText().toString();
                String startDate = startDateEdt.getText().toString();
                String endDate = endDateEdt.getText().toString();
                String amountCollected = "0";

                // below line is for checking whether the
                // edittext fields are empty or not.
                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(amount) && TextUtils.isEmpty(startDate) && TextUtils.isEmpty(endDate)) {
                    // if the text fields are empty
                    // then show the below message.
                    Toast.makeText(EditSavingsGoal.this, "Please complete the fields.", Toast.LENGTH_SHORT).show();
                } else {
                    // else call the method to add
                    // data to our database.
                    addDataToFirebase(name, amount, startDate, endDate,amountCollected);
                }

            }
        });

    }

    private void addDataToFirebase(String name, String amount, String startDate, String endDate, String amountCollected) {
        // below 3 lines of code is used to set
        // data in our object class.
        //Goals goals = new Goals();
        goals.setName(name);
        goals.setAmount(amount);
        goals.setStartDate(startDate);
        goals.setEndDate(endDate);
        goals.setAmountCollected(amountCollected);

        // we are use add value event listener method
        // which is called with database reference.
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                databaseReference.push().setValue(goals);

                // after adding this data we are showing toast message.
                Toast.makeText(EditSavingsGoal.this, "Goals added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(EditSavingsGoal.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), SetSavingsGoal.class);
        startActivity(intent);
    }
}