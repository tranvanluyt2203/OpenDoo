package com.example.opendoor.Screen;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.example.opendoor.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView textView = findViewById(R.id.textView);
        animateText(textView, getString(R.string.app_name));

    }
    private void animateText(final TextView textView, final String text) {
        textView.setText("");

        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            int index = 0;

            @Override
            public void run() {
                if (index < text.length()) {
                    textView.append(String.valueOf(text.charAt(index)));
                    index++;
                    handler.postDelayed(this, 250);
                }
                else {
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                }
            }
        };
        handler.post(runnable);
    }


}
