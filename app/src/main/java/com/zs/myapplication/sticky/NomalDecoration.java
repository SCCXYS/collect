package com.zs.myapplication.sticky;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.icu.util.Measure;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZS on 2017/9/15.
 * describe：
 */

public abstract class NomalDecoration extends RecyclerView.ItemDecoration {
    protected String TAG = "ZS";
    private Paint mHeaderTxtPaint;
    private Paint mHeaderContentPaint;

    private int headerHeight = 80;//头部高度
    private int textPaddingLeft = 50;//头部文字左边距
    private int textSize = 30;//字体大小
    private int textColor = Color.BLACK;//字体颜色
    private int headerContentColor = 0xffeeeeee;//头布局背景颜色
    private final float txtYAxis;
    private RecyclerView recyclerView;

    public NomalDecoration() {
        mHeaderTxtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHeaderTxtPaint.setColor(textColor);
        mHeaderTxtPaint.setTextSize(textSize);
        mHeaderTxtPaint.setTextAlign(Paint.Align.LEFT);

        mHeaderContentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHeaderContentPaint.setColor(headerContentColor);
        Paint.FontMetrics fontMetrics = mHeaderTxtPaint.getFontMetrics();
        float total = -fontMetrics.ascent + fontMetrics.descent;
        txtYAxis = total / 2 - fontMetrics.descent;
    }

    //是否绘制过头布局
    private boolean isInitHeight = false;

    private OnDecorationHeadDraw onDecorationHeadDraw;
    public interface OnDecorationHeadDraw {
        View getHeadView(int position);
    }

    /**
     * 只是用来绘制，不做其他处理
     * @param onDecorationHeadDraw
     */
    public void setOnDecorationHeadDraw(OnDecorationHeadDraw onDecorationHeadDraw) {
        this.onDecorationHeadDraw = onDecorationHeadDraw;
    }
    private OnHeaderClickListener onHeaderClickListener;
    public void setOnHeaderClickListener(OnHeaderClickListener onHeaderClickListener) {
        this.onHeaderClickListener = onHeaderClickListener;
    }

    public interface OnHeaderClickListener {
        void headClick(int position);
        }
    public abstract String getHeadName(int pos);

