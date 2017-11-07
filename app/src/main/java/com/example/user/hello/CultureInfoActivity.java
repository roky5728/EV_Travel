package com.example.user.hello;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CultureInfoActivity extends AppCompatActivity {
    Double x,y;
    Bitmap bitmapSample1;
    String img01;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_culture_info);

        Intent intent = getIntent();
        TextView name = (TextView)findViewById(R.id.Cul_name);
        TextView addr = (TextView)findViewById(R.id.Cul_addr);
        TextView phone = (TextView)findViewById(R.id.Cul_phone);
        TextView open = (TextView)findViewById(R.id.Cul_open);
        TextView closed = (TextView)findViewById(R.id.Cul_closed);
        TextView seat= (TextView)findViewById(R.id.Cul_seat);
        TextView home= (TextView)findViewById(R.id.Cul_home);
        TextView fee=(TextView)findViewById(R.id.Cul_entrfree);
       img = (ImageView)findViewById(R.id.Cul_img);

        String name01 = intent.getStringExtra("문화");
        name.setText(name01);
        String addr01 = intent.getStringExtra("주소");
        addr.setText(addr01);
        String phone01=intent.getStringExtra("phone");
        phone.setText(phone01);
        String open01 = intent.getStringExtra("open");
        open.setText(open01);
        if(open01.equals(null)){
            open.setText("24 Hour");
        }

        String closed01 = intent.getStringExtra("closed");
        closed.setText(closed01);
        String seat01=intent.getStringExtra("seat");
        seat.setText(seat01);
        if(seat01.equals(null)){
            seat.setText("없음");
        }
        String home01= intent.getStringExtra("homepage");
        home.setText(home01);
        String fee01=intent.getStringExtra("fee");

        fee.setText(fee01);
        if(fee.getText().equals(null)){
            fee.setText("무료");
        }

        x=intent.getDoubleExtra("충전소_x",0.0);

        y=intent.getDoubleExtra("충전소_y",0.0);

        img01 = intent.getStringExtra("img");
        //addr.setText(img01);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    bitmapSample1=getBitmap(img01);

                }catch(Exception e){}
                finally {
                    if(bitmapSample1!=null) {
                        runOnUiThread(new Runnable() {
                            @SuppressLint("NewApi")
                            public void run() {
                                img.setImageBitmap(bitmapSample1);
                            }
                        });
                    }
                }
            }
        }).start();

        Button btn =(Button)findViewById(R.id.cul_desti);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent =new Intent();
                intent.putExtra("x",x);
                intent.putExtra("y",y);
                setResult(1,intent);
                finish();

            }
        });

    }

    private Bitmap getBitmap(String url) {
        URL imgUrl = null;
        HttpURLConnection connection = null;
        InputStream is = null;

        Bitmap retBitmap = null;

        try{
            imgUrl = new URL(url);
            connection = (HttpURLConnection) imgUrl.openConnection();
            connection.setDoInput(true); //url로 input받는 flag 허용
            connection.connect(); //연결
            is = connection.getInputStream(); // get inputstream
            retBitmap = BitmapFactory.decodeStream(is);
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            if(connection!=null) {
                connection.disconnect();
            }
            return retBitmap;
        }
    }



}
