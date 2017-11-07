package com.example.user.hello;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static java.lang.Double.parseDouble;

public class MainActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {
    //네비게이션드로어

    //tmapview
    TMapView tmapview;
    TMapData tMapData = new TMapData();
    TMapGpsManager tmapgps;
    TMapPoint start, end, middle, tpoint;
    RadioGroup typeView;
    boolean m_bTrackingMode = true;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    LocationManager locationManager;
    double longitude = 0.0, latitude = 0.0;
    String lat, lon = "";
    //timer
    AlarmManager mAlarmMgr;
    //POI
    EditText keywordView;
    ListView POIlistView;
    ArrayAdapter<POI> mAdapter;
    boolean ismarker = true;
    POI poi;
    //관광지 마커
    boolean isConnection = false;
    public ArrayList<TourMap> TourMap = new ArrayList<TourMap>();

    // public ArrayList<SampleDTO> SampleDTO = new ArrayList<SampleDTO>();

    public ArrayList<StationMap> StationMap = new ArrayList<StationMap>();
    public ArrayList<CultureMap> CultureMap = new ArrayList<CultureMap>();

    public static final int REQUEST_TourList = 101;
    public static final int REQUEST_CulList = 102;
    public static final int REQUEST_StaList = 103;
    public static final int REQUEST_Station = 104;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  startActivity(new Intent(this,SplashActivity.class));
        setContentView(R.layout.activity_main);
        chkGpsService();
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.map_view);


        tmapview = new TMapView(this);

        tmapgps = new TMapGpsManager(this);
       // tmapgps.setProvider(TMapGpsManager.NETWORK_PROVIDER);
        /////////////////////////////
       // tmapgps.getLocation(); // 현재위치의 좌표 반환

        tmapgps.setMinTime(1000);
        tmapgps.setMinDistance(2);
        tmapgps.setProvider(TMapGpsManager.NETWORK_PROVIDER); //네트워크로 위치 받기
        tmapgps.OpenGps();

        if (tmapgps.getLocation() == null) { //네트워크로 위치  받으려고 했는데 잘 안받아진다면
            tmapgps.setProvider(TMapGpsManager.GPS_PROVIDER); //gps로 받아야겠당 야외에서는 얘로
            tmapgps.OpenGps();
        }
        tpoint = tmapgps.getLocation();
        start=new TMapPoint(tpoint.getLatitude(),tpoint.getLongitude());

        tmapview.setSKPMapApiKey("17fb9c29-cc6b-3519-b6ed-de0874d757aa");

        tmapview.setCompassMode(true);
        tmapview.setIconVisibility(true);
        tmapview.setZoomLevel(15);
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);
        //  tmapview.setTrackingMode(true);

        tmapview.setOnApiKeyListener(new TMapView.OnApiKeyListenerCallback() {
            @Override
            public void SKPMapApikeySucceed() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setupMap();
                    }
                });
            }

            @Override
            public void SKPMapApikeyFailed(String s) {

            }
        });

        tmapview.setSightVisible(true);
        relativeLayout.addView(tmapview);
        //알람
        mAlarmMgr=(AlarmManager)getSystemService(ALARM_SERVICE);

        //POI 64
        keywordView = (EditText) findViewById(R.id.editText2);//키워드 검색하는 창
        POIlistView = (ListView) findViewById(R.id.POIlistView);//키워드 검색했을 때 리스트 보여주고 ->클릭하면 그 근처로 이동
        mAdapter = new ArrayAdapter<POI>(this, android.R.layout.simple_list_item_1);
        POIlistView.setAdapter(mAdapter);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //충전 완료 십분전 알람
        Button charge=(Button)findViewById(R.id.ChargeBtn);
        charge.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //이 버튼 누르면 충전 완료 10분 전 알람 줄꺼니깜 20분 후로 알람 주면 되겠징 .....
                //완전 방전 된 상태에서 30분 걸리니깜 ............. 그냥 15분 후 알람 주는게 낫겠당
                AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);

                dialog  .setTitle("충전 알람 설정")
                        .setMessage("충전 알람이 설정 되었습니다. 충전 완료 되기 10분전 알려드리겠습니다.")

                        .setNeutralButton("확인했습니다.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "충전 알람이 설정되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create().show();
                setAlarm_charge();
            }
        });



        Button btn01 = (Button) findViewById(R.id.tourBtn);

        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TourListActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);//현재위치 보내기
                startActivityForResult(intent, REQUEST_TourList);

            }
        });
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Button btn02 = (Button) findViewById(R.id.culBtn);

        btn02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CultureListActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);//현재위치 보내기
                startActivityForResult(intent, REQUEST_CulList);

            }
        });

        Button btn03 = (Button) findViewById(R.id.stationBtn);

        btn03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), StationListActivity.class);
                //현재 위치 보내기
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);//현재위치 보내기
                startActivityForResult(intent, REQUEST_StaList);

              ///////////////////////////////////////////////////////////////////////////


            }
        });



        //POI 95
       Button btn = (Button) findViewById(R.id.btn_search);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                POIlistView.setVisibility(View.VISIBLE);


                searchPOI();
            }
        });
        POIlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                POI poi = (POI) POIlistView.getItemAtPosition(position);

                //  poi.set;
                moveMap(poi.item.getPOIPoint().getLatitude(), poi.item.getPOIPoint().getLongitude());
                // tmapview.setCenterPoint(poi.item.getPOIPoint().getLatitude(), poi.item.getPOIPoint().getLongitude());
            }
        }); //poi 검색해서 클릭하면 지도 옮겨가서 그 위치 보여주기
        //줌 확대 zoom
        Button btn04 = (Button) findViewById(R.id.btn_plus);

        btn04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmapview.MapZoomIn();

            }
        });

        //줌 축소 zoom
        Button btn05 = (Button) findViewById(R.id.btn_minus);

        btn05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmapview.MapZoomOut();

            }
        });

        //현재위치로 가는 버튼
        Button btn07=(Button) findViewById(R.id.btn_set);

        btn07.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                tmapview.setTrackingMode(true);

            }
        });


        //경유지 버튼
        Button btn_pass= (Button)findViewById(R.id.passBtn);
        btn_pass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                /////////////////////////////////////////////////////////////////////////
                passRoute();
            }
        });





        getTour();
        addMarker();
        // sample.setText("dfdf");
        getStation();
        addMarker02();
        getCulture();
        addMarker04();

