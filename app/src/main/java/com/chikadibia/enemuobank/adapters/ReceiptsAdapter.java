package com.chikadibia.enemuobank.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chikadibia.enemuobank.R;
import com.chikadibia.enemuobank.objects.Product;
import com.chikadibia.enemuobank.objects.Receipt;

import java.util.List;


public class ReceiptsAdapter extends BaseAdapter {
    Context context;
    List<Receipt> menu_name;
    public ReceiptsAdapter(Context act, List<Receipt> menu_name){
        this.context = act;
        this.menu_name = menu_name;
    }
    @Override
    public int getCount() {
        return menu_name.size();
    }

    @Override
    public String getItem(int position) {
        Receipt service = menu_name.get(position-1);
        return service.transaction_type;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.row_for_receipt,viewGroup,false);
        TextView transaction_type =(TextView)row.findViewById(R.id.transaction_type);
        transaction_type.setText("Transaction: "+menu_name.get(i).transaction_type);
        TextView amount =(TextView)row.findViewById(R.id.amount);
        amount.setText("Amount: "+menu_name.get(i).amount);
        TextView available_balance =(TextView)row.findViewById(R.id.available_balance);
        available_balance.setText("Available: "+menu_name.get(i).available_balance);
        TextView description =(TextView)row.findViewById(R.id.description);
        description.setText("Description: "+menu_name.get(i).description);
        TextView time =(TextView)row.findViewById(R.id.time);
        time.setText("Time: "+menu_name.get(i).time);
        TextView timestamp =(TextView)row.findViewById(R.id.timestamp);
        timestamp.setText("Date: "+menu_name.get(i).timestamp);
        return row;
    }
}
