package com.example.user.hello;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import static android.R.attr.id;


public class NotificationSomething extends Activity {
    NotificationManager nm=null;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nm.cancel(id);
        CharSequence s = "전달 받은 값은 "; //여기도 바꿔야해
        int id=0;

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            s = "error";
        }
        else {
            id = extras.getInt("notificationId");
        }
        TextView t = (TextView) findViewById(R.id.textView);
        s = s+" "+id;
        t.setText(s);
       nm =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        //노티피케이션 제거
        nm.cancel(id);
    }

}