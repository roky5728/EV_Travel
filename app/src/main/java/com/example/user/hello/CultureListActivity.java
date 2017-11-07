package com.example.user.hello;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static java.lang.Double.parseDouble;

public class CultureListActivity extends MainActivity {
    CultureAdpater adapter;
    ArrayList<CultureMap> manylists=new ArrayList<CultureMap>();
    //목록을 뿌려줄 리스트 뷰
    ListView list;
    //리스트 뷰 누르면 상단에 아이템 정보를 위에 표시한다.
    TextView textView;

    Intent intent;
    Button button,button2;
    double lat,lon;
    Double a=0.0,b=0.0;
    public static final int REQUEST_CulInfo = 105;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_culture_list);
        list = (ListView) findViewById(R.id.listView02);
        intent=getIntent();

        String lat01 = intent.getStringExtra("lat");
        String lon01 = intent.getStringExtra("lon");

        lat=parseDouble(lat01);
        lon = parseDouble(lon01);
        //이름순
        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.button){
                    adapter.setNameAscSort();
                }
            }
        });

        //거리순
        button2=(Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.button2) {
                    adapter.setDisAscSort();
                }
            }
        });


        manylists = CultureMap;
        //
        for(int i=0; i<manylists.size();i++){
            double fee= getDistance(lat,lon,manylists.get(i).getLatitude(),manylists.get(i).getLongitude());
            double fee01=fee*0.001;
            String fee02=String.format("%.1f",fee01);
            double fee03=parseDouble(fee02);
/////////////////////////////////////////////////////////////////////////////////////////////////
            manylists.get(i).setEntrfree(fee03);

        }
        //어뎁터 만들기
        adapter=new CultureAdpater(this,manylists);
        list.setAdapter(adapter);
        //한 아이템 선택하면 실행 되는 거 야
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewclicked, int position, long id) {
                intent = new Intent(getBaseContext(), CultureInfoActivity.class);

                CultureMap selectedlist = manylists.get(position);

                intent.putExtra("문화", selectedlist.getName());

                intent.putExtra("주소", selectedlist.getAddress());
                intent.putExtra("x", selectedlist.getLatitude());
                intent.putExtra("y", selectedlist.getLongitude());
                intent.putExtra("phone",selectedlist.getPhone());
                intent.putExtra("img",selectedlist.getImg());
                intent.putExtra("codename",selectedlist.getCodename());
                intent.putExtra("seat",selectedlist.getSeat());
                intent.putExtra("open",selectedlist.getOpen());
                intent.putExtra("entrfree",selectedlist.getEntrfree());
                intent.putExtra("closed",selectedlist.getClosed());
                intent.putExtra("fee",selectedlist.getFee());
                intent.putExtra("fax",selectedlist.getFax());
                intent.putExtra("homepage",selectedlist.getHomepage());
                intent.putExtra("fee",selectedlist.getFee());

                startActivityForResult(intent,REQUEST_CulInfo);



            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == 1) {
            Double value = intent.getDoubleExtra("x",0.0);
            Double Y=intent.getDoubleExtra("y",0.0);

            Intent resultIntent = new Intent();

            resultIntent.putExtra("x",value);
            resultIntent.putExtra("y",Y);
            setResult(1, resultIntent);
            finish();

        } else {

        }
    }

    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
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




