package com.example.dolfinloginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.AppCompatImageButton;

import com.google.firebase.database.DatabaseReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SavingGoalDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavingGoalDetailFragment extends Fragment {
    private String savingsGoalName;
    private String amount;
    private String startDate;
    private String endDate;
    private String amountCollected;
    private ProgressBar circularProgressBar;
    private AppCompatImageButton addBtn;

    DatabaseReference mbase;
    Goals goals;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SavingGoalDetailFragment() {
        // Required empty public constructor
    }

    public SavingGoalDetailFragment(String savingsGoalName, String amount, String startDate, String endDate, String amountCollected) {
        this.savingsGoalName = savingsGoalName;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amountCollected = amountCollected;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SavingGoalDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SavingGoalDetailFragment newInstance(String param1, String param2) {
        SavingGoalDetailFragment fragment = new SavingGoalDetailFragment();
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
        View view = inflater.inflate(R.layout.fragment_saving_goal_detail, container, false);
        addBtn = view.findViewById(R.id.BtnAdd);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AddSavingsFragment(savingsGoalName,amount,amountCollected);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.savingsGoalDetail, fragment).addToBackStack(null).commit();

//                AddSavings addSavings = new AddSavings(savingsGoalName);
//                Intent intent = new Intent(getActivity(), AddSavings.class);
//                getActivity().startActivity(intent);

            }
        });


        TextView TVname = view.findViewById(R.id.TVSavingsGoal);
        TextView TVamount = view.findViewById(R.id.amount);
        TextView TVstartDate = view.findViewById(R.id.startDate);
        TextView TVendDate = view.findViewById(R.id.endDate);
        TextView TVRemaining = view.findViewById(R.id.TVRemaining2);
        ProgressBar PBCircularProgressBar = view.findViewById(R.id.circularProgressBar);

        TVname.setText(savingsGoalName);
        TVamount.setText(amount);
        TVstartDate.setText(startDate);
        TVendDate.setText(endDate);

        int goalAmount = (int) Double.parseDouble(amount);
        int goalAmountCollected = (int)Double.parseDouble(amountCollected);
        int percent = (int) (((double) goalAmountCollected / (double) goalAmount)*100);
        PBCircularProgressBar.setProgress(percent);
//        PBCircularProgressBar.setMax(100);

        String remaining = String.valueOf(Integer.parseInt(amount) - Integer.parseInt(amountCollected));
        TVRemaining.setText(remaining);

        if (Double.parseDouble(amountCollected) == Double.parseDouble(amount)) {
            UnlockQuotes unlockQuotes = new UnlockQuotes();
            unlockQuotes.show(getActivity().getSupportFragmentManager(), unlockQuotes.getTag());
        }

        return view;

    }






}