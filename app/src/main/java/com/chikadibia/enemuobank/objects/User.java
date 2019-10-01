package com.chikadibia.enemuobank.objects;

public class User {
    public int id;
    public String first_name;
    public String last_name;
    public String pin;
    public String account_number;
    public double balance;

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

}
