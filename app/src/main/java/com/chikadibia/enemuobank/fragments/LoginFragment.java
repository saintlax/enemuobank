package com.chikadibia.enemuobank.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.chikadibia.enemuobank.R;
import com.chikadibia.enemuobank.adapters.LoginAdapter;
import com.chikadibia.enemuobank.database.DatabaseHelper;
import com.chikadibia.enemuobank.helpers.Utils;
import com.chikadibia.enemuobank.objects.User;

import java.util.ArrayList;
import java.util.List;

import static com.chikadibia.enemuobank.constants.Constants.MODE;
import static com.chikadibia.enemuobank.constants.Constants.SP_NAME;
import static com.chikadibia.enemuobank.constants.Constants.SP_USER_ID;
import static com.chikadibia.enemuobank.database.DatabaseHelper.TABLE_USERS;
import static com.chikadibia.enemuobank.helpers.Utils.OpenProductFragment;
import static com.chikadibia.enemuobank.helpers.Utils.formatPIN;

public class LoginFragment extends Fragment implements  GridView.OnItemClickListener {

    List<String> menu_name;
    List<Integer> menu_icon;
    Context context;
    User user;
    SharedPreferences sharedPreferences;
    DatabaseHelper db;
    TextView mOutput;
    Button mLogin,mCancel;
    String pin_string;
    int loginCount = 0;
    public LoginFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        context = getActivity();
        db = new DatabaseHelper(context);

        mOutput = (TextView)view.findViewById(R.id.output);
        mLogin = (Button)view.findViewById(R.id.login);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loginCount < 3)
                    fakeLogin();
                else
                    Utils.popup(context,"Access Denied","You have exceeded the number of login.\nClose the App and try again");
                loginCount++;
            }
        });
        mCancel = (Button)view.findViewById(R.id.cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pin_string = "";
                mOutput.setText(pin_string);
            }
        });
        menu_icon = new ArrayList<Integer>();
        menu_name = new ArrayList<String>();


        menu_icon.add(R.drawable.ic_zero);
        menu_name.add(getString(R.string.menu_zero));

        menu_icon.add(R.drawable.ic_one);
        menu_name.add(getString(R.string.menu_one));

        menu_icon.add(R.drawable.ic_two);
        menu_name.add(getString(R.string.menu_two));

        menu_icon.add(R.drawable.ic_three);
        menu_name.add(getString(R.string.menu_three));

        menu_icon.add(R.drawable.ic_four);
        menu_name.add(getString(R.string.menu_four));


        menu_icon.add(R.drawable.ic_five);
        menu_name.add(getString(R.string.menu_five));

        menu_icon.add(R.drawable.ic_six);
        menu_name.add(getString(R.string.menu_six));

        menu_icon.add(R.drawable.ic_seven);
        menu_name.add(getString(R.string.menu_seven));

        menu_icon.add(R.drawable.ic_eight);
        menu_name.add(getString(R.string.menu_eight));

        menu_icon.add(R.drawable.ic_nine);
        menu_name.add(getString(R.string.menu_nine));


        GridView gridview = (GridView)view.findViewById(R.id.gridView);
        LoginAdapter adapter = new LoginAdapter(getActivity(),menu_name,menu_icon);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        pin_string += formatPIN(context,menu_name.get(i));
        pin_string = pin_string.replace("null","");
        if(pin_string.length() < 5)
        mOutput.setText(pin_string);
    }

    private void fakeLogin(){
        //this is supposed to be handled by a web server or firebase or any API
        String query = "select * from "+TABLE_USERS+" where pin='"+pin_string+"'";
        Cursor cursor = db.query(query);
        if(cursor.getCount() > 0){
            user = db.user_data(query);
            if(user != null)
                OpenProductFragment(context,user);
            else
                Utils.popup(context,"Access Denied","User could not be initialized bbecause of your village people");
        }else{
            Utils.popup(context,"Access Denied","Invalid Pin");
        }
    }

}
