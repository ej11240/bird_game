package com.example.bird_game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SaveActivity extends AppCompatActivity {

    ImageView cat,bird;
    int count=0;
    TextView info;
    Button goback;
     Point size;
    private static final int PERMISSION_REQUEST_CODE = 100;
    TelephonyManager telephonyManager;
    @RequiresApi(api = Build.VERSION_CODES.P)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saving);

        info=(TextView)findViewById(R.id.info);
        goback=(Button)findViewById(R.id.goback);
        goback.setVisibility(View.GONE);

        bird=(ImageView)findViewById(R.id.bird);
        if(((MainActivity)MainActivity.context).stage1==1)
            bird.setImageResource(R.drawable.egg);
        else if(((MainActivity)MainActivity.context).stage1==2)
            bird.setImageResource(R.drawable.egg2);
        else if(((MainActivity)MainActivity.context).stage1>=3)
            bird.setImageResource(R.drawable.egg3);
        else
            bird.setImageResource(R.drawable.egg1);

        cat=(ImageView)findViewById(R.id.cat);

        Glide.with(this).load(R.drawable.movingcat).into(cat);

        Display display = getWindowManager().getDefaultDisplay();
        size=new Point();
        display.getSize(size);

        cat.setVisibility(View.VISIBLE);


        info.setText("고양이가 아기새의 둥지를 위협하고 있어요😱!\n아기새가 안전하도록 고양이를 터치해 10회 이상 쫓아내 주세요!");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                info.setVisibility(View.GONE);
                startgame();
            }
        },1000);

    }

    public void startgame(){
        final DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        final Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Random ran = new Random();
                        final float dx = ran.nextFloat() * size.x;
                        final float dy = ran.nextFloat() * size.y;
                        final Timer timer = new Timer();
                        float angle=(float) (Math.atan((dy-size.y/2)/(dx-size.x/2))*(180.0/Math.PI));
                        cat.setRotation(angle);
                        cat.animate()
                                .x(dx)
                                .y(dy)
                                .setDuration(0)
                                .start();
                        cat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                count++;
                                Toast.makeText(getApplicationContext(),count+"번 쫓아냈어요!!!",Toast.LENGTH_SHORT).show();
                            }
                        });

                        if(count>=2){
                            count=0;
                            Thread.currentThread().interrupt();
                            info.setVisibility(View.VISIBLE);
                            goback.setVisibility(View.VISIBLE);
                            info.setText("와 대단해요 고양이를 모두 내쫓으셨군요!");
                            goback.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ((MainActivity)MainActivity.context).fly.setEnabled(true);
                                    ((MainActivity)MainActivity.context).safe.setBackgroundColor(Color.BLUE);
                                    ((MainActivity)MainActivity.context).safe.setText("둥지 지키기 클리어!");
                                    ((MainActivity)MainActivity.context).stage1++;
                                    if(((MainActivity)MainActivity.context).stage1==1)
                                        ((MainActivity)MainActivity.context).imageView.setImageResource(R.drawable.egg);
                                    else if(((MainActivity)MainActivity.context).stage1==2)
                                        ((MainActivity)MainActivity.context).imageView.setImageResource(R.drawable.egg2);
                                    else if(((MainActivity)MainActivity.context).stage1>=3)
                                        ((MainActivity)MainActivity.context).imageView.setImageResource(R.drawable.egg3);
                                    else
                                        ((MainActivity)MainActivity.context).imageView.setImageResource(R.drawable.egg1);

                                    onBackPressed();
                                }
                            });
                        }
                    }
                });
            }
        }, 0, 800);
        
    }

}
