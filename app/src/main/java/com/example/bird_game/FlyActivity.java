package com.example.bird_game;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class FlyActivity extends AppCompatActivity {
//
//    private float xPos, xAccel, xVel = 0.0f;
//    private float yPos, yAccel, yVel = 0.0f;
//    private float xMax, yMax;
//    private Bitmap ball;
//    private SensorManager sensorManager;
//
//    private class BallView extends View {
//        public BallView(Context context) {
//            super(context);
//        }
//    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        BallView ballView = new BallView(this);
//        setContentView(ballView);
//
//        Point size = new Point();
//        Display display = getWindowManager().getDefaultDisplay();
//        display.getSize(size);
//        xMax = (float) size.x - 100;
//        yMax = (float) size.y - 100;
//
//        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        sensorManager.registerListener((SensorEventListener) this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
//    }
//
//    @Override
//    protected void onStop() {
//        sensorManager.unregisterListener(this);
//        super.onStop();
//    }
//
//    @Override
//    public void onSensorChanged(SensorEvent sensorEvent) {
//        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//            xAccel = sensorEvent.values[0];
//            yAccel = -sensorEvent.values[1];
//            updateBall();
//        }
//    }
//    private void updateBall() {
//        float frameTime = 0.666f;
//        xVel += (xAccel * frameTime);
//        yVel += (yAccel * frameTime);
//
//        float xS = (xVel / 2) * frameTime;
//        float yS = (yVel / 2) * frameTime;
//
//        xPos -= xS;
//        yPos -= yS;
//
//        if (xPos > xMax) {
//            xPos = xMax;
//        } else if (xPos < 0) {
//            xPos = 0;
//        }
//
//        if (yPos > yMax) {
//            yPos = yMax;
//        } else if (yPos < 0) {
//            yPos = 0;
//        }
//    }
//
//    private class BallView  extends View{
//
//        public BallView(Context context) {
//            super(context);
//            Bitmap ballSrc = BitmapFactory.decodeResource(getResources(), R.drawable.lava);
//            final int dstWidth = 100;
//            final int dstHeight = 100;
//            ball = Bitmap.createScaledBitmap(ballSrc, dstWidth, dstHeight, true);
//        }
//
//        @Override
//        protected void onDraw(Canvas canvas) {
//            canvas.drawBitmap(ball, xPos, yPos, null);
//            invalidate();
//        }
//    }



    Button start,goback;
    private float xPos, xAccel, xVel = 0.0f;
    private float yPos, yAccel, yVel = 0.0f;
    private float xMax, yMax;
    private Bitmap ball;
    private SensorManager sensorManager;

    FrameLayout frame;
    ImageView bird,tree,nest;
    float sizex,sizey,initialy,treex,treey,nowy,nestx,nesty;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flying);

        SensorManager sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        bird=(ImageView)findViewById(R.id.bird);

        if(((MainActivity)MainActivity.context).stage1==1)
            bird.setImageResource(R.drawable.egg);
        else if(((MainActivity)MainActivity.context).stage1==2)
            bird.setImageResource(R.drawable.egg2);
        else if(((MainActivity)MainActivity.context).stage1>=3)
            bird.setImageResource(R.drawable.egg3);
        else
            bird.setImageResource(R.drawable.egg1);

        initialy=bird.getY();
        tree=(ImageView)findViewById(R.id.tree);
        treex=tree.getX();
        treey=tree.getY();
        nowy=initialy;
        nest=(ImageView)findViewById(R.id.nest);
        nestx=nest.getX();
        nesty=nest.getY();

        frame=(FrameLayout)findViewById(R.id.frame);

        textView=(TextView)findViewById(R.id.textView);

        start=(Button)findViewById(R.id.start);
        goback=(Button)findViewById(R.id.goback);
        goback.setVisibility(View.GONE);

        final float[] mValuesMagnet      = new float[3];
        final float[] mValuesAccel       = new float[3];
        final float[] mValuesOrientation = new float[3];
        final float[] mRotationMatrix    = new float[9];


        final TextView txt1 = (TextView) findViewById(R.id.textView);
        final SensorEventListener mEventListener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

            public void onSensorChanged(SensorEvent event) {
                // Handle the events for which we registered
                switch (event.sensor.getType()) {
                    case Sensor.TYPE_ACCELEROMETER:
                        System.arraycopy(event.values, 0, mValuesAccel, 0, 3);
                        break;

                    case Sensor.TYPE_MAGNETIC_FIELD:
                        System.arraycopy(event.values, 0, mValuesMagnet, 0, 3);
                        break;
                }
            };
        };

        // You have set the event lisetner up, now just need to register this with the
        // sensor manager along with the sensor wanted.
        setListners(sensorManager, mEventListener);

        start.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                start.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);

                final Timer timer = new Timer();

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SensorManager.getRotationMatrix(mRotationMatrix, null, mValuesAccel, mValuesMagnet);
                                SensorManager.getOrientation(mRotationMatrix, mValuesOrientation);
                                final CharSequence test;
                                test = "results: " + mValuesOrientation[0] + " " + mValuesOrientation[1] + " " + mValuesOrientation[2];
                                //왼쪽 기울이면 [0]은 +로(최대 거의 3까지 감
                                txt1.setText(test);

                                Display display = getWindowManager().getDefaultDisplay();
                                Point size=new Point();
                                display.getSize(size);
                                float setx;
                                if(mValuesOrientation[0]<0) setx=-mValuesOrientation[0];
                                float nowx=(float) (mValuesOrientation[0]/3.2*sizex/2);
                                nowy++;
                                System.out.println("\nx값: "+nowx+"\ny값:"+nowy+"\n"+"x:"+treex+", y:"+treey);
                                //bird.animate().x(nowx).y(nowy).setDuration(0).start();
                                bird.setY(nowy);
                                bird.setX(nowx);
                                //Toast.makeText(getApplicationContext(),"x:"+treex+", y:"+treey,Toast.LENGTH_SHORT).show();
                                nest.setX(treex+200);nest.setY(treey+200);
                                if((400)>nowx&&(100)<nowx&&(1100)>nowy&&(380)<nowy){
                                    nowy=0;
                                }
                                if((nestx+200)<nowx&&(nestx-200)>nowx&&(nesty+200)<nowy&&(nesty-200)>nowy){
                                    Toast.makeText(getApplicationContext(),"성공!",Toast.LENGTH_SHORT).show();
                                }
                                if(nowy==sizey) {
                                    Thread.interrupted();
                                    Toast.makeText(getApplicationContext(), "성공!", Toast.LENGTH_SHORT).show();
                                    ((MainActivity)MainActivity.context).fly.setBackgroundColor(Color.BLUE);
                                    ((MainActivity)MainActivity.context).fly.setText("날기 연습 클리어!");
                                    ((MainActivity)MainActivity.context).stage1++;
                                    if(((MainActivity)MainActivity.context).stage1==1)
                                        ((MainActivity)MainActivity.context).imageView.setImageResource(R.drawable.egg);
                                    else if(((MainActivity)MainActivity.context).stage1==2)
                                        ((MainActivity)MainActivity.context).imageView.setImageResource(R.drawable.egg2);
                                    else if(((MainActivity)MainActivity.context).stage1>=3)
                                        ((MainActivity)MainActivity.context).imageView.setImageResource(R.drawable.egg3);
                                    else
                                        ((MainActivity)MainActivity.context).imageView.setImageResource(R.drawable.egg1);

                                    goback.setVisibility(View.VISIBLE);
                                    textView.setText("아기새의 날기 연습이 성공했어요!");
                                    textView.setVisibility(View.INVISIBLE);

                                    goback.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            onBackPressed();
                                        }
                                    });
                                }
                            }
                        });
                    }
                },0,10);
            }
        });

    }

    // Register the event listener and sensor type.
    public void setListners(SensorManager sensorManager, SensorEventListener mEventListener)
    {
        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        sizex=frame.getWidth();
        sizey= frame.getHeight();
    }

}
