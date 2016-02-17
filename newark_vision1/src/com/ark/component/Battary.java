package com.ark.component;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ark.newark_vision1.R;

/**
 * Created by edwin on 14-9-11.
 */
public class Battary extends RelativeLayout {

    private ImageView imageView;
    private TextView textViewValue;
    private TextView textView;


    public Battary(Context context) {

        super(context);


    }

    public Battary(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);


    }

    public Battary(Context context, AttributeSet attrs) {

        super(context, attrs);

//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(R.layout.watervalue,null);
        LayoutInflater.from(context).inflate(R.layout.battary, this);


        imageView = (ImageView) findViewById(R.id.battary_img);

        textViewValue = (TextView) findViewById(R.id.battary_text1);


        Typeface face = Typeface.createFromAsset(context.getResources().getAssets(), "Transformers.ttf");
        textViewValue.setTypeface(face);

        this.setbattaryValue(53);


        // this.setwaterValue(9);


    }


    public void setbattaryValue(int value) {


        textViewValue.setText(value + "%");

        if (value == 0) {

            imageView.setBackgroundResource(R.drawable.battary01);


        } else if (value > 0 && value <= 10) {

            imageView.setBackgroundResource(R.drawable.battary01);


        } else if (value > 10 && value <= 20) {

            imageView.setBackgroundResource(R.drawable.battary02);


        } else if (value > 20 && value <= 30) {


            imageView.setBackgroundResource(R.drawable.battary03);

        } else if (value > 30 && value <= 40) {//红色


            imageView.setBackgroundResource(R.drawable.battary04);

        } else if (value > 40 && value <= 50) {//黄色

            imageView.setBackgroundResource(R.drawable.battary05);

        } else if (value > 50 && value <= 60) {


            imageView.setBackgroundResource(R.drawable.battary06);


        } else if (value > 60 && value <= 70) {


            imageView.setBackgroundResource(R.drawable.battary07);

        } else if (value > 70 && value <= 80) {

            imageView.setBackgroundResource(R.drawable.battary08);


        } else if (value > 80 && value <= 90) {

            imageView.setBackgroundResource(R.drawable.battary09);


        } else {//满

            imageView.setBackgroundResource(R.drawable.battary10);
        }
    }
}
