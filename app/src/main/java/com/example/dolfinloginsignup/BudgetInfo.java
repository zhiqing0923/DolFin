package com.example.dolfinloginsignup;

public class BudgetInfo {



    private String category;
    private String budget;
    private String percentage;

    // an empty constructor is
    // required when using
    // Firebase Realtime Database.
    public BudgetInfo() {

    }

    public String getPercentage() {
        return percentage;
    }

    public String getBudget() {
        return budget;
    }

    public String getCategory() {
        return category;
    }

    // created getter and setter methods
    // for all our variables.


    public void setBudget(String budget) {
        this.budget = budget;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
