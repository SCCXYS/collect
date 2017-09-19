package com.zs.myapplication.anim;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.zs.myapplication.R;

public class AnimActivity extends AppCompatActivity {

    private ImageView iv_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);
        iv_logo = (ImageView) findViewById(R.id.iv_logo);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScaleAnimation();
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScaleAnimation2();
            }
        });

    }

    private void onScaleAnimation2() {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(iv_logo,"scaleX",1.0f,1.8f,1.0f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(iv_logo,"scaleY",1.0f,1.8f,1.0f);
        AnimatorSet set =new AnimatorSet();
        set.setDuration(2000);
        set.setInterpolator(new SpringScaleInterpolator(0.5f));
        set.playTogether(animatorX,animatorY);
        set.start();
    }

    private void onScaleAnimation() {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(iv_logo,"scaleX",1.0f,1.8f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(iv_logo,"scaleY",1.0f,1.8f);
        AnimatorSet set =new AnimatorSet();
        set.setDuration(2000);
        set.playTogether(animatorX,animatorY);
        set.start();
    }
}
