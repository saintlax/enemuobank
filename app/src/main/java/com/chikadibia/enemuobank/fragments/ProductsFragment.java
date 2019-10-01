package com.chikadibia.enemuobank.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.chikadibia.enemuobank.R;
import com.chikadibia.enemuobank.adapters.ProductsAdapter;
import com.chikadibia.enemuobank.database.DatabaseHelper;
import com.chikadibia.enemuobank.dialogs.Dialogs;
import com.chikadibia.enemuobank.helpers.Utils;
import com.chikadibia.enemuobank.interfaces.Callback;
import com.chikadibia.enemuobank.interfaces.TransferComplete;
import com.chikadibia.enemuobank.objects.CurrencyDenominations;
import com.chikadibia.enemuobank.objects.Product;
import com.chikadibia.enemuobank.objects.Receipt;
import com.chikadibia.enemuobank.objects.User;

import java.util.ArrayList;
import java.util.List;

import static com.chikadibia.enemuobank.constants.Constants.ACCOUNT_NUMBER;
import static com.chikadibia.enemuobank.constants.Constants.BALANCE;
import static com.chikadibia.enemuobank.constants.Constants.DATE_UPDATED;
import static com.chikadibia.enemuobank.constants.Constants.FIVE_HUNDREDS;
import static com.chikadibia.enemuobank.constants.Constants.NAIRA;
import static com.chikadibia.enemuobank.constants.Constants.ONE_THOUSANDS;
import static com.chikadibia.enemuobank.constants.Constants.PIN;
import static com.chikadibia.enemuobank.constants.Constants.TOTAL;
import static com.chikadibia.enemuobank.database.DatabaseHelper.TABLE_CURRENCY_DENOMINATIONS;
import static com.chikadibia.enemuobank.database.DatabaseHelper.TABLE_RECEIPTS;
import static com.chikadibia.enemuobank.database.DatabaseHelper.TABLE_USERS;
import static com.chikadibia.enemuobank.helpers.Utils.OpenReceiptsFragment;
import static com.chikadibia.enemuobank.helpers.Utils.getTime;
import static com.chikadibia.enemuobank.helpers.Utils.getTimestamp;
import static com.chikadibia.enemuobank.helpers.Utils.isBalanceAvailable;

public class ProductsFragment extends Fragment {

