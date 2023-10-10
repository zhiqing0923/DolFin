package com.example.dolfinloginsignup;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DAOIncome {
    private DatabaseReference databaseReference;

    public DAOIncome(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Income.class.getSimpleName());
    }

    public Task<Void> add(Income income){
        return databaseReference.push().setValue(income);
    }
}
