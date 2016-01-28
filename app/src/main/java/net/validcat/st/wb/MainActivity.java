package net.validcat.st.wb;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.validcat.st.wb.support.BottleParams;

public class MainActivity extends AppCompatActivity {
    final static private String COUNT_BOTTLE = "count_bottle";

    private SharedPreferences sPref;
    private ImageView imgBottle;
    private ImageView imgFull;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        loadDate();
    }

    @Override
    protected void onDestroy() {
        saveDate();
        super.onDestroy();
    }

    private void initUI(){
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        imgBottle = (ImageView) findViewById(R.id.imgBottle);
        imgFull = (ImageView) findViewById(R.id.imgFull);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
                    fillBottle();
                } else {
                    fullBottle();
                }
                break;
            case R.id.btnClear:
                BottleParams.count = 0;
                tvInfo.setText(BottleParams.EMPTY);
                imgBottle.setImageDrawable(getResources().getDrawable(R.drawable.empty));
                imgFull.setImageDrawable(null);
                saveDate();
                break;
        }
    }

    private void fullBottle() {
        tvInfo.setText(BottleParams.FULL);
        imgFull.setImageResource(R.drawable.ic_full);
        Toast.makeText(getApplicationContext(), R.string.full_bottle, Toast.LENGTH_SHORT).show();
    }

    private void fillBottle() {
        tvInfo.setText(BottleParams.bottleTxt[BottleParams.count]);
        imgBottle.setImageDrawable(getResources().getDrawable(BottleParams.bottleImg[BottleParams.count]));
        BottleParams.count++;
        if (BottleParams.count == BottleParams.bottleImg.length){
            fullBottle();
        }
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
