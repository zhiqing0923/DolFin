package com.example.dolfinloginsignup;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class BudgetAdapter extends FirebaseRecyclerAdapter<
        BudgetInfo, BudgetAdapter.BudgetViewholder> {

    public BudgetAdapter(
            @NonNull FirebaseRecyclerOptions<BudgetInfo> options)
    {
        super(options);
    }


    // Function to bind the view in Card view(here
    // "person.xml") iwth data in
    // model class(here "person.class")
    @Override
    protected void
    onBindViewHolder(@NonNull BudgetViewholder holder,
                     int position, @NonNull BudgetInfo model)
    {


        // Add firstname from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        holder.TVcategory.setText(model.getCategory());
        holder.TVbudget.setText("$ " + model.getBudget());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("ExpenseInfo");
        Query query = databaseReference.orderByChild("expenseCategory").equalTo(model.getCategory());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double totalExpenses =0;
                for( DataSnapshot ds :dataSnapshot.getChildren()) {
                    ExpenseInfo expenseInfo = ds.getValue(ExpenseInfo.class);
                    double expenses = Double.parseDouble(expenseInfo.getExpenseAmount());
                    totalExpenses = totalExpenses + expenses;
                }
                holder.TVexpense.setText("$ " + String.valueOf(totalExpenses));

                int percent = (int) (((double) totalExpenses / (double) Integer.parseInt(model.getBudget()))*100);
                holder.bar.setProgress(percent);

                double remaining = Integer.parseInt(model.getBudget()) - totalExpenses;
                String formattedValue = String.format("%.2f", remaining);
                holder.TVremaining.setText("$ " + formattedValue);

                if (percent >= Integer.parseInt(model.getPercentage())) {
                    holder.TValert.setText("You have exceeded the budget limit!");
                    holder.alertIcon.setVisibility(View.VISIBLE);
                } else {
                    holder.TValert.setText("");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.TVcategory.getContext());
                builder.setTitle("Delete Budget");
                builder.setMessage("Delete...?");

                builder.setPositiveButton("Yes", ((dialogInterface, i) -> {
                    FirebaseDatabase.getInstance().getReference().child("Budget").child(getRef(holder.getBindingAdapterPosition()).getKey()).removeValue();
                }));
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
                return true;
            }
        });

    }

    // Function to tell the class about the Card view (here
    // "person.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public BudgetViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.budget_individual_item_view, parent, false);
        return new BudgetViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    class BudgetViewholder extends RecyclerView.ViewHolder {
        TextView TVcategory, TVremaining, TVexpense, TVbudget, TValert;
        ImageView alertIcon;
        ProgressBar bar;
//        TextView budget;
//        TextView percentage;
//        ProgressBar progressBar;
//        TextView totalExpense;
//        ImageView alert;
//        TextView limit;

        public BudgetViewholder(@NonNull View itemView)
        {
            super(itemView);
//            category = itemView.findViewById(R.id.category);
//            percentage = itemView.findViewById(R.id.remaining);
//            progressBar = itemView.findViewById(R.id.progressBar);
//            totalExpense = itemView.findViewById(R.id.TVTotalExpense);
//            budget = itemView.findViewById(R.id.budget);
//            alert = itemView.findViewById(R.id.IVAlert);
//            limit = itemView.findViewById(R.id.TVLimit);
            TVcategory = itemView.findViewById(R.id.category);
            TVremaining = itemView.findViewById(R.id.remaining);
            TVexpense = itemView.findViewById(R.id.totalExpense);
            TVbudget = itemView.findViewById(R.id.budget);
            bar = itemView.findViewById(R.id.progressBar);
            alertIcon = itemView.findViewById(R.id.IVAlert);
            TValert = itemView.findViewById(R.id.TValert);
        }
    }
}
