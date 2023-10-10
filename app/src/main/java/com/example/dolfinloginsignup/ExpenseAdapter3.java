package com.example.dolfinloginsignup;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ExpenseAdapter3 extends FirebaseRecyclerAdapter<
        ExpenseInfo, ExpenseAdapter3.ExpenseInfoViewholder> {

    public ExpenseAdapter3(
            @NonNull FirebaseRecyclerOptions<ExpenseInfo> options)
    {
        super(options);
    }

    // Function to bind the view in Card view(here
    // "person.xml") iwth data in
    // model class(here "person.class")
    @Override
    protected void onBindViewHolder(@NonNull ExpenseInfoViewholder holder,
                                    int position, @NonNull ExpenseInfo model)
    {

        // Add firstname from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        holder.expenseName.setText(model.getExpenseName());

        // Add lastname from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        holder.expenseDate.setText(model.getExpenseDate());

        // Add age from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        holder.expenseAmount.setText("- $" + model.getExpenseAmount());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                EditExpenseFragment editExpenseFragment = new EditExpenseFragment(model.getExpenseCategory(), model.getExpenseName(), model.getExpenseAmount(),model.getExpenseDate());
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.statistic, editExpenseFragment).addToBackStack(null).commit();

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.expenseName.getContext());
                builder.setTitle("Delete Expense Record");
                builder.setMessage("Delete...?");

                builder.setPositiveButton("Yes", ((dialogInterface, i) -> {
                    FirebaseDatabase.getInstance().getReference().child("ExpenseInfo").child(getRef(holder.getBindingAdapterPosition()).getKey()).removeValue();
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
    public ExpenseInfoViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_individual_item_view, parent, false);
        return new ExpenseAdapter3.ExpenseInfoViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    class ExpenseInfoViewholder extends RecyclerView.ViewHolder {
        TextView expenseName, expenseDate, expenseAmount;

        public ExpenseInfoViewholder(@NonNull View itemView)
        {
            super(itemView);

            expenseName = itemView.findViewById(R.id.TVExpenseName);
            expenseDate = itemView.findViewById(R.id.TVDate);
            expenseAmount = itemView.findViewById(R.id.TVExpenseAmount);
        }
    }
}
