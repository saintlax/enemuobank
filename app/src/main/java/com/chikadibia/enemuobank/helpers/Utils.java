package com.chikadibia.enemuobank.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.chikadibia.enemuobank.MainActivity;
import com.chikadibia.enemuobank.R;
import com.chikadibia.enemuobank.database.DatabaseHelper;
import com.chikadibia.enemuobank.fragments.LoginFragment;
import com.chikadibia.enemuobank.fragments.ProductsFragment;
import com.chikadibia.enemuobank.fragments.ReceiptsFragment;
import com.chikadibia.enemuobank.objects.CurrencyDenominations;
import com.chikadibia.enemuobank.objects.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.chikadibia.enemuobank.R.string.menu_zero;
import static com.chikadibia.enemuobank.R.string.menu_one;
import static com.chikadibia.enemuobank.R.string.menu_two;
import static com.chikadibia.enemuobank.R.string.menu_three;
import static com.chikadibia.enemuobank.R.string.menu_four;
import static com.chikadibia.enemuobank.R.string.menu_five;
import static com.chikadibia.enemuobank.R.string.menu_six;
import static com.chikadibia.enemuobank.R.string.menu_seven;
import static com.chikadibia.enemuobank.R.string.menu_eight;
import static com.chikadibia.enemuobank.R.string.menu_nine;
import static com.chikadibia.enemuobank.constants.Constants.PIN;
import static com.chikadibia.enemuobank.constants.Constants.USER_ID;
import static com.chikadibia.enemuobank.database.DatabaseHelper.TABLE_USERS;

public class Utils {
    public static void OpenLoginFragment(Context context){
        ((MainActivity)context ).getSupportActionBar().setTitle("Enter your PIN");
        LoginFragment fragment= new LoginFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
    }
    static public  String getTimestamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        return sdf.format(new Date());

    }
    static public  String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-ss");
        return sdf.format(new Date());

    }
    public static int formatPIN(Context context,String input){
        if(input.equalsIgnoreCase(context.getResources().getString(menu_zero))){
            return 0;
        }
        if(input.equalsIgnoreCase(context.getResources().getString(menu_one))){
            return 1;
        }
        if(input.equalsIgnoreCase(context.getResources().getString(menu_two))){
            return 2;
        }
        if(input.equalsIgnoreCase(context.getResources().getString(menu_three))){
            return 3;
        }
        if(input.equalsIgnoreCase(context.getResources().getString(menu_four))){
            return 4;
        }
        if(input.equalsIgnoreCase(context.getResources().getString(menu_five))){
            return 5;
        }
        if(input.equalsIgnoreCase(context.getResources().getString(menu_six))){
            return 6;
        }
        if(input.equalsIgnoreCase(context.getResources().getString(menu_seven))){
            return 7;
        }
        if(input.equalsIgnoreCase(context.getResources().getString(menu_eight))){
            return 8;
        }
        if(input.equalsIgnoreCase(context.getResources().getString(menu_nine))){
            return 9;
        }
        return 0;
    }
    public static void popup(Context context,String title,String message){
        AlertDialog.Builder  builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Got It", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing
            }
        });
        builder.show();
    }
    public static void OpenProductFragment(Context context,User user){
        ((MainActivity)context ).getSupportActionBar().setTitle(user.last_name+" "+user.first_name);
        ProductsFragment fragment = new ProductsFragment();
        Bundle data = new Bundle();
        data.putString(PIN, user.pin+"");
        fragment.setArguments(data);
        android.support.v4.app.FragmentTransaction fragmentTransaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
    }
    public static void OpenReceiptsFragment(Context context,User user){
        ((MainActivity)context ).getSupportActionBar().setTitle("My Receipts");
        ReceiptsFragment fragment = new ReceiptsFragment();
        Bundle data = new Bundle();
        data.putString(USER_ID, user.id+"");
        fragment.setArguments(data);
        android.support.v4.app.FragmentTransaction fragmentTransaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
    }

    public static boolean isBalanceAvailable(String amount,Context context, DatabaseHelper db, CurrencyDenominations currencyDenominations) {
        Double amountt = Double.parseDouble(amount);
        //when test fund exceed ATM vault
        if(amountt > currencyDenominations.total){
            Utils.popup(context,"Max Balance","The amount you entered is larger than the ATM Vault.\nYour Balance MUST be less than N"+currencyDenominations.total);
            return false;
        }
        String query = "select * from "+TABLE_USERS+" order by id desc";
        List<User> userList = db.UsersList(query);
        if(userList == null){
            return true;
        }
        Double vault_total = 0.0;
        for(User user: userList){
            vault_total += user.balance;
        }
        //when users balances exceed/equal ATM Vault
        if(vault_total >= currencyDenominations.total){
            Utils.popup(context,"Maxed Out Users","Users has exhausted the N"+currencyDenominations.total+" loaded on this ATM");
            return false;
        }
        return true;
    }

}
