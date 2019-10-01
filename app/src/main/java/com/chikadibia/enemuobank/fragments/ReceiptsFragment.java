package com.chikadibia.enemuobank.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chikadibia.enemuobank.R;
import com.chikadibia.enemuobank.adapters.ReceiptsAdapter;
import com.chikadibia.enemuobank.database.DatabaseHelper;
import com.chikadibia.enemuobank.helpers.Utils;
import com.chikadibia.enemuobank.objects.Receipt;
import com.chikadibia.enemuobank.objects.User;

import java.util.List;

import static com.chikadibia.enemuobank.constants.Constants.USER_ID;
import static com.chikadibia.enemuobank.database.DatabaseHelper.TABLE_RECEIPTS;
import static com.chikadibia.enemuobank.database.DatabaseHelper.TABLE_USERS;

public class ReceiptsFragment extends Fragment {

    Context context;
    User user;
    DatabaseHelper db;
    List<Receipt> receiptList;
    ListView listView;
    ReceiptsAdapter adapter;

    onLoggedIn loggedIn;
    public ReceiptsFragment(){

    }

    public interface onLoggedIn{
        public void loggedIn(User user);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            loggedIn = (onLoggedIn)context;
            loggedIn.loggedIn(user);
        }catch (ClassCastException e){
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receipts, container, false);
        context = getActivity();
        db = new DatabaseHelper(context);
        String query = "select * from "+TABLE_USERS+" where id='"+getArguments().getString(USER_ID)+"'";
        user = db.user_data(query);
        if(user == null){
            Utils.popup(context,"Access Denied","Your account could not be verified");
            return null;
        }
        loggedIn.loggedIn(user);
        initReceipts(view);
        return view;
    }

    private void initReceipts(View view) {
        String query = "select * from "+TABLE_RECEIPTS + " where user_id="+user.id+" order by id desc";
        receiptList = db.ReceiptList(query);
        listView = (ListView)view.findViewById(R.id.listview);
        adapter = new ReceiptsAdapter(context, receiptList);;
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Receipt receipt = receiptList.get(position);
                //do nothing
            }
        });
    }

}
