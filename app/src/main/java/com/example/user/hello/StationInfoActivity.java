package com.example.user.hello;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StationInfoActivity extends AppCompatActivity {
    Double x,y;

    String phone01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_info);

        Intent intent = getIntent();
        TextView name = (TextView)findViewById(R.id.Sta_name);
        TextView addr = (TextView)findViewById(R.id.Sta_addr);
        TextView closed = (TextView)findViewById(R.id.Sta_closed);
        TextView start = (TextView)findViewById(R.id.Sta_start);

        TextView type = (TextView)findViewById(R.id.Sta_type);
        TextView phone = (TextView)findViewById(R.id.Sta_phone);


        String name01 = intent.getStringExtra("충전소");
        name.setText(name01);
        String addr01 = intent.getStringExtra("주소");
        addr.setText(addr01);
        String closed01 = intent.getStringExtra("closed");
        closed.setText(closed01);
        String start01= intent.getStringExtra("start");
        String finish01=intent.getStringExtra("finish");
        start.setText(start01+" ~ "+finish01);

        String type01 = intent.getStringExtra("type");type.setText(type01);

        x=intent.getDoubleExtra("충전소_x",0.0);

        y=intent.getDoubleExtra("충전소_y",0.0);
       phone01=intent.getStringExtra("phone");phone.setText(phone01);




        Button btn =(Button)findViewById(R.id.sta_mark);
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
        /*
        ImageView img = (ImageView)findViewById(R.id.imageView2);
        img.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:/"+phone01));
                startActivity(intent);
            }
        });
*/
    }



}
