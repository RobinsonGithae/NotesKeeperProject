package com.example.noteskeeper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.noteskeeper.R;

import java.util.ArrayList;



public class CategoryAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> categoryList;

    public CategoryAdapter(Context context, ArrayList<String> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.category_item, parent, false);

            holder = new ViewHolder();
            holder.category_title = convertView.findViewById(R.id.txv_category_spinner_name);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.category_title.setText(categoryList.get(position));
        return convertView;
    }

    private class ViewHolder{
        TextView category_title;
    }

    public void clear(){
        final int size = categoryList.size();
        if(size > 0){
            for(int i = 0; i <= size; i++){
                categoryList.remove(i);
            }

        notifyDataSetInvalidated();

        }
    }
}
