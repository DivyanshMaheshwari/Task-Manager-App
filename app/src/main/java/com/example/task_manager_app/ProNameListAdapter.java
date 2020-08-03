package com.example.task_manager_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ProNameListAdapter extends BaseAdapter {

    Context context;
    TextView projectname,projectdes;
    public ArrayList<ProListView> arrayList;

    public ProNameListAdapter(Context context, ArrayList<ProListView> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @Override
    public int getCount() {

        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View View, ViewGroup parent) {
        View = LayoutInflater.from(context).inflate(R.layout.activity_home_page_pro_list,parent,false);
        projectname=View.findViewById(R.id.liTextview);
        projectdes=View.findViewById(R.id.projectDescription);
        projectname.setText(arrayList.get(position).getProjectName());
        projectdes.setText(arrayList.get(position).getProjectdescription());
        return View;
    }
}