package com.example.dolfinloginsignup;

public class Goals {

        private String name;
        private String amount;
        private String startDate;
        private String endDate;
        private String amountCollected;

        public Goals() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getAmountCollected() {
            return amountCollected;
        }

        public void setAmountCollected(String amountCollected) {
            this.amountCollected = amountCollected;
        }
}
