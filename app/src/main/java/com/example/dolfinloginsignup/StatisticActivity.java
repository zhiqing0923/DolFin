package com.example.dolfinloginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class StatisticActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    Button dateButton;
    String key = null;
    PieChart pieChart;
    ExpenseAdapter3 adapter; // Create Object of the Adapter class
    DatabaseReference mbase; // Create object of the Firebase Realtime Database
    ImageView leftIcon;
    String captureSelectedItem;
    DatePickerDialog datePickerDialog;
    String date = null;
    float TFood = 0, TFashion = 0, TTransport = 0, TDaily = 0, TTravel = 0, TRent = 0, TGrocery = 0, TOthers = 0, total = 0;
    float fFood = 0, fFashion = 0, fTransport = 0, fDaily = 0, fTravel = 0, fRent = 0, fGrocery = 0, fOthers = 0;
    private RecyclerView expenseRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        //Calender
        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        date = dateButton.getText().toString();

        // Create a instance of the database and get its reference
        mbase = FirebaseDatabase.getInstance().getReference("ExpenseInfo");
        expenseRecycler = findViewById(R.id.incomerecycler);

        leftIcon = findViewById(R.id.left_icon);
        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        // Create a instance of the database and get its reference
        expenseRecycler = findViewById(R.id.incomerecycler);

        // To display the Recycler view linearly
        expenseRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));

        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<ExpenseInfo> options = new FirebaseRecyclerOptions.Builder<ExpenseInfo>().setQuery(mbase, ExpenseInfo.class).build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        adapter = new ExpenseAdapter3(options);
        // Connecting Adapter class with the Recycler view*/
        expenseRecycler.setAdapter(adapter);

        //Graph start here
        pieChart = findViewById(R.id.pieChart);
        setupPieChart();
        loadPieChart(filterForGraph());

        //Day Picker
        Spinner spinner = findViewById(R.id.calender_spinner);
        ArrayAdapter adapterSpinner = ArrayAdapter.createFromResource(
                this,
                R.array.Spinner_Day_Picker,
                R.layout.spinner_layout
        );
        adapterSpinner.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spinner.setAdapter(adapterSpinner);
        spinner.setOnItemSelectedListener(this);
    }

    //Calculate float for each category
    public float[] filterForGraph() {
        String[] category = {"Food", "Fashion", "Transport", "Daily Goods", "Travel", "Rent", "Grocery", "Others"};
        mbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ExpenseInfo expenseInfo = ds.getValue(ExpenseInfo.class);
                    if (expenseInfo.getExpenseDate().contains(date)) {
                        if (expenseInfo.getExpenseCategory().equals(category[0])) {
                            TFood += Float.parseFloat(expenseInfo.getExpenseAmount());
                        } else if (expenseInfo.getExpenseCategory().equals(category[1])) {
                            TFashion += Float.parseFloat(expenseInfo.getExpenseAmount());
                        } else if (expenseInfo.getExpenseCategory().equals(category[2])) {
                            TTransport += Float.parseFloat(expenseInfo.getExpenseAmount());
                        } else if (expenseInfo.getExpenseCategory().equals(category[3])) {
                            TDaily += Float.parseFloat(expenseInfo.getExpenseAmount());
                        } else if (expenseInfo.getExpenseCategory().equals(category[4])) {
                            TTravel += Float.parseFloat(expenseInfo.getExpenseAmount());
                        } else if (expenseInfo.getExpenseCategory().equals(category[5])) {
                            TRent += Float.parseFloat(expenseInfo.getExpenseAmount());
                        } else if (expenseInfo.getExpenseCategory().equals(category[6])) {
                            TGrocery += Float.parseFloat(expenseInfo.getExpenseAmount());
                        } else if (expenseInfo.getExpenseCategory().equals(category[7])) {
                            TOthers += Float.parseFloat(expenseInfo.getExpenseAmount());
                        }
                        total += Float.parseFloat(expenseInfo.getExpenseAmount());
                    }
                }
                fFood = TFood / total;
                fFashion = TFashion / total;
                fTransport = TTransport / total;
                fDaily = TDaily / total;
                fTravel = TTravel / total;
                fRent = TRent / total;
                fGrocery = TGrocery / total;
                fOthers = TOthers / total;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return new float[]{fFood, fFashion, fTransport, fDaily, fTransport, fTravel, fRent, fGrocery, fOthers};
    }

    //Methods for pie chart
    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Expenses Category");
        pieChart.setCenterTextSize(20);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
        l.setFormSize(5f);
    }

    //float f1, float f2, float f3, float f4, float f5, float f6, float f7, float f8
    //float [] values
    public void loadPieChart(float[] values) {
        //float [] values = {f1, f2, f3, f4, f5, f6,f7, f8};
        String[] category = {"Food", "Fashion", "Transport", "Daily Goods", "Travel", "Rent", "Grocery", "Others"};
        ArrayList<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < category.length; i++) {
            if (values[i] != 0.0) {
                entries.add(new PieEntry(values[i], category[i]));
            }
        }

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color : ColorTemplate.LIBERTY_COLORS) {
            colors.add(color);
        }
        for (int color : ColorTemplate.COLORFUL_COLORS) {
            colors.add(color);
        }


        PieDataSet dataSet = new PieDataSet(entries, " ");
        //dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.setEntryLabelTextSize(10f);
        pieChart.invalidate();
        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }

    //Dropbox methods
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
        if (parent.getSelectedItem().toString().equals("Daily")) {
            setupPieChart();
        } else if (parent.getSelectedItem().toString().equals("Weekly")) {
            setupPieChart();
        } else if (parent.getSelectedItem().toString().equals("Monthly")) {
            setupPieChart();
        } else if (parent.getSelectedItem().toString().equals("Yearly")) {
            setupPieChart();
        }

        loadPieChart(filterForGraph());
        captureSelectedItem = parent.getSelectedItem().toString();
        dateButton.setText(getSelectedDate(parent.getSelectedItem().toString()));
        date = dateButton.getText().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();
    }

    //Calender button
    public String getSelectedDate(String input) {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        if (input.equals("Daily") || input.equals("Weekly")) {
            return makeDateString_Day(day, month, year);
        } else if (input.equals("Monthly")) {
            return makeDateString_Month(month, year);
        } else if (input.equals("Yearly")) {
            return makeDateString_Year(year);
        }
        return null;
    }


    public void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                if (captureSelectedItem.equals("Daily") || captureSelectedItem.equals("Weekly")) {
                    date = makeDateString_Day(day, month, year);
                } else if (captureSelectedItem.equals("Monthly")) {
                    date = makeDateString_Month(month, year);
                } else if (captureSelectedItem.equals("Yearly")) {
                    date = makeDateString_Year(year);
                } else {
                    date = "";
                }
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    public String makeDateString_Day(int day, int month, int year) {
        return day + " " + getMonthFormat(month) + " " + year;
    }

    public String makeDateString_Month(int month, int year) {
        return getMonthFormat(month) + " " + year;
    }

    public String makeDateString_Year(int year) {
        return "" + year;
    }

    public String getMonthFormat(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
}