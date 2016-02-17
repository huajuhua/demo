package com.ark.newark_phone;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.Image;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CategoryView extends LinearLayout {

    // private Map<Long, String> dataSource = null;

    private List<Integer> titleDataSource = null;
    private List<Long> valueDataSource = null;
    private List<ImageView> views = new ArrayList<ImageView>();

    private ViewPager viewPager;// 页卡内容
    private ImageView imageView;// 动画图片

    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度

    private OnCategoryItemSelectedListener onCategoryItemSelectedListener = null;

    public CategoryView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public CategoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public void setValueDataSource(List<Long> valueDataSource) {
        this.valueDataSource = valueDataSource;
    }

    public void setTitleDataSource(List<Integer> titleDataSource) {
        this.titleDataSource = titleDataSource;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;

        if (viewPager != null)
            this.viewPager.setOnPageChangeListener(getOnPageChangeListener());
    }

    public void setOnCategoryItemSelectedListener(
            OnCategoryItemSelectedListener onCategoryItemSelectedListener) {
        this.onCategoryItemSelectedListener = onCategoryItemSelectedListener;
    }

    public void createUI() {
        if (titleDataSource == null)
            return;

        // android:id="@+id/categoryView"
        // android:layout_width="fill_parent"
        // android:layout_height="40.0dip"
        // android:background="#FFFFFF"

        LinearLayout container = new LinearLayout(this.getContext());
        LayoutParams containerlp = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        container.setLayoutParams(containerlp);
        container.setOrientation(HORIZONTAL);
        container.setGravity(Gravity.CENTER_HORIZONTAL);
        this.addView(container);

        int index = 0;
        for (int i = 0; i < titleDataSource.size(); i++) {
            LinearLayout container1 = new LinearLayout(this.getContext());
            LayoutParams containerlp1 = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            containerlp1.weight = (float) 1.0;
            container1.setLayoutParams(containerlp1);
            container1.setOrientation(HORIZONTAL);
            container1.setGravity(Gravity.CENTER_HORIZONTAL);

            ImageView imageview = new ImageView(this.getContext());

            LayoutParams lp = new LayoutParams(
                    100,
                    100);
            lp.setMargins(0, 10, 10, 0);
//            lp.setMargins(40,20,40,20);

//			lp.weight = (float) 1.0;
            imageview.setLayoutParams(lp);
            imageview.setBackgroundResource(titleDataSource.get(i));
            imageview.setOnClickListener(new CategoryOnClickListener(index));
            imageview.setTag(i + "a");

            container1.addView(imageview);

            views.add(imageview);

            container.addView(container1);

            index++;
        }
//		for (Integer title : titleDataSource) {
//
//            LinearLayout container1 = new LinearLayout(this.getContext());
//            LayoutParams containerlp1 = new LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            containerlp1.weight = (float) 1.0;
//            container1.setLayoutParams(containerlp1);
//            container1.setOrientation(HORIZONTAL);
//            container1.setGravity(Gravity.CENTER_HORIZONTAL);
//
//			ImageView imageview = new ImageView(this.getContext());
//
//			LayoutParams lp = new LayoutParams(
//					100,
//					100);
//            lp.setMargins(0,10,10,0);
////            lp.setMargins(40,20,40,20);
//
////			lp.weight = (float) 1.0;
//            imageview.setLayoutParams(lp);
//            imageview.setBackgroundResource(title);
//			imageview.setOnClickListener(new CategoryOnClickListener(index));
//            imageview.setTag();
//
//            container1.addView(imageview);
//
//			views.add(imageview);
//
//			container.addView(container1);
//
//			index++;
//
//		}

        imageView = new ImageView(this.getContext());

        LinearLayout.LayoutParams clp = new LinearLayout.LayoutParams(
                50, LayoutParams.WRAP_CONTENT);
        clp.setMargins(0, 12, 0, 0);

        imageView.setScaleType(ScaleType.CENTER);
        imageView.setImageResource(R.drawable.categoryline);

        imageView.setLayoutParams(clp);
        this.addView(imageView);

        // //底部线条
        // LinearLayout bottomline = new LinearLayout(this.getContext());
        // LinearLayout.LayoutParams bottomlinelp = new
        // LinearLayout.LayoutParams(
        // LinearLayout.LayoutParams.FILL_PARENT,
        // ScreenUtility.dip2px(this.getContext(), 1));
        // bottomline.setLayoutParams(bottomlinelp);
        // bottomline.setBackgroundColor(Color.argb(0xFF, 0xdb, 0x44, 0x01));
        // this.addView(bottomline);

        // 初始化动画
        InitImageView();

        ViewGroup.LayoutParams vlp = this.getLayoutParams();
        vlp.height = LayoutParams.WRAP_CONTENT;
        this.setLayoutParams(vlp);
        this.setOrientation(VERTICAL);

    }

    private void InitImageView() {
        if (titleDataSource == null)
            return;

        int itemCount = titleDataSource.size();
        // imageView = (ImageView) findViewById(R.id.cursor);
        bmpW = BitmapFactory.decodeResource(getResources(),
                R.drawable.categoryline).getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();

        ((Activity) this.getContext()).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);

        int screenW = this.getLayoutParams().width; // dm.widthPixels;// 获取分辨率宽度
        if (screenW == LayoutParams.FILL_PARENT)
            screenW = dm.widthPixels;
        offset = (screenW / itemCount - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        imageView.setImageMatrix(matrix);// 设置动画初始位置

        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        lp.width = screenW / itemCount;
        imageView.setLayoutParams(lp);

    }

    /**
     * 头标点击监听 3
     */
    private class CategoryOnClickListener implements OnClickListener {
        private int index = 0;

        public CategoryOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            viewPager.setCurrentItem(index);
        }

    }

    public OnPageChangeListener getOnPageChangeListener() {
        return new CategoryOnPageChangeListener();
    }

    public class CategoryOnPageChangeListener implements OnPageChangeListener {

        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        int two = one * 2;// 页卡1 -> 页卡3 偏移量

        public void onPageScrollStateChanged(int arg0) {

        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        public void onPageSelected(int arg0) {

//			Log.d("CategoryView", "onPageSelected==========="+arg0);
//
//			if (titleDataSource == null)
//				return;
//			if (arg0 >= titleDataSource.size())
//				return; 

            Animation animation = new TranslateAnimation(one * currIndex, one
                    * arg0, 0, 0);
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            imageView.startAnimation(animation);

            Log.d(this.getClass().getName(), "onPageSelected=" + arg0);

            if (onCategoryItemSelectedListener != null) {
                Long selectedValue = valueDataSource.get(arg0);
                Integer selectedTitle = titleDataSource.get(arg0);

                onCategoryItemSelectedListener.onCategoryItemSelected(arg0,
                        selectedValue, selectedTitle);
            }
        }

    }

    public interface OnCategoryItemSelectedListener {
        void onCategoryItemSelected(int selectedPosition, Long selectedValue,
                                    Integer selectedTitle);
    }

}
