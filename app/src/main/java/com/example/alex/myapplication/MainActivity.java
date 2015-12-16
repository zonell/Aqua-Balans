package com.example.alex.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.myapplication.support.BottleParams;

public class MainActivity extends AppCompatActivity {
    final static private String COUNT_BOTTLE = "count_bottle";

    private SharedPreferences sPref;
    private ImageView imgBottle;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInfo = (TextView) findViewById(R.id.tvInfo);
        imgBottle = (ImageView) findViewById(R.id.imgBottle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadDate();
    }

    @Override
    protected void onDestroy() {
        saveDate();
        super.onDestroy();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn200ml:
                if (BottleParams.count < BottleParams.bottleImg.length){
                    fillBottle();
                } else{
                    fullBottle();
                }
                break;
            case R.id.btn400ml:
                if (BottleParams.count < BottleParams.bottleImg.length && BottleParams.count != BottleParams.bottleImg.length-1){
                    BottleParams.count++;
                    fillBottle();
                } else if (BottleParams.count == BottleParams.bottleImg.length-1){
                    Toast.makeText(getApplicationContext(),"Bottle empty!!!Drink little", Toast.LENGTH_SHORT).show();
                } else {
                    fullBottle();
                }
                break;
            case R.id.btnClear:
                BottleParams.count = 0;
                tvInfo.setText(BottleParams.EMPTY);
                imgBottle.setImageDrawable(getResources().getDrawable(R.drawable.empty));
                saveDate();
                break;
        }
    }

    private void fullBottle() {
        tvInfo.setText(BottleParams.FULL);
        Toast.makeText(getApplicationContext(),"FULL BOTTLE =)", Toast.LENGTH_SHORT).show();
    }

    private void fillBottle() {
        tvInfo.setText(BottleParams.bottleTxt[BottleParams.count]);
        imgBottle.setImageDrawable(getResources().getDrawable(BottleParams.bottleImg[BottleParams.count]));
        BottleParams.count++;
        saveDate();
    }

    public void saveDate(){
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putInt(COUNT_BOTTLE, BottleParams.count);
        editor.apply();
    }

    public void loadDate(){
        sPref = getPreferences(MODE_PRIVATE);
        BottleParams.count = sPref.getInt(COUNT_BOTTLE, 0);

        if (BottleParams.count != 0){
            imgBottle.setImageDrawable(getResources().getDrawable(BottleParams.bottleImg[BottleParams.count - 1]));
            tvInfo.setText(BottleParams.bottleTxt[BottleParams.count - 1]);
        } else {
            imgBottle.setImageDrawable(getResources().getDrawable(R.drawable.empty));
            tvInfo.setText(R.string.ml0);
        }
    }
}
