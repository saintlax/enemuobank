package com.chikadibia.enemuobank.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.chikadibia.enemuobank.objects.CurrencyDenominations;
import com.chikadibia.enemuobank.objects.Receipt;
import com.chikadibia.enemuobank.objects.User;

import java.util.ArrayList;
import java.util.List;

import static com.chikadibia.enemuobank.constants.Constants.DATE_UPDATED;
import static com.chikadibia.enemuobank.constants.Constants.FIVE_HUNDREDS;
import static com.chikadibia.enemuobank.constants.Constants.ONE_THOUSANDS;
import static com.chikadibia.enemuobank.constants.Constants.TOTAL;
import static com.chikadibia.enemuobank.helpers.Utils.getTimestamp;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "users.db";
    public static String TABLE_USERS = "users";
    public static String TABLE_BANK_BALANCE = "bank_balance";
    public static String TABLE_CURRENCY_DENOMINATIONS = "currency_denominations";
    public static String TABLE_RECEIPTS = "receipts";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table "+TABLE_USERS +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "first_name TEXT," +
                "last_name TEXT," +
                "pin TEXT," +
                "balance TEXT," +
                "account_number TEXT)";
        db.execSQL(query);

        query = "create table "+TABLE_BANK_BALANCE +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "total_balance TEXT," +
                "date_updated TEXT)";
        db.execSQL(query);

        query = "create table "+TABLE_CURRENCY_DENOMINATIONS +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "five_hundreds INTEGER," +
                "one_thousands INTEGER," +
                "total DOUBLE," +
                "date_updated TEXT)";
        db.execSQL(query);



        query = "create table "+TABLE_RECEIPTS +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "transaction_type TEXT," +
                "amount TEXT," +
                "available_balance TEXT," +
                "description TEXT," +
                "time TEXT," +
                "timestamp TEXT)";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_BANK_BALANCE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CURRENCY_DENOMINATIONS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_RECEIPTS);
        onCreate(db);
    }

    public boolean do_insert(String table, ContentValues contentValues){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(table,null,contentValues);
        if(result != -1){
            return true;
        }

        return false;
    }

    public int do_edit(String table, ContentValues contentValues,String dbvar ,String param){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(table,contentValues,dbvar +" = ?",new String[]{param});
    }


    public Integer do_delete(String table,String dbvar,String param){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(table,dbvar+" = ?", new String[]{param});
    }
    public Cursor query(String query){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery(query,null);
        return res;
    }

    public User user_data(String query){
        User user = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery(query,null);
        if(res.getCount() == 0){
            return null;
        }
        while(res.moveToNext()){
            int id = res.getInt(0);
            String first_name = res.getString(1);
            String last_name = res.getString(2);
            String pin = res.getString(3);
            String balance = res.getString(4);
            String account_number = res.getString(5);
            user = new User();
            user.setId(id);
            user.setLast_name(last_name);
            user.setFirst_name(first_name);
            user.setPin(pin);
            user.setBalance((balance != null) ? Double.parseDouble(balance) : 0);
            user.setAccount_number(account_number);
        }
        res.close();
        return user;
    }

    public List<User> testUsersList(){
        List<User> bankUsers = new ArrayList<>();
        User user = new User();
        user.setId(0);
        user.setLast_name("Enemuo");
        user.setFirst_name("Chikadibia");
        user.setPin("1234");
        user.setBalance(7500);
        user.setAccount_number("87645534");
        bankUsers.add(user);

        user = new User();
        user.setId(2);
        user.setLast_name("Okodede");
        user.setFirst_name("Gideon");
        user.setPin("4356");
        user.setBalance(2500.6);
        user.setAccount_number("87634234");
        bankUsers.add(user);

        user = new User();
        user.setId(3);
        user.setLast_name("Emeka");
        user.setFirst_name("Enemuo");
        user.setPin("7265");
        user.setBalance(550.9);
        user.setAccount_number("67645234");
        bankUsers.add(user);

        user = new User();
        user.setId(4);
        user.setLast_name("Uchenna");
        user.setFirst_name("Enemuo");
        user.setPin("6732");
        user.setBalance(200.4);
        user.setAccount_number("17625734");
        bankUsers.add(user);

        return bankUsers;
    }

    public boolean addTestUsers(){
        int i = 0;
        for(User user: testUsersList()){
            if(addUser(user))
                i++;
        }
        return (i > 0)? true : false;
    }

    public boolean addUser(User user){
        ContentValues contentValues = new ContentValues();
        contentValues.put("last_name",user.last_name);
        contentValues.put("first_name",user.first_name);
        contentValues.put("pin",user.pin);
        contentValues.put("account_number",user.account_number);
        contentValues.put("balance",user.balance);
        Log.e("**New User**",user.last_name);
        return do_insert(TABLE_USERS,contentValues);
    }

    public boolean addReceipt(Receipt receipt){
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id",receipt.user_id);
        contentValues.put("transaction_type",receipt.transaction_type);
        contentValues.put("amount",receipt.amount);
        contentValues.put("available_balance",receipt.available_balance);
        contentValues.put("description",receipt.description);
        contentValues.put("time",receipt.time);
        contentValues.put("timestamp",receipt.timestamp);
        return do_insert(TABLE_RECEIPTS,contentValues);
    }

    public boolean hasTestUsers(){
        String query = "select * from "+TABLE_USERS+"";
        Cursor cursor = query(query);
        if(cursor.getCount() == 0){
            return addTestUsers();
        }
        return false;
    }

    public boolean addMoneyToATM(int five_hundred,int one_thousand){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIVE_HUNDREDS,five_hundred);
        contentValues.put(ONE_THOUSANDS,one_thousand);
        int total = (500 * five_hundred) + (one_thousand * 1000);
        contentValues.put(TOTAL,total);
        contentValues.put(DATE_UPDATED,getTimestamp());
        String query = "select * from "+TABLE_CURRENCY_DENOMINATIONS + " order by id desc";
        Cursor cursor = query(query);
        if(cursor.getCount()==0)
        do_insert(TABLE_CURRENCY_DENOMINATIONS,contentValues);
        return false;
    }


    public List<CurrencyDenominations> ATMBalance(String query){
        List<CurrencyDenominations> currencyDenominationsList = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery(query,null);
        if(res.getCount() == 0){
            return null;
        }
        currencyDenominationsList = new ArrayList<>();
        while(res.moveToNext()){
            int id = res.getInt(0);
            int five_hundred = res.getInt(1);
            int one_thousand = res.getInt(2);
            Double total = res.getDouble(3);
            String date = res.getString(4);
            CurrencyDenominations c = new CurrencyDenominations();
            c.setId(id);
            c.setFive_hundreds(five_hundred);
            c.setOne_thousands(one_thousand);
            c.setTotal(total);
            c.setDate_updated(date);
            currencyDenominationsList.add(c);
        }

        res.close();
        return currencyDenominationsList;
    }


    public List<Receipt> ReceiptList(String query){
        List<Receipt> receiptList = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery(query,null);
        if(res.getCount() == 0){
            return null;
        }
        receiptList = new ArrayList<>();
        while(res.moveToNext()){
            int id = res.getInt(0);
            int user_id = res.getInt(1);
            String transaction_type = res.getString(2);
            String amount = res.getString(3);
            String available_balance = res.getString(4);
            String description = res.getString(5);
            String time = res.getString(6);
            String timestamp = res.getString(7);
            Receipt r = new Receipt();
            r.setId(id);
            r.setUser_id(user_id);
            r.setTransaction_type(transaction_type);
            r.setAmount(amount);
            r.setAvailable_balance(available_balance);
            r.setDescription(description);
            r.setTime(time);
            r.setTimestamp(timestamp);
            receiptList.add(r);
        }
        res.close();
        return receiptList;
    }


    public List<User> UsersList(String query){
        List<User> userList = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery(query,null);
        if(res.getCount() == 0){
            return null;
        }
        userList = new ArrayList<>();
        while(res.moveToNext()){
            int id = res.getInt(0);
            String first_name = res.getString(1);
            String last_name = res.getString(2);
            String pin = res.getString(3);
            String balance = res.getString(4);
            String account_number = res.getString(5);
            User user = new User();
            user.setId(id);
            user.setLast_name(last_name);
            user.setFirst_name(first_name);
            user.setPin(pin);
            user.setBalance((balance != null) ? Double.parseDouble(balance) : 0);
            user.setAccount_number(account_number);
            userList.add(user);
        }

        res.close();
        return userList;
    }


}
