package com.chikadibia.enemuobank;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.chikadibia.enemuobank.database.DatabaseHelper;
import com.chikadibia.enemuobank.dialogs.Dialogs;
import com.chikadibia.enemuobank.fragments.LoginFragment;
import com.chikadibia.enemuobank.fragments.ProductsFragment;
import com.chikadibia.enemuobank.fragments.ReceiptsFragment;
import com.chikadibia.enemuobank.helpers.Utils;
import com.chikadibia.enemuobank.interfaces.Callback;
import com.chikadibia.enemuobank.interfaces.Done;
import com.chikadibia.enemuobank.objects.Product;
import com.chikadibia.enemuobank.objects.User;

import static com.chikadibia.enemuobank.helpers.Utils.OpenLoginFragment;
import static com.chikadibia.enemuobank.helpers.Utils.OpenProductFragment;

public class MainActivity extends AppCompatActivity implements ReceiptsFragment.onLoggedIn{

    Context context;
    DatabaseHelper db;
    Dialogs dialogs;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = MainActivity.this;
        dialogs = new Dialogs(context);
        db = new DatabaseHelper(context);
       // db.hasTestUsers();
        db.addMoneyToATM(34,21);
        OpenLoginFragment(context);

    }

    @Override
    public void onBackPressed() {
        android.support.v4.app.Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if(currentFragment instanceof LoginFragment){
            super.onBackPressed();
        }else if(currentFragment instanceof ReceiptsFragment){
            if(user != null)
                OpenProductFragment(context,user);
            else
                Utils.popup(context,"Access Denied","The interface failed to return a user. Village people i guess..hahaa");
        }else{
            dialogs.logout(new Done() {
                @Override
                public void done() {
                    OpenLoginFragment(context);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_user) {
            dialogs.add_user();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loggedIn(User user) {
        this.user = user;
    }
}
