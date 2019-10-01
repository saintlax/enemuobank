package com.chikadibia.enemuobank.objects;

public class Receipt {
    public int id,user_id;
    public String transaction_type,amount,available_balance,description,time,timestamp;

    public void setId(int id) {
        this.id = id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setAvailable_balance(String available_balance) {
        this.available_balance = available_balance;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
