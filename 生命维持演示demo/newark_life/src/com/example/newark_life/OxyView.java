package com.example.newark_life;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by edwin on 14-9-11.
 */
public class OxyView extends RelativeLayout {

    private ImageView imageView;
    private TextView textViewValue;
    private TextView textView;


    public OxyView(Context context) {

        super(context);


    }

    public OxyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);


    }

    public OxyView(Context context, AttributeSet attrs) {

        super(context, attrs);

//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(R.layout.watervalue,null);
        LayoutInflater.from(context).inflate(R.layout.oxyvalue, this);


        imageView = (ImageView) findViewById(R.id.oxyvalue_img);

        textViewValue = (TextView) findViewById(R.id.oxyvalue_text1);

        textView = (TextView) findViewById(R.id.oxyvalue_text2);


        Typeface face = Typeface.createFromAsset(context.getResources().getAssets(), "Transformers.ttf");
        textViewValue.setTypeface(face);
        textView.setTypeface(face);

        this.setwaterValue(64);


        // this.setwaterValue(9);


    }


    public void setwaterValue(int value) {


        textViewValue.setText(value + "%");

        if (value == 0) {

            imageView.setBackgroundResource(R.drawable.oxy_00);
            textView.setText("DANGER");


        } else if (value > 0 && value <= 10) {

            imageView.setBackgroundResource(R.drawable.oxy_01);
            textView.setText("DANGER");


        } else if (value > 10 && value <= 20) {

            imageView.setBackgroundResource(R.drawable.oxy_02);
            textView.setText("DANGER");


        } else if (value > 20 && value <= 30) {


            imageView.setBackgroundResource(R.drawable.oxy_03);
            textView.setText("REFILLING");

        } else if (value > 30 && value <= 40) {//红色


            imageView.setBackgroundResource(R.drawable.oxy_04);
            textView.setText("REFILLING");

        } else if (value > 40 && value <= 50) {//黄色

            imageView.setBackgroundResource(R.drawable.oxy_05);
            textView.setText("REFILLING");

        } else if (value > 50 && value <= 60) {


            imageView.setBackgroundResource(R.drawable.oxy_06);
            textView.setText("REFILLING");


        } else if (value > 60 && value <= 70) {


            imageView.setBackgroundResource(R.drawable.oxy_07);
            textView.setText("REFILLING");

        } else if (value > 70 && value <= 90) {

            imageView.setBackgroundResource(R.drawable.oxy_08);
            textView.setText("REFILLING");


        } else {//满

            imageView.setBackgroundResource(R.drawable.oxy_09);
            textView.setText("FULL");
        }
    }
}
