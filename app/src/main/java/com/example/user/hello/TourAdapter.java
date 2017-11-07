package com.example.user.hello;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by user on 2017-08-18.
 */

public class TourAdapter extends BaseAdapter{

    ArrayList<TourMap> lists =null;

    Context context;
    public TourAdapter(Context context,ArrayList<TourMap> savedata){
        this.context=context;
        this.lists=savedata;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    ViewHolder ViewHolder=new ViewHolder();

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if(convertView == null){
            ViewHolder = new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
            ViewHolder.name=(TextView)convertView.findViewById(R.id.name);
            ViewHolder.address=(TextView)convertView.findViewById(R.id.address);
            ViewHolder.kor_info=(TextView)convertView.findViewById(R.id.kor_info);
            convertView.setTag(ViewHolder);
        }else{
            ViewHolder=(ViewHolder)convertView.getTag();
        }
       TourMap one = lists.get(position);
        ViewHolder.name.setText(one.getName());
        ViewHolder.address.setText(one.getAddress());
        ViewHolder.kor_info.setText(""+one.getDis()+"km");

        return convertView;

    }
    class ViewHolder{
        public TextView name;
        public TextView address;
        public TextView kor_info;

    }

    Comparator<TourMap> disAsc=new Comparator<TourMap>() {

        @Override
        public int compare(TourMap t0, TourMap t1) {
            return t0.getDis()<t1.getDis()?-1:t0.getDis()>t1.getDis()?1:0;
        }
    };
    public void setDisAscSort(){
        Collections.sort(lists,disAsc);
        this.notifyDataSetChanged();

    }

}
