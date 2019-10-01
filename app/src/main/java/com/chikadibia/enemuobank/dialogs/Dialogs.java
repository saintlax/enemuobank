package com.chikadibia.enemuobank.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chikadibia.enemuobank.R;
import com.chikadibia.enemuobank.database.DatabaseHelper;
import com.chikadibia.enemuobank.helpers.Utils;
import com.chikadibia.enemuobank.interfaces.Callback;
import com.chikadibia.enemuobank.interfaces.Done;
import com.chikadibia.enemuobank.interfaces.TransferComplete;
import com.chikadibia.enemuobank.objects.CurrencyDenominations;
import com.chikadibia.enemuobank.objects.Product;
import com.chikadibia.enemuobank.objects.User;

import static com.chikadibia.enemuobank.database.DatabaseHelper.TABLE_CURRENCY_DENOMINATIONS;
import static com.chikadibia.enemuobank.database.DatabaseHelper.TABLE_USERS;
import static com.chikadibia.enemuobank.helpers.Utils.isBalanceAvailable;

public class Dialogs {
    Context context;
    AlertDialog.Builder alertDialog;
    public Dialogs(Context context){
        this.context = context;
    }
    public void fund_account( final Callback callback){
        alertDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_amount, null);
        final EditText amount = (EditText)view.findViewById(R.id.amount);
        amount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                String amt = amount.getText().toString();
                if(TextUtils.isEmpty(amt)){
                    Utils.popup(context,"Error","An Amount is required");
                }else{
                    callback.complete(amt);
                }
                return true;
            }
        });

        alertDialog.setTitle("Add Money to your account");
        alertDialog.setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String amt = amount.getText().toString();
                        if(TextUtils.isEmpty(amt)){
                            Utils.popup(context,"Error","An Amount is required");
                        }else{
                            callback.complete(amt);
                        }
                    }
                });
        alertDialog.show();
    }
    public void withdrawal( final Callback callback){
        alertDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_amount, null);
        final EditText amount = (EditText)view.findViewById(R.id.amount);
        amount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                String amt = amount.getText().toString();
                if(TextUtils.isEmpty(amt)){
                    Utils.popup(context,"Error","An Amount is required");
                }else{
                    callback.complete(amt);
                }
                return true;
            }
        });
        alertDialog.setTitle("Withdrawal");
        alertDialog.setView(view)
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String amt = amount.getText().toString();
                        if(TextUtils.isEmpty(amt)){
                            Utils.popup(context,"Error","An Amount is required");
                        }else{
                            callback.complete(amt);
                        }
                    }
                });
        alertDialog.show();
    }
    public void logout(final Done doneCallback){
        alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Logout");
        alertDialog.setMessage("Do you want to logout?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                doneCallback.done();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
               //do nothing
            }
        });
        alertDialog.show();
    }

    public void transfer(final TransferComplete callback){
        alertDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_transfer, null);
        final EditText amount = (EditText)view.findViewById(R.id.amount);
        final EditText account_number = (EditText)view.findViewById(R.id.account_number);
        account_number.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                String amt = amount.getText().toString();
                String acct = account_number.getText().toString();
                if(TextUtils.isEmpty(amt) || TextUtils.isEmpty(acct)){
                    Utils.popup(context,"Error","An Amount and account number is required");
                }else{
                    callback.complete(amt,acct);
                }
                return true;
            }
        });
        alertDialog.setTitle("Transfer");
        alertDialog.setView(view)
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String amt = amount.getText().toString();
                        String acct = account_number.getText().toString();
                        if(TextUtils.isEmpty(amt) || TextUtils.isEmpty(acct)){
                            Utils.popup(context,"Error","An Amount and account number is required");
                        }else{
                            callback.complete(amt,acct);
                        }
                    }
                });
        alertDialog.show();
    }

    public void add_user(){
        alertDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_add_user, null);
        final EditText first_name = (EditText)view.findViewById(R.id.first_name);
        final EditText last_name = (EditText)view.findViewById(R.id.last_name);
        final EditText balance = (EditText)view.findViewById(R.id.balance);
        final EditText pin = (EditText)view.findViewById(R.id.pin);
        final EditText account_number = (EditText)view.findViewById(R.id.account_number);

        alertDialog.setTitle("Add a User");
        alertDialog.setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String fname = first_name.getText().toString();
                        String lname = last_name.getText().toString();
                        String pinn = pin.getText().toString();
                        String balancee = balance.getText().toString();
                        String account_numberr = account_number.getText().toString();
                        if(TextUtils.isEmpty(fname) || TextUtils.isEmpty(lname) || TextUtils.isEmpty(pinn) || TextUtils.isEmpty(balancee) || TextUtils.isEmpty(account_numberr)){
                            Utils.popup(context,"Input Error","All fields are required");
                            return;
                        }
                        if(pinn.length() > 4){
                            Utils.popup(context,"PIN Error","PIN must not exceed 4 digits");
                            return;
                        }
                        User user = new User();
                        user.setLast_name(lname);
                        user.setFirst_name(fname);
                        user.setPin(pinn);
                        user.setBalance(Double.parseDouble(balancee));
                        user.setAccount_number(account_numberr);
                        DatabaseHelper db = new DatabaseHelper(context);

                        CurrencyDenominations currencyDenominations = null;
                        String query1 = "select * from "+TABLE_CURRENCY_DENOMINATIONS+" order by id desc limit 1";
                        for(CurrencyDenominations currencyDenominations1 : db.ATMBalance(query1)){
                            currencyDenominations = currencyDenominations1;
                        }
                        if(isBalanceAvailable(balancee,context,db,currencyDenominations)==false){
                            return;
                        }


                        String query = "select * from "+TABLE_USERS + " where pin='"+pinn+"'";
                        Cursor cursor = db.query(query);
                        if(cursor.getCount()==0){
                            query = "select * from "+TABLE_USERS + " where account_number='"+account_numberr+"'";
                            cursor = db.query(query);
                            if(cursor.getCount()==0){
                                if(db.addUser(user))
                                    Utils.popup(context,"Successful","New account created for "+user.last_name);
                                else
                                    Utils.popup(context,"Insert Error","Database Insert Error");
                            }else{
                                Utils.popup(context,"Account Number Error","This account number is aready in use");
                            }
                        }else{
                            //since there is no physical ATM card.. else this is not required
                            Utils.popup(context,"PIN Error","This PIN is already in use. Try again");
                        }


                    }
                });
        alertDialog.show();
    }




}
