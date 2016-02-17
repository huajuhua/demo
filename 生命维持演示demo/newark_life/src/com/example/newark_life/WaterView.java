package com.example.newark_life;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.w3c.dom.Text;

/**
 * Created by edwin on 14-9-11.
 */
public class WaterView extends RelativeLayout {

    private ImageView imageView;
    private TextView textViewValue;


    public WaterView(Context context) {

        super(context);


    }

    public WaterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);


    }

    public WaterView(Context context, AttributeSet attrs) {

        super(context, attrs);

//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(R.layout.watervalue,null);
        LayoutInflater.from(context).inflate(R.layout.watervalue, this);


        imageView = (ImageView) findViewById(R.id.emer_water_value_bg);

        textViewValue = (TextView) findViewById(R.id.emer_water_value);

        TextView textView = (TextView) findViewById(R.id.emer_water_value_text);


        Typeface face = Typeface.createFromAsset(context.getResources().getAssets(), "Transformers.ttf");
        textViewValue.setTypeface(face);
        textView.setTypeface(face);

        this.setwaterValue(90);


        // this.setwaterValue(9);


    }


    public void setwaterValue(int value) {


        textViewValue.setText(value + "%");

        if (value == 0) {

            imageView.setBackgroundResource(R.drawable.emer_water_value_0);


        } else if (value > 0 && value <= 10) {

            imageView.setBackgroundResource(R.drawable.emer_water_value_10);


        } else if (value > 10 && value <= 15) {

            imageView.setBackgroundResource(R.drawable.emer_water_value_15);


        } else if (value > 15 && value <= 20) {


            imageView.setBackgroundResource(R.drawable.emer_water_value_20);


        } else if (value > 20 && value <= 25) {//红色


            imageView.setBackgroundResource(R.drawable.emer_water_value_25);


        } else if (value > 25 && value <= 30) {//黄色

            imageView.setBackgroundResource(R.drawable.emer_water_value_30);


        } else if (value > 30 && value <= 35) {


            imageView.setBackgroundResource(R.drawable.emer_water_value_35);


        } else if (value > 35 && value <= 40) {


            imageView.setBackgroundResource(R.drawable.emer_water_value_40);


        } else if (value > 40 && value <= 45) {

            imageView.setBackgroundResource(R.drawable.emer_water_value_45);


        } else if (value > 45 && value <= 50) {


            imageView.setBackgroundResource(R.drawable.emer_water_value_50);


        } else if (value > 50 && value <= 55) {


            imageView.setBackgroundResource(R.drawable.emer_water_value_55);

        } else if (value > 55 && value <= 60) {

            imageView.setBackgroundResource(R.drawable.emer_water_value_60);


        } else if (value > 60 && value <= 65) {


            imageView.setBackgroundResource(R.drawable.emer_water_value_65);

        } else if (value > 65 && value <= 70) {

            imageView.setBackgroundResource(R.drawable.emer_water_value_70);

        } else if (value > 70 && value <= 75) {

            imageView.setBackgroundResource(R.drawable.emer_water_value_75);


        } else if (value > 75 && value <= 80) {


            imageView.setBackgroundResource(R.drawable.emer_water_value_80);

        } else if (value > 80 && value <= 85) {


            imageView.setBackgroundResource(R.drawable.emer_water_value_85);

        } else if (value > 85 && value <= 90) {

            imageView.setBackgroundResource(R.drawable.emer_water_value_90);


        } else if (value > 90 && value < 100) {

            imageView.setBackgroundResource(R.drawable.emer_water_value_90);


        } else {//满


            imageView.setBackgroundResource(R.drawable.emer_water_value_100);

        }

    }


}