/*
        try{
            SampleDTO=parser.apiParserSearch();
        }catch (Exception e){
            e.printStackTrace();
        }
        for(SampleDTO entity : SampleDTO){
            tv.setText(entity.getName());
            tv.setText("@@");
        }
*/

    }

    // 좌표 찍는 거 이거 바꿔야됑 위치 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_TourList) {
            if (resultCode == RESULT_OK) {

                Double ex = intent.getDoubleExtra("관광명소", 0.0);
                Double ex01 = intent.getDoubleExtra("latitude", 0.0);

                end = new TMapPoint(ex01, ex);
                searchRoute(start,end);
                setAlarm();
            }

        } else if (requestCode == REQUEST_StaList) {
            if (resultCode == 1) {

                Double ex = intent.getDoubleExtra("x", 0.0);
                Double ex01 = intent.getDoubleExtra("y", 0.0);
                end=new TMapPoint(ex,ex01);
                searchRoute(start,end);
                setAlarm();

            }
        }else if(requestCode == REQUEST_CulList){
            if(resultCode==1){
                Double x = intent.getDoubleExtra("x",0.0);
                Double y = intent.getDoubleExtra("y",0.0);
                end = new TMapPoint(x,y);
                searchRoute(start,end);
                setAlarm();
            }
        }
    }

    //GPS 설정 체크
    private boolean chkGpsService() {
        AlertDialog AlertDialog;
        String gps = android.provider.Settings.Secure.getString(getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        Log.d(gps, "aaaa");
        if (!(gps.matches(".*gps.*") && gps.matches(".*network.*"))) {

            // GPS OFF 일때 Dialog 표시
            AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);
            gsDialog.setTitle("위치 서비스 설정");
            gsDialog.setMessage("무선 네트워크 사용, GPS 위성 사용을 모두 체크하셔야 정확한 위치 서비스가 가능합니다.\n위치 서비스 기능을 설정하시겠습니까?");
            gsDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // GPS설정 화면으로 이동
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                }
            })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    }).create().show();
            return false;

        } else {
            return true;
        }


    }


    // 이거 충전소 목적지 설정 하고 싶은 건데 연결이 안된거당 ㅠㅠ


    @Override
    public void onLocationChange(Location location) {
        lat = "" + location.getLatitude();
        lon = "" + location.getLongitude();
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        if (m_bTrackingMode) {
            tmapview.setLocationPoint(location.getLongitude(), location.getLatitude());
            tmapview.setCenterPoint(location.getLongitude(), location.getLatitude());
            start = new TMapPoint(location.getLatitude(), location.getLongitude());
        }else{
            start = new TMapPoint(latitude,longitude);}
        //현재위치 아이콘 바꾸기
        Bitmap myloc = BitmapFactory.decodeResource(getResources(), R.drawable.heart_big);
        tmapview.setIcon(myloc);
        //
    }

    private void searchRoute(TMapPoint start, TMapPoint end) {
        TMapData data = new TMapData();

        data.findPathData(start, end, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(final TMapPolyLine path) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        path.setLineWidth(30);
                        path.setLineColor(Color.RED);
                        tmapview.addTMapPath(path);
                        Bitmap s = ((BitmapDrawable) ContextCompat.getDrawable(MainActivity.this, android.R.drawable.ic_input_delete)).getBitmap();
                        Bitmap e = ((BitmapDrawable) ContextCompat.getDrawable(MainActivity.this, android.R.drawable.ic_input_get)).getBitmap();
                        tmapview.setTMapPathIcon(s, e);
                    }
                });
            }
        });

    }
    //POI 검색해서 마커 찍고 또 검색해서 새로운 마커 또 직고

    private void searchPOI() {
        final TMapData data = new TMapData();
        String keyword = keywordView.getText().toString();
        if (!TextUtils.isEmpty(keyword)) {
            data.findAllPOI(keyword, new TMapData.FindAllPOIListenerCallback() {
                @Override
                public void onFindAllPOI(final ArrayList<TMapPOIItem> arrayList) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tmapview.removeAllMarkerItem();
                            addMarker();
                            addMarker02();
                            addMarker04();
                            mAdapter.clear();

                            for (TMapPOIItem poi : arrayList) {
                                addMarker03(poi);
                                mAdapter.add(new POI(poi));
                            }

                            if (arrayList.size() > 0) {
                                TMapPOIItem poi = arrayList.get(0);
                                moveMap(poi.getPOIPoint().getLatitude(), poi.getPOIPoint().getLongitude());
                            }
                        }
                    });
                }
            });
        }
    }

    private void moveMap(double lat, double lng) {
        tmapview.setCenterPoint(lng, lat);
    }

    //POI 로 찾았을 때 마커 추가
    public void addMarker03(TMapPOIItem poi) {
        TMapMarkerItem item = new TMapMarkerItem();
        item.setTMapPoint(poi.getPOIPoint());
        Bitmap icon = ((BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.pin)).getBitmap();
        item.setIcon(icon);
        item.setPosition(0.5f, 1);
        item.setCalloutTitle(poi.getPOIName());
        item.setCalloutSubTitle(poi.getPOIContent());

        Bitmap right = ((BitmapDrawable) ContextCompat.getDrawable(this,  R.drawable.checkin)).getBitmap();
        item.setCalloutRightButtonImage(right);
        item.setCanShowCallout(true);
        tmapview.addMarkerItem(poi.getPOIID(), item);
    }

    private void setupMap() {
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);// 기본 설정

        tmapview.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
                String message = tMapMarkerItem.getName();

                //라디오 버튼 없애공
                end = tMapMarkerItem.getTMapPoint();
                 POIlistView.setVisibility(View.INVISIBLE);

                Toast.makeText(MainActivity.this,  "목적지 연결 되었습니다!", Toast.LENGTH_SHORT).show();
                searchRoute(start,end);
                setAlarm();
            }
        });
    }

    private void addMarker() {
        for (TourMap earth : TourMap) {


            TMapPoint loc = new TMapPoint(earth.getLatitude(), earth.getLongitude());
            TMapMarkerItem item = new TMapMarkerItem();
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.street);
            item.setIcon(icon);
            item.setPosition(0.5f, 1);
            item.setCalloutTitle(earth.getName());

            Bitmap right = ((BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.checkin)).getBitmap();
            item.setCalloutRightButtonImage(right);
            item.setCanShowCallout(true);

            item.setTMapPoint(loc);
            String name = earth.getName();
            tmapview.addMarkerItem(name, item);


        }
    }

    //충전소 addmarkder
    private void addMarker02() {
        for (StationMap earth : StationMap) {
            TMapPoint loc = new TMapPoint(earth.getLatitude(), earth.getLongitude());
            TMapMarkerItem item = new TMapMarkerItem();
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.electric);
            item.setIcon(icon);
            item.setPosition(0.5f, 1);

            item.setCalloutTitle(earth.getName());
            item.setCalloutSubTitle(earth.getAddress());

            Bitmap right = ((BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.checkin)).getBitmap();
            item.setCalloutRightButtonImage(right);
            item.setCanShowCallout(true);
            item.setTMapPoint(loc);
            String name = earth.getName();
            tmapview.addMarkerItem(name, item);
        }
    }

    private void addMarker04() {
        for (CultureMap earth : CultureMap) {
            TMapPoint loc = new TMapPoint(earth.getLatitude(), earth.getLongitude());
            TMapMarkerItem item = new TMapMarkerItem();
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
            item.setIcon(icon);
            item.setPosition(0.5f, 1);
            item.setCalloutTitle(earth.getName());
            Bitmap right = ((BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.checkin)).getBitmap();
            item.setCalloutRightButtonImage(right);
            item.setCanShowCallout(true);
            item.setTMapPoint(loc);
            String name = earth.getName();
            tmapview.addMarkerItem(name, item);
        }
    }
    //현재 위치

    //관광지 파일 파싱
    public void getTour() {
        TourMap.clear();
        String file = "Tour.json";

        try {
            InputStream is = getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String result = new String(buffer, "utf-8");

            JSONObject json = new JSONObject(result);
            JSONArray jarr = json.getJSONArray("DATA");

            for (int i = 0; i < jarr.length(); i++) {
                json = jarr.getJSONObject(i);
                double mapX = parseDouble(json.getString("WGS84_X"));//127.sss
                double mapY = parseDouble(json.getString("WGS84_Y"));
                String name = json.getString("NM_DP");
                String address = json.getString("ADD_KOR");
                double dis=0;
                TourMap.add(new TourMap(mapY, mapX, name, address,dis));

            }
            isConnection = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getStation() {
        StationMap.clear();
        String file = "Station.json";


        try {
            InputStream is = getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String result = new String(buffer, "utf-8");

            JSONObject json = new JSONObject(result);
            JSONArray jarr = json.getJSONArray("STATION");

            for (int i = 0; i < jarr.length(); i++) {
                json = jarr.getJSONObject(i);
                double mapY = parseDouble(json.getString("Longitude"));//127.sss
                double mapX = parseDouble(json.getString("Latitude"));

                String name = json.getString("Title");
                String address = json.getString("Road_ad");

                String closed = json.getString("closed");
                String start = json.getString("Start_time");
                String finish = json.getString("Finish_time");

                String type = json.getString("Type_ch");

                String slow = json.getString("Slow_ch");
                String rapid = json.getString("Rapid_ch");
                // String fee = json.getString("Fee");
                String management = json.getString("Management");
                String phone = json.getString("Phone");
                double fee = getDistance(latitude,longitude,mapX,mapY);

                StationMap.add(new StationMap(name, address, closed, start, finish, type, slow, rapid, fee, management, phone, mapX, mapY));
                //  StationMap.add(new StationMap(name, address, closed,type, mapX,mapY));
                // double a = StationMap.getDis
            }
            isConnection = true;

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void getCulture() {
        CultureMap.clear();
        String file = "Culture.json";


        try {
            InputStream is = getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String result = new String(buffer, "utf-8");

            JSONObject json = new JSONObject(result);
            JSONArray jarr = json.getJSONArray("DATA");

            for (int i = 0; i < jarr.length(); i++) {
                json = jarr.getJSONObject(i);
                double mapY = parseDouble(json.getString("Y_COORD"));//127.sss
                double mapX = parseDouble(json.getString("X_COORD"));
                String codename = json.getString("CODENAME");
                String name = json.getString("FAC_NAME");
                String address = json.getString("ADDR");
                String seat = json.getString("SEAT_CNT");
                String phone = json.getString("PHNE");
                String open = json.getString("OPENHOUR");
               // int entrfree = parseInt(json.getString("ENTRFREE"));
                String img = json.getString("MAIN_IMG");
                String fee = json.getString("ENTR_FEE");
                String closed = json.getString("CLOSEDAY");
                String home = json.getString("HOMEPAGE");
                String fax = json.getString("FAX");
                double entrfree=0.0;

                CultureMap.add(new CultureMap(codename, name, seat, phone, open, mapX, mapY, entrfree, img, fee, closed, address, home, fax));

            }
            isConnection = true;

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void setAlarm(){
        // 수행할 동작을 생성
        CountDownTimer timer = new CountDownTimer(1800000,2000) {//나중에 생각 하기 !!!!!!!!!!!!!!!!!!
            @Override
            public void onTick(long l) {
                //2초에 한번씩 1번 실행 되는 메소드
            }
            @Override
            public void onFinish() {

                final ArrayList<TMapPoint> passList = new ArrayList<TMapPoint>();
                    //마지막 1번째가 실행이 되고 실행 되는 메소드
                    AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
                    NotificationSomethings("운전한지 1시간 정도 지났습니다.! 가까운 충전소 연결 해 드릴까요?");//알람진동울리게하기
                    dialog  .setTitle("경유지 알림")
                          .setMessage("가까운 충전소를 연결해 드릴까요?")
                            .setPositiveButton("연결합니다.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                  passRoute();/// 경유지 연결 함수
                             }
                        })
                         .setNeutralButton("취소합니다", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                 Toast.makeText(MainActivity.this, "취소했습니다", Toast.LENGTH_SHORT).show();
                             }
                        })
                         .create().show();
              }
         }.start();
    }
    public void setAlarm_charge(){
        // 수행할 동작을 생성
        CountDownTimer timer = new CountDownTimer(900000,2000) {//나중에 생각 하기 !!!!!!!!!!!!!!!!!!
            @Override
            public void onTick(long l) {
                //2초에 한번씩 1번 실행 되는 메소드
            }
            @Override
            public void onFinish() {
                final ArrayList<TMapPoint> passList = new ArrayList<TMapPoint>();
                //마지막 1번째가 실행이 되고 실행 되는 메소드
                AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
                NotificationSomethings("충전 시작한지 15분 지났습니다! 충전 완료되기 십분 정도 남았습니다.");//알람진동울리게하기

            }
        }.start();
    }

    Comparator<StationMap> disAsc=new Comparator<StationMap>() {
        @Override
        public int compare(StationMap t0, StationMap t1) {
            return t0.getFee()<t1.getFee()?-1:t0.getFee()>t1.getFee()?1:0;
        }
    };
    public void setDisAscSort(){
        Collections.sort(StationMap,disAsc);
    }

    public void passRoute(){


        final ArrayList<TMapPoint> passList = new ArrayList<TMapPoint>();
        for(int i=0;i<StationMap.size();i++){
            double fee= getDistance(latitude,longitude, StationMap.get(i).getLatitude(),StationMap.get(i).getLongitude());
            String fee01= ""+fee;
            StationMap.get(i).setFee(fee);
        }
        setDisAscSort();

        middle = new TMapPoint(StationMap.get(0).getLatitude(),StationMap.get(0).getLongitude());
        passList.add(middle);


        tMapData.findPathDataWithType(TMapData.TMapPathType.CAR_PATH,start,end, passList, 0,
                new TMapData.FindPathDataListenerCallback() {
                    @Override
                    public void onFindPathData(TMapPolyLine polyLine) {
                        tmapview.addTMapPath(polyLine);
                    }
                });
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

    //알람 소리 내보자자
    public void NotificationSomethings(String message) {
        Resources res = getResources();

        Intent notificationIntent = new Intent(this, NotificationSomething.class);
        notificationIntent.putExtra("notificationId", 9999); //화면에보여지는값인거같은데 나중에 바꿔보기
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentTitle("EV Travel")
                .setContentText(message)
                .setTicker("상태바 한줄 메시지")
                .setSmallIcon(R.mipmap.ic_launcher)//아이콘도 바꿔야하고
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))//이 아이콘도 바꿔야하고
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); builder.setSound(alarmSound);
        nm.notify(1234, builder.build());
    }
}