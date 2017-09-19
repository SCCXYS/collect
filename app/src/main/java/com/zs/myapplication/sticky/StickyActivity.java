package com.zs.myapplication.sticky;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.zs.myapplication.R;

import java.util.List;

/**
 * Created by zs on 2017/9/15.
 * describe：
 */

public class StickyActivity extends AppCompatActivity {


    private RecyclerView recView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky);
        recView = (RecyclerView) findViewById(R.id.recView);

        final List<Car> carList = CarList.getCars();
        RecAdapter adapter = new RecAdapter(this);
        adapter.addDate(carList);

        NomalDecoration decoration = new NomalDecoration() {
            @Override
            public String getHeadName(int pos) {
                return carList.get(pos).getHeaderName();
            }
        };
        decoration.setOnHeaderClickListener(new NomalDecoration.OnHeaderClickListener() {
            @Override
            public void headClick(int position) {
                Toast.makeText(StickyActivity.this, carList.get(position).getHeaderName(), Toast.LENGTH_SHORT).show();
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recView.addItemDecoration(decoration);
        recView.setLayoutManager(manager);
        recView.setAdapter(adapter);
    }
}