    Context context;
    User user;
    DatabaseHelper db;
    List<Product> productList;
    ListView listView;
    ProductsAdapter adapter;
    TextView mBalance;
    Dialogs dialogs;
    CurrencyDenominations currencyDenominations;
    public ProductsFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        context = getActivity();
        dialogs = new Dialogs(context);
        db = new DatabaseHelper(context);
        String query = "select * from "+TABLE_USERS+" where pin='"+getArguments().getString(PIN)+"'";
        user = db.user_data(query);
        if(user == null){
            Utils.popup(context,"Access Denied","Your account could not be verified");
            return null;
        }
        mBalance = (TextView)view.findViewById(R.id.balance);
        mBalance.setText(Html.fromHtml(NAIRA+" "+user.balance));
        initATM();
        initProducts(view);
        return view;
    }

    private void initATM() {
        String query = "select * from "+TABLE_CURRENCY_DENOMINATIONS+" order by id desc limit 1";
        for(CurrencyDenominations currencyDenominations1 : db.ATMBalance(query)){
            currencyDenominations = currencyDenominations1;
        }
        if(currencyDenominations != null) {
            ATMLedger(currencyDenominations);
        }else
            Utils.popup(context,"EMPTY ATM","Temporary Unable to dispense cash");
    }

    private void ATMLedger(CurrencyDenominations currencyDenominations) {
        String ATMData = Html.fromHtml("500s: " + currencyDenominations.five_hundreds + " pieces <br>" + "1000s: " + currencyDenominations.one_thousands + " pieces <br>Total: " +NAIRA+" "+ currencyDenominations.total)+"";
        Utils.popup(context, "ATM Balance", ATMData);
    }

    public boolean isMultiple(Double amount){
        boolean status = true;
        boolean isDiv500 = amount % 500 == 0;
        boolean isDiv1000 = amount % 100 == 0;
        if(!isDiv500){
            status = false;
        }
        if(!isDiv1000){
            status = false;
        }
        return status;
    }

    private void initProducts(View view) {
        productList = new ArrayList<>();
        Product product = new Product();
        product.setId(1);
        product.setName(getString(R.string.withdrawal));
        product.setType("Internal Action");
        product.setIcon(R.drawable.ic_money);
        productList.add(product);


        product = new Product();
        product.setId(2);
        product.setName(getString(R.string.transfer));
        product.setType("Internal Action");
        product.setIcon(R.drawable.ic_arrows);
        productList.add(product);

        product = new Product();
        product.setId(3);
        product.setName(getString(R.string.check_balance));
        product.setType("Internal Action");
        product.setIcon(R.drawable.ic_payment);
        productList.add(product);


        product = new Product();
        product.setId(4);
        product.setName(getString(R.string.fund_account));
        product.setType("Internal Action");
        product.setIcon(R.drawable.ic_add);
        productList.add(product);


        product = new Product();
        product.setId(5);
        product.setName(getString(R.string.my_receipts));
        product.setType("Internal Action");
        product.setIcon(R.drawable.ic_receipt);
        productList.add(product);


        listView = (ListView)view.findViewById(R.id.listview);
        adapter = new ProductsAdapter(context, productList);;
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Product product = productList.get(position);
                if(product.name.equalsIgnoreCase(getString(R.string.my_receipts))){
                    get_receipts(user);
                }
                if(product.name.equalsIgnoreCase(getString(R.string.check_balance))){
                    check_balance(user);
                }
                if(product.name.equalsIgnoreCase(getString(R.string.withdrawal))){
                    dialogs.withdrawal(new Callback() {
                        @Override
                        public void complete(String amount) {
                            withdraw_fund(amount);
                        }
                    });
                }
                if(product.name.equalsIgnoreCase(getString(R.string.fund_account))){
                    dialogs.fund_account(new Callback() {
                        @Override
                        public void complete(String amount) {
                            fund_account(amount);
                        }
                    });
                }
                if(product.name.equalsIgnoreCase(getString(R.string.transfer))){
                    dialogs.transfer(new TransferComplete() {
                        @Override
                        public void complete(String amount, String account_number) {
                            transfer_fund(amount,account_number);
                        }
                    });
                }
            }
        });
    }
    public void check_balance(User user){
        String msg = "Account Name: "+ user.last_name+" "+user.first_name+"<br>";
        msg += "Account Number: "+user.account_number+"<br>";
        msg += "Balance: "+NAIRA+" "+user.balance;
        Utils.popup(context,"Balance Enquiry",Html.fromHtml(msg)+"");
    }
    public void transfer_fund(String amount, String account_number){
        Double amountt = Double.parseDouble(amount);
        if(user.balance > 0 && user.balance >= amountt){
            String query = "select * from "+TABLE_USERS+ " where account_number='"+account_number+"' order by id desc limit 1";
            User receiver = db.user_data(query);
            if(receiver != null){
                receiver.balance += amountt;
                user.balance -=amountt;
                //update user account
                ContentValues contentValues = new ContentValues();
                contentValues.put(BALANCE,user.balance);
                if(db.do_edit(TABLE_USERS,contentValues,ACCOUNT_NUMBER ,user.account_number) != -1){
                    //update receiver account
                    contentValues = new ContentValues();
                    contentValues.put(BALANCE,receiver.balance);
                    if(db.do_edit(TABLE_USERS,contentValues,ACCOUNT_NUMBER ,receiver.account_number) != -1){
                        //add Receipt
                        Receipt r = new Receipt();
                        r.setUser_id(user.id);
                        r.setTimestamp(getTimestamp());
                        r.setTime(getTime());
                        r.setDescription("You TRANSFERRED N"+amount +" to "+ receiver.last_name+" "+receiver.first_name);
                        r.setAvailable_balance(Html.fromHtml(NAIRA+user.balance)+"");
                        r.setAmount(Html.fromHtml(NAIRA+amount)+"");
                        r.setTransaction_type("TRANSFER");
                        if(db.addReceipt(r)){
                            mBalance.setText(Html.fromHtml(NAIRA+" "+user.balance));
                            Utils.popup(context,"Successful","Transfer of N"+amount+" to "+receiver.last_name+" "+receiver.first_name+" was successful");
                        }else{
                            Utils.popup(context,"Receipt Error","Receipt could not be generated");
                        }
                    }else{
                        Utils.popup(context,"Account Error","Receiver account could not be updated");
                    }
                }else{
                    Utils.popup(context,"Account Error","Your account could not be updated");
                }

            }else{
                Utils.popup(context,"Account Error","The Account number does not exist");
            }
        }else{
            Utils.popup(context,"Error","You have insufficient balance to complete this transaction");
        }
    }
    public void fund_account(String amount){
        if(isBalanceAvailable(amount,context,db,currencyDenominations)==false){
            return;
        }
        ContentValues contentValues = new ContentValues();
        Double amountt = Double.parseDouble(amount) + user.balance;
        contentValues.put("balance",amountt);
        if(db.do_edit(TABLE_USERS,contentValues,PIN ,user.pin) != -1){
            //add Receipt
            Receipt r = new Receipt();
            r.setUser_id(user.id);
            r.setTimestamp(getTimestamp());
            r.setTime(getTime());
            r.setDescription("You funded your account with N"+amount);
            r.setAvailable_balance(Html.fromHtml(NAIRA+user.balance)+"");
            r.setAmount(Html.fromHtml(NAIRA+amount)+"");
            r.setTransaction_type("SELF FUNDING");
            if(db.addReceipt(r)){
                mBalance.setText(Html.fromHtml(NAIRA+" "+amountt));
                user.balance = amountt;
            }else{
                Utils.popup(context,"Receipt Error","Receipt could not be generated");
            }
        }else{
            Utils.popup(context,"Error","Account could not be funded");
        }
    }


    public void withdraw_fund(String amount){
        ContentValues contentValues;
        if(user.balance >= Double.parseDouble(amount) && user.balance > 0){
            if(Double.parseDouble(amount) <= currencyDenominations.total){
                Double amountt = Double.parseDouble(amount);

                if(isMultiple(amountt)==false){
                    Utils.popup(context,"Withdrawal Error","Amount must be in multiples of 500s or 1000s");
                    return;
                }
                contentValues = new ContentValues();
                currencyDenominations.total -= amountt;
                contentValues.put(TOTAL,currencyDenominations.total);
                //2 pieces of 500s equals N1,000, our formula will be
                int pay_amount = Integer.parseInt(amount);
                int required_five_hundreds_to_pay = (int) ((2 * pay_amount)/1000);

                //2 pieces of 1000s equals N2,000, our formula will be
                int required_one_thousands_pay = (int) ((2 * pay_amount)/2000);
                //check denomination to pay user
                if(required_five_hundreds_to_pay <= currencyDenominations.five_hundreds){
                    currencyDenominations.five_hundreds -= required_five_hundreds_to_pay;
                }else if(required_one_thousands_pay <= currencyDenominations.one_thousands){
                    currencyDenominations.one_thousands -= required_one_thousands_pay;
                }else{
                    Utils.popup(context,"Try again","Enter an amount in multiples of 500s or 1000s");
                    return;
                }

                contentValues.put(FIVE_HUNDREDS,currencyDenominations.five_hundreds);
                contentValues.put(ONE_THOUSANDS,currencyDenominations.one_thousands);
                contentValues.put(DATE_UPDATED,getTimestamp());
                if(db.do_insert(TABLE_CURRENCY_DENOMINATIONS,contentValues)){
                    //update user account
                    contentValues = new ContentValues();
                    contentValues.put(BALANCE,amountt);
                    if(db.do_edit(TABLE_USERS,contentValues,PIN ,user.pin) != -1){

                        //add Receipt
                        Receipt r = new Receipt();
                        r.setUser_id(user.id);
                        r.setTimestamp(getTimestamp());
                        r.setTime(getTime());
                        r.setDescription("You Withdrew N"+amount+" from your account");
                        r.setAvailable_balance(Html.fromHtml(NAIRA+user.balance)+"");
                        r.setAmount(Html.fromHtml(NAIRA+amount)+"");
                        r.setTransaction_type("WITHDRAWAL");
                        if(db.addReceipt(r)){
                            user.balance -= amountt;
                            mBalance.setText(Html.fromHtml(NAIRA+" "+user.balance));
                            ATMLedger(currencyDenominations);
                        }else{
                            Utils.popup(context,"Receipt Error","Receipt could not be generated");
                        }
                    }else{
                        Utils.popup(context,"Error","Balance could not be updated");
                    }
                }
            }else{
                Utils.popup(context,"Unable to Dispense","Your withdrawal amount is larger than what our ATM can dispense..\nKindly use the banking hall..");
            }
        }else{
            Utils.popup(context,"Insufficient Funds","You currently do not have enough funds for this transaction\nFund your account and try again.");
        }
    }

    public void get_receipts(User user){
        String query = "select * from "+TABLE_RECEIPTS + " where user_id="+user.id;
        List<Receipt> receiptList = db.ReceiptList(query);
        if(receiptList != null){
            OpenReceiptsFragment(context,user);
        }else{
            Utils.popup(context,"No Receipt","No Receipts has been logged to your account");
        }
    }
}
