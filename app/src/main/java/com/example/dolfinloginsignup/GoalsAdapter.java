package com.example.dolfinloginsignup;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
public class GoalsAdapter extends FirebaseRecyclerAdapter<Goals, GoalsAdapter.GoalsViewholder>{
    public GoalsAdapter(
            @NonNull FirebaseRecyclerOptions<Goals> options) {
        super(options);
    }

    // Function to bind the view in Card view(here
    // "editSavingsGoal.xml") iwth data in
    // model class(here "person.class")
    @Override
    protected void onBindViewHolder(@NonNull GoalsViewholder holder, int position, @NonNull Goals model) {

        // Add name from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        holder.name.setText(model.getName());

        // Add lastname from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        //holder.amount.setText(model.getAmount());

        // Add age from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        //holder.startDate.setText(model.getStartDate());

        // holder.endDate.setText(model.getEndDate());
        int amount = Integer.parseInt(model.getAmount());
        int amountCollected = Integer.parseInt(model.getAmountCollected());
        int percent = (int) (((double) amountCollected / (double) amount)*100);

        holder.progress.setText(String.valueOf(percent)+'%');
        holder.bar.setProgress(percent);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                SavingGoalDetailFragment savingGoalDetailFragment = new SavingGoalDetailFragment(model.getName(), model.getAmount(), model.getStartDate(),model.getEndDate(), model.getAmountCollected());
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.setSavingsGoal, savingGoalDetailFragment).addToBackStack(null).commit();

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.name.getContext());
                builder.setTitle("Delete Goal");
                builder.setMessage("Delete...?");

                builder.setPositiveButton("Yes", ((dialogInterface, i) -> {
                    FirebaseDatabase.getInstance().getReference().child("Goals").child(getRef(holder.getBindingAdapterPosition()).getKey()).removeValue();
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
    public GoalsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goals_individual_item_view, parent, false);
        return new GoalsAdapter.GoalsViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    class GoalsViewholder extends RecyclerView.ViewHolder {

        TextView name, amount, startDate, endDate, progress;
        ProgressBar bar;

        public GoalsViewholder(@NonNull View itemView)
        {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            //amount = itemView.findViewById(R.id.amount);
            //startDate = itemView.findViewById(R.id.startDate);
            //endDate = itemView.findViewById(R.id.endDate);
            progress = itemView.findViewById(R.id.progress);
            bar = itemView.findViewById(R.id.bar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
