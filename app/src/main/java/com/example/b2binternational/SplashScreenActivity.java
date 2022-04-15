package com.example.b2binternational;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class SplashScreenActivity extends AppCompatActivity {

    //Photo by <a href="https://unsplash.com/@eiskonen?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText">Hans Eiskonen</a> on <a href="https://unsplash.com/s/photos/logo-design?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText">Unsplash</a>
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Menghilangkan Status bar
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        int mTimeSplashScreen = 3000; //3 detik

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                SplashScreenActivity.this.startActivity(intent);
                SplashScreenActivity.this.finish();
            }
        }, mTimeSplashScreen);
    }
}