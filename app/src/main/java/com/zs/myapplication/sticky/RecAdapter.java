package com.zs.myapplication.sticky;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zs.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZS on 2017/9/15.
 * describe：
 */

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.Hodler> {
    private Activity mActivity;
    private List<Car> mList;

    public RecAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        mList = new ArrayList<>();
    }

    public void addDate(List<Car> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public Hodler onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Hodler(LayoutInflater.from(mActivity).inflate(R.layout.item_rec_car, parent, false));
    }

    @Override
    public void onBindViewHolder(Hodler holder, int position) {
        Car car = mList.get(position);

        holder.tv_name.setText(car.getName());
        Glide.with(mActivity).load(car.getLogo()).into(holder.iv_logo);
    }

    @Override
    public int getItemCount() {
        return mList ==null ? 0 : mList.size();
    }

    class Hodler extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private ImageView iv_logo;

        public Hodler(final View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.item_tv);
            iv_logo = (ImageView) itemView.findViewById(R.id.item_iv);
            LinearLayout item_content = (LinearLayout) itemView.findViewById(R.id.item_content);
            item_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "你点击到了" + tv_name.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
