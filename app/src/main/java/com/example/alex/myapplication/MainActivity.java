package com.example.alex.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.myapplication.support.BottleSize;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sPref;

    final private String COUNT_BOTTLE = "count_bottle";

    private ImageView imgBottle;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvInfo = (TextView) findViewById(R.id.tvInfo);

        imgBottle = (ImageView) findViewById(R.id.imgBottle);

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
                    if (BottleSize.count < BottleSize.bottleImg.length){
                        tvInfo.setText(BottleSize.bottleTxt[BottleSize.count]);
                        imgBottle.setImageDrawable(getResources().getDrawable(BottleSize.bottleImg[BottleSize.count]));
                        BottleSize.count++;
                        saveDate();
                    }
                else{
                        tvInfo.setText(R.string.full);
                        Toast.makeText(getApplicationContext(),"FULL BOTTLE =)", Toast.LENGTH_SHORT).show();
                    }
                break;
            case R.id.btn400ml:
                if (BottleSize.count < BottleSize.bottleImg.length && BottleSize.count != BottleSize.bottleImg.length-1){
                    BottleSize.count++;
                    tvInfo.setText(BottleSize.bottleTxt[BottleSize.count]);
                    imgBottle.setImageDrawable(getResources().getDrawable(BottleSize.bottleImg[BottleSize.count]));
                    BottleSize.count++;
                    saveDate();
                }
                else if (BottleSize.count == BottleSize.bottleImg.length-1){
                    Toast.makeText(getApplicationContext(),"Bottle empty!!!Drink little", Toast.LENGTH_SHORT).show();
                }
                else {
                    tvInfo.setText(R.string.full);
                    Toast.makeText(getApplicationContext(),"FULL BOTTLE =)", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnClear:
                BottleSize.count = 0;
                tvInfo.setText(R.string.ml0);
                imgBottle.setImageDrawable(getResources().getDrawable(R.drawable.empty));
                saveDate();
                break;
        }
    }

    public void saveDate(){
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putInt(COUNT_BOTTLE, BottleSize.count);
        editor.commit();
    }

    public void loadDate(){
        sPref = getPreferences(MODE_PRIVATE);
        BottleSize.count = sPref.getInt(COUNT_BOTTLE, 0);
        String SHOW_SPLASH = "show_splash";
        sPref.getInt(SHOW_SPLASH, 1);

        if (BottleSize.count != 0){
            imgBottle.setImageDrawable(getResources().getDrawable(BottleSize.bottleImg[BottleSize.count - 1]));
            tvInfo.setText(BottleSize.bottleTxt[BottleSize.count - 1]);
        }
        else {
            imgBottle.setImageDrawable(getResources().getDrawable(R.drawable.empty));
            tvInfo.setText(R.string.ml0);
        }
    }
}
