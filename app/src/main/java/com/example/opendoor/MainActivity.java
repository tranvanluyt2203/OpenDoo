package com.example.opendoor;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.opendoor.Assets.Common;
import com.example.opendoor.Tab.TabviewActivity;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LottieAnimationView animationView = findViewById(R.id.animation);
        animationView.setAnimation(Common.FOLDER_ANIMATION + "/" + Common.SCAN_FACE);
        animationView.playAnimation();
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {
                Log.d(Common.TAG,"Start");
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                Log.d(Common.TAG,"End");
                animationView.setVisibility(View.GONE);
                Intent intent = new Intent(MainActivity.this, TabviewActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {
                Log.d(Common.TAG,"Cancel");
            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {
                Log.d(Common.TAG,"Repeat");
            }
        });

    }
}