    /**
     * 先调用getItemOffsets再调用onDraw
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (recyclerView == null) {
            recyclerView = parent;
        }
        if (onDecorationHeadDraw != null && !isInitHeight) {
            View headView = onDecorationHeadDraw.getHeadView(0);
            headView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            headerHeight = headView.getMeasuredHeight();
            isInitHeight = true;
        }

        /**
         * 我们为每个不同头布局名称的第一个item设置头部高度
         */
        int position = parent.getChildAdapterPosition(view);//获取当前item的位置
        String headName = getHeadName(position);//根据postion获取悬浮头布局的名称
        if (headName == null) {
            return;
        }
        if (position == 0 || !headName.equals(getHeadName(position - 1))) {
            outRect.top = headerHeight;//设置view paddingTop的距离
        }
    }

    private SparseArray<Integer> stickyHeadPosArray = new SparseArray<>();//记录每个头布和悬浮头布的坐标信息
    private Map<Integer, View> headViewMap = new HashMap<>();
    private GestureDetector gestureDetector;
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if (recyclerView == null) {
            recyclerView = parent;
        }
        if (gestureDetector == null) {
            gestureDetector = new GestureDetector(recyclerView.getContext(), gestureListener);
            recyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });
        }
        stickyHeadPosArray.clear();
        int childCount = recyclerView.getChildCount();//获取屏幕上可见的item数
        int left = recyclerView.getLeft() + recyclerView.getPaddingLeft();
        int right = recyclerView.getRight() + recyclerView.getPaddingRight();

        String firstHeadName = null;
        int firstPos = 0;
        int translateTop = 0; //绘制悬浮头布的偏移量

        for (int i = 0; i < childCount; i++) {

            View childView = recyclerView.getChildAt(i);
            int position = recyclerView.getChildAdapterPosition(childView); //获取当前view在Adapter里的pos
            String headName = getHeadName(position);                 //根据pos获取要悬浮的头部名
            if (i == 0) {
                firstHeadName = headName;
                firstPos = position;
            }
            if (headName == null)
                continue;//如果headerName为空，跳过此次循环

            int viewTop = childView.getTop() + recyclerView.getPaddingTop();

            //如果当前位置为0   获取和上一个头布局的名字不同
            if (position == 0 || !headName.equals(getHeadName(position - 1))) {
                //绘制每个组的头
                if (onDecorationHeadDraw != null) {
                    View headView;
                    if (headViewMap.get(position) == null) {
                        headView = onDecorationHeadDraw.getHeadView(position);
                        headView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                        headView.setDrawingCacheEnabled(true);
                        headView.layout(0, 0, right, headerHeight);
                        headViewMap.put(position, headView);
                        c.drawBitmap(headView.getDrawingCache(), left, viewTop - headerHeight, null);

                    } else {
                        headView = headViewMap.get(position);
                        c.drawBitmap(headView.getDrawingCache(), left, viewTop - headerHeight, null);
                    }

                } else {
                    c.drawRect(left, viewTop - headerHeight, right, viewTop, mHeaderContentPaint);
                    c.drawText(headName, left + textPaddingLeft, viewTop - headerHeight / 2 + txtYAxis, mHeaderTxtPaint);
                }
                if (headerHeight < viewTop && viewTop <= 2 * headerHeight) {//判断此时是否是两个头布局，需要偏移
                    translateTop = viewTop - 2 * headerHeight;
                }
                stickyHeadPosArray.put(position, viewTop);//将头部信息放入array
                Log.e(TAG, "绘制各个头部" + position);
            }
        }

        if (firstHeadName == null) return;
        c.save();
        c.translate(0, translateTop);
        if (onDecorationHeadDraw != null) {//inflater
            View headerView;
            if (headViewMap.get(firstPos) == null) {
                headerView = onDecorationHeadDraw.getHeadView(firstPos);
                headerView.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                headerView.setDrawingCacheEnabled(true);
                headerView.layout(0, 0, right, headerHeight);//布局layout
                headViewMap.put(firstPos, headerView);
                c.drawBitmap(headerView.getDrawingCache(), left, 0, null);
            } else {
                headerView = headViewMap.get(firstPos);
                c.drawBitmap(headerView.getDrawingCache(), left, 0, null);
            }
        } else {
            /*绘制悬浮的头部*/
            c.drawRect(left, 0, right, headerHeight, mHeaderContentPaint);
            c.drawText(firstHeadName, left + textPaddingLeft, headerHeight / 2 + txtYAxis, mHeaderTxtPaint);
//           canvas.drawLine(0, headerHeight / 2, right, headerHeight / 2, mHeaderTxtPaint);//画条线看看文字居中不
        }
        c.restore();
        Log.e(TAG, "绘制悬浮头部");
    }
    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            for (int i = 0; i < stickyHeadPosArray.size(); i++) {
                int valueAt = stickyHeadPosArray.valueAt(i);
                float y = e.getY();
                if (valueAt - headerHeight <= y && y <= valueAt) {
                    if (onHeaderClickListener != null) {
                        onHeaderClickListener.headClick(stickyHeadPosArray.keyAt(i));
                    }
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    };
    public void setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
    }
    public void setTextPaddingLeft(int textPaddingLeft) {
        this.textPaddingLeft = textPaddingLeft;
    }
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
    public void setHeaderContentColor(int headerContentColor) {
        this.headerContentColor = headerContentColor;
    }

    public void loadImg(final String url, final int position, ImageView view) {
        if (getImg(url) != null) {
            view.setImageDrawable(getImg(url));
        } else {
            Glide.with(recyclerView.getContext()).load(url).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    headViewMap.remove(position);
                    imgDrawableMap.put(url, resource);
                    recyclerView.postInvalidate();
                }
            });
        }
    }

    private Map<String, Drawable> imgDrawableMap = new HashMap<>();
    private Drawable getImg(String url) {
        return imgDrawableMap.get(url);
    }
}
