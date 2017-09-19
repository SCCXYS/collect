package com.zs.myapplication.Data;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zs.myapplication.R;

public class DateActivity extends AppCompatActivity {
    private DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
            DateActivity.this);
    private TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        tvDate = (TextView) findViewById(R.id.tv_date);

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTimePicKDialog.dateTimePicKDialog2(tvDate,"00-00-00");
            }
        });
    }
}
