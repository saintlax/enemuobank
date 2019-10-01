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
import java.util.List;


public class ProductsAdapter extends BaseAdapter {
    Context context;
    List<Product> menu_name;
    public ProductsAdapter(Context act, List<Product> menu_name){
        this.context = act;
        this.menu_name = menu_name;
    }
    @Override
    public int getCount() {
        return menu_name.size();
    }

    @Override
    public String getItem(int position) {
        Product service = menu_name.get(position-1);
        return service.name;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.row_for_custom_menu,viewGroup,false);
        ImageView imageView = (ImageView)row.findViewById(R.id.menu_icon);
        imageView.setImageResource(menu_name.get(i).icon);
        TextView name =(TextView)row.findViewById(R.id.menu_name);
        name.setText(menu_name.get(i).name);
        return row;
    }
}
