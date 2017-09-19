package com.zs.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zs.myapplication.broken.BrokanActivity;
import com.zs.myapplication.calendar.CalendarActivity;
import com.zs.myapplication.constraint.ConstraintActivity;
import com.zs.myapplication.sticky.StickyActivity;
import com.zs.myapplication.switch_button.SwitchButton;
import com.zs.myapplication.welcome.activity.WelcomeActivity;

public class MainActivity extends AppCompatActivity {

    private SwitchButton sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sb = (SwitchButton) findViewById(R.id.sb);
        sb.setChecked(true);
        boolean checked = sb.isChecked();
        Log.e("---","是否选中"+checked);
        sb.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                Toast.makeText(MainActivity.this, isChecked+"", Toast.LENGTH_SHORT).show();
                boolean checked = sb.isChecked();
                Log.e("---","是否选中"+checked);
            }
        });
        findViewById(R.id.btn_sticky).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StickyActivity.class));
            }
        });
        findViewById(R.id.btn_brokan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BrokanActivity.class));
            }
        });
        findViewById(R.id.btn_constraint).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ConstraintActivity.class));
            }
        });
        findViewById(R.id.btn_welcome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
            }
        });
        findViewById(R.id.btn_calendar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CalendarActivity.class));
            }
        });
    }
}
