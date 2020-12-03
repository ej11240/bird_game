package com.example.bird_game;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class FeedActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    ImageView lava1, lava2,bird;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feeding);

        Toast toast=Toast.makeText(getApplicationContext(), "나무의 벌레를 떨어뜨리기 위해 \n휴대폰을 흔들어주세요", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();

        bird=(ImageView)findViewById(R.id.bird);
        if(((MainActivity)MainActivity.context).stage1==1)
            bird.setImageResource(R.drawable.egg);
        else if(((MainActivity)MainActivity.context).stage1==2)
            bird.setImageResource(R.drawable.egg2);
        else if(((MainActivity)MainActivity.context).stage1>=3)
            bird.setImageResource(R.drawable.egg3);
        else
            bird.setImageResource(R.drawable.egg1);


        lava1=(ImageView)findViewById(R.id.lava1);
        lava1.setVisibility(View.INVISIBLE);
        lava2=(ImageView)findViewById(R.id.lava2);
        lava2.setVisibility(View.INVISIBLE);

        button=(Button)findViewById(R.id.button);
        button.setVisibility(View.INVISIBLE);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }
    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 30) {
                execute();
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    @Override
    protected void onResume() {
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void execute(){
        // 소리 효과
        MediaPlayer mediaPlayer=MediaPlayer.create(FeedActivity.this, R.raw.treesound);
        mediaPlayer.start();
        mediaPlayer.setLooping(false);

        //화면 효과
        lava1.setVisibility(View.VISIBLE);
        Animation animDown=new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.2f
        );
        animDown.setDuration(2000);
        //animDown.setRepeatCount(-1);
        //animDown.setRepeatMode(Animation.REVERSE);
        animDown.setInterpolator(new LinearInterpolator());
        lava1.setAnimation(animDown);
        lava1.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lava2.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "먹이가 떨어졌네요!!", Toast.LENGTH_SHORT).show();

                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity)MainActivity.context).safe.setEnabled(true);
                        ((MainActivity)MainActivity.context).feed.setBackgroundColor(Color.BLUE);
                        ((MainActivity)MainActivity.context).feed.setText("먹이주기 클리어!");
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
        },2000);


    }


}
