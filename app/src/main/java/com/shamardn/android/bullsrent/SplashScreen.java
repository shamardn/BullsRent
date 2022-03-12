package com.shamardn.android.bullsrent;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_SCREEN =5000;
    Animation top_anim,bottom_anim;

    ImageView imageView ;
    TextView logo,slogan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        imageView = findViewById(R.id.splash_img);
        logo = findViewById(R.id.spalsh_logo);
        slogan = findViewById(R.id.splash_slogan);

        top_anim = AnimationUtils.loadAnimation(this,R.anim.top_anim);
        bottom_anim = AnimationUtils.loadAnimation(this,R.anim.bottom_anim);

        imageView.setAnimation(top_anim);
        logo.setAnimation(bottom_anim);
        slogan.setAnimation(bottom_anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this,Login.class);

                Pair[] pairs = new Pair[3];
                pairs[0] = new Pair<View,String>(imageView,"trans_img");
                pairs[1] = new Pair<View,String>(logo,"trans_logo");
                pairs[2] = new Pair<View,String>(slogan,"trans_slogan");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this,pairs);
                startActivity(intent,options.toBundle());
               finish();
            }
        },SPLASH_SCREEN);
    }
}