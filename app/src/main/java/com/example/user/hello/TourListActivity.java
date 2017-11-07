package com.example.user.hello;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.user.hello.R.id.button2_tour;
import static java.lang.Double.parseDouble;

public class TourListActivity extends MainActivity{
    TourAdapter adapter;
    //목록
    ArrayList<TourMap> manylists=new ArrayList<TourMap>();
    //목록을 뿌려줄 리스트 뷰
    ListView list;
    //리스트 뷰 누르면 상단에 아이템 정보를 위에 표시한다.
    TextView textView;
    Intent intent;
    Button button,dis;
    double lat,lon;
    Double a=0.0,b=0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_list);

        list = (ListView) findViewById(R.id.listView01);
      //  textView = (TextView) findViewById(R.id.textView01);
        intent=getIntent();

        String lat01 = intent.getStringExtra("lat");
        String lon01 = intent.getStringExtra("lon");

        lat=parseDouble(lat01);
        lon = parseDouble(lon01);
        //거리순
        dis=(Button)findViewById(R.id.button2_tour);
      dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()== button2_tour) {
                    adapter.setDisAscSort();
                }
            }
        });

        //목록 만들꺼야
        manylists = TourMap;
        //
        for(int i=0; i<manylists.size();i++){
            double fee= getDistance(lat,lon,manylists.get(i).getLatitude(),manylists.get(i).getLongitude());
            double fee01=fee*0.001;
            String fee02=String.format("%.1f",fee01);
            double fee03=parseDouble(fee02);
/////////////////////////////////////////////////////////////////////////////////////////////////
            manylists.get(i).setDis(fee03);

        }
        //어뎁터 만들기
        adapter=new TourAdapter(this,manylists);
        list.setAdapter(adapter);
        //한 아이템 선택하면 실행 되는 거 야
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewclicked, int position, long id) {
              intent =new Intent();

                TourMap selectedlist = manylists.get(position);
                String message = "You clicked position" + position + "list is " + selectedlist.getName() + "로 목적지 설정 되었습니다.";
               // textView.setText(selectedlist.getName());

                Toast.makeText(TourListActivity.this, message, Toast.LENGTH_LONG).show();

                intent.putExtra("관광명소",selectedlist.getLongitude());

                intent.putExtra("latitude",selectedlist.getLatitude());
                setResult(RESULT_OK,intent);
                finish();


            }
        });
    }  public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c * 1000;
    }

}



