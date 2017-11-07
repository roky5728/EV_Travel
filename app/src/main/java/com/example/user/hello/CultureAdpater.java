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
 * Created by user on 2017-08-23.
 */

public class CultureAdpater extends BaseAdapter{
    ArrayList<CultureMap> lists = null;

    Context context;
    public CultureAdpater(Context context,ArrayList<CultureMap> savedata){
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

        CultureMap one = lists.get(position);
        /*
        if(one.getCodename().equals("도서관")){
                    ViewHolder.name.setText(one.getName());
                    ViewHolder.address.setText(one.getAddress());
                    ViewHolder.kor_info.setText(""+one.getEntrfree()+"km");
                }

*/

       // CultureMap one = lists.get(position); // 데이타 가져오기
        ViewHolder.name.setText(one.getName());
        ViewHolder.address.setText(one.getAddress());
        ViewHolder.kor_info.setText(""+one.getEntrfree()+"km");

        return convertView;

    }

    public ArrayList<CultureMap> return2(){return lists;}
    class ViewHolder{
        public TextView name;
        public TextView address;
        public TextView kor_info;

    }

    Comparator<CultureMap> nameAsc = new Comparator<CultureMap>() {
        @Override
        public int compare(CultureMap t0, CultureMap t1) {
            return t0.getName().compareTo(t1.getName());
        }


    };
    public void setNameAscSort(){
        Collections.sort(lists,nameAsc);
        this.notifyDataSetChanged();
    }

    Comparator<CultureMap> disAsc=new Comparator<CultureMap>() {
        @Override
        public int compare(CultureMap t0, CultureMap t1) {
            return t0.getEntrfree()<t1.getEntrfree()?-1:t0.getEntrfree()>t1.getEntrfree()?1:0;
        }


    };
    public void setDisAscSort(){
        Collections.sort(lists,disAsc);
        this.notifyDataSetChanged();

    }



}

