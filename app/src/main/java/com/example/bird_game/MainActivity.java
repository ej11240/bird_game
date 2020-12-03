package com.example.bird_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    Button feed, fly, safe;
    ImageView imageView;
    public static Context context;
    public int stage1=0;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context=this;
        imageView=(ImageView)findViewById(R.id.imageView);

        feed=(Button)findViewById(R.id.feed);
        fly=(Button)findViewById(R.id.fly);
        fly.setEnabled(true);
        safe=(Button)findViewById(R.id.safe);
        safe.setEnabled(true);

        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gofeeding=new Intent(getApplicationContext(), FeedActivity.class);
                startActivity(gofeeding);
            }
        });



        fly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goflying=new Intent(getApplicationContext(), FlyActivity.class);
                startActivity(goflying);
            }
        });



        safe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gosafe=new Intent(getApplicationContext(), SaveActivity.class);
                startActivity(gosafe);
            }
        });

    }


}