package com.example.dolfinloginsignup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddSavings extends AppCompatActivity {

    private String referenceName, amount, amountCollected;
    private EditText addSavingsEdt;
    private Button done;

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    // creating a variable for
    // our object class
    Goals goals;

    Query query;
    String key;

    public AddSavings() {
    }

    public AddSavings(String referenceName, String amount, String amountCollected) {
        this.referenceName = referenceName;
        this.amount = amount;
        this.amountCollected = amountCollected;
    }

    public AddSavings(String referenceName) {
        this.referenceName = referenceName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_savings);

        // initialize variables
        addSavingsEdt = findViewById(R.id.ETAddSavingsAmount);
        done = findViewById(R.id.BtnDone);

        // below line is used to get the
        // instance of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference().child("Goals");

        query = databaseReference.orderByChild("name").equalTo(referenceName);

        // initializing our object
        // class variable.
        goals = new Goals();


        // for done button
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addAmount = addSavingsEdt.getText().toString();
                if (TextUtils.isEmpty(addAmount)) {
                    // if the text fields are empty
                    // then show the below message.
                    Toast.makeText(AddSavings.this, "Please add an amount.", Toast.LENGTH_SHORT).show();
                } else {
                    // else call the method to add
                    // data to our database.
                    addDataToFirebase(addAmount);
                }
            }
        });
    }

    private void addDataToFirebase(String addAmount) {
        // below 3 lines of code is used to set
        // data in our object class.
        //Goals goals = new Goals();
        goals.setAmountCollected(addAmount);

        // we are use add value event listener method
        // which is called with database reference.
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    key = dataSnapshot.getKey();
                }
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("amountCollected", addAmount);
                databaseReference.child(key).updateChildren(hashMap);

                // after adding this data we are showing toast message.
                Toast.makeText(AddSavings.this, "amount added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(AddSavings.this, "Fail to add amount " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}