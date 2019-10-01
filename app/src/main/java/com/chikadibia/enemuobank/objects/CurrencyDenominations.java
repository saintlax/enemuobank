package com.chikadibia.enemuobank.objects;

public class CurrencyDenominations {
    public int id,five_hundreds,one_thousands;
    public String date_updated;
    public Double total;

    public void setTotal(Double total) {
        this.total = total;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFive_hundreds(int five_hundreds) {
        this.five_hundreds = five_hundreds;
    }

    public void setOne_thousands(int one_thousands) {
        this.one_thousands = one_thousands;
    }

    public void setDate_updated(String date_updated) {
        this.date_updated = date_updated;
    }
}
