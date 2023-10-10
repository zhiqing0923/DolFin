package com.example.dolfinloginsignup;

import android.os.Bundle;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;


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
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddSavingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddSavingsFragment extends Fragment {
    String referenceName, amount, amountCollected;
    private EditText addSavingsAmount;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddSavingsFragment() {
        // Required empty public constructor
    }

    public AddSavingsFragment(String referenceName, String amount, String amountCollected) {
        this.referenceName = referenceName;
        this.amount = amount;
        this.amountCollected = amountCollected;
    }

    public AddSavingsFragment(String referenceName) {
        this.referenceName = referenceName;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddSavingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddSavingsFragment newInstance(String param1, String param2) {
        AddSavingsFragment fragment = new AddSavingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_savings, container, false);
        // initialize variables
        addSavingsAmount = view.findViewById(R.id.ETAddSavingsAmount);
        done = view.findViewById(R.id.BtnDone);

        // below line is used to get the
        // instance of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference().child("Goals");

        // initializing our object
        // class variable.
        goals = new Goals();

        query = databaseReference.orderByChild("name").equalTo(referenceName);

        // for done button
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addAmount = addSavingsAmount.getText().toString();
                String updateAmount = String.valueOf(Integer.parseInt(amountCollected) + Integer.parseInt(addAmount));
                if (TextUtils.isEmpty(addAmount)) {
                    // if the text fields are empty
                    // then show the below message.
                    Toast.makeText(getActivity(), "Please add an amount.", Toast.LENGTH_SHORT).show();
                } else {
                    // else call the method to add
                    // data to our database.
                    addDataToFirebase(updateAmount);
                }
            }
        });
        return view;
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

                for (DataSnapshot snapShot : snapshot.getChildren()) {
                    key = snapShot.getKey(); //Key
                }
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("amountCollected", addAmount);
                databaseReference.child(key).updateChildren(hashMap);

                // after adding this data we are showing toast message.
                Toast.makeText(getActivity(), "amount added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(getActivity(), "Fail to add amount " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

}