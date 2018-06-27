package com.khalidelfaker.mobilecodingchallenge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListViewAdapter extends BaseAdapter {

    private Context context;
    ArrayList<?> objects;
    ArrayList<View> viewArrayList;

    public MyListViewAdapter(Context context, ArrayList<?> objects) {
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View cell_list_View;

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView==null){
            cell_list_View = inflater.inflate(R.layout.repos_element_ligne, null);
            TextView repo_name = cell_list_View.findViewById(R.id.repo_name);
            repo_name.setText(objects.get(position).toString());
            notifyDataSetChanged();
        }
        else {
            cell_list_View = convertView;
        }

        return cell_list_View;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


}
