package net.validcat.st.wb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.validcat.st.wb.model.Constants;
import net.validcat.st.wb.support.BottleParams;
import net.validcat.st.wb.support.SettingsActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String COUNT_BOTTLE = "count_bottle";
    private static final String COUNT_CANCEL = "count_cancel";

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
    protected void onStop() {
        super.onStop();
        saveDate();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.nav_progress:
                if (getApplicationContext() != MainActivity.this){
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.nav_give_feedback:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URI_FEEDBACK)));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initUI(){
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        imgBottle = (ImageView) findViewById(R.id.imgBottle);
        imgFull = (ImageView) findViewById(R.id.imgFull);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn200ml:
                if (BottleParams.count < BottleParams.bottleImg.length){
                    fillBottle();
                } else{
                    fullBottle();
                }
                BottleParams.count_cancel = BottleParams.CANCEL_200_ML;
                break;
            case R.id.btn400ml:
                if (BottleParams.count < BottleParams.bottleImg.length
                        && BottleParams.count != BottleParams.bottleImg.length-1){
                    BottleParams.count++;
                    fillBottle();
                } else if (BottleParams.count == BottleParams.bottleImg.length-1){
                    fillBottle();
                } else {
                    fullBottle();
                }
                BottleParams.count_cancel = BottleParams.CANCEL_400_ML;
                break;
            case R.id.btnCancel:
                cancel();
                break;
            case R.id.btnClear:
                clear();
                break;
        }
    }

    private void cancel(){
        if (BottleParams.count - BottleParams.count_cancel < 0){
            clear();
        }
        else {
            BottleParams.count = BottleParams.count - BottleParams.count_cancel;
            fillBottle();
            BottleParams.count_cancel = BottleParams.COUNT_CANCEL;
            deleteImgFull();
        }
        saveDate();
    }

    private void clear(){
        BottleParams.count = 0;
        tvInfo.setText(BottleParams.EMPTY);
        imgBottle.setImageDrawable(getResources().getDrawable(R.drawable.empty));
        deleteImgFull();
        saveDate();
    }

    private void fullBottle() {
        tvInfo.setText(BottleParams.FULL);
        imgFull.setImageResource(R.drawable.ic_full);
        saveDate();
        Toast.makeText(getApplicationContext(), R.string.full_bottle, Toast.LENGTH_SHORT).show();
    }

    private void fillBottle() {
        tvInfo.setText(BottleParams.bottleTxt[BottleParams.count]);
        imgBottle.setImageDrawable(getResources().getDrawable(
                BottleParams.bottleImg[BottleParams.count]));
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
        editor.putInt(COUNT_CANCEL, BottleParams.count_cancel);
        editor.apply();
    }

    public void loadDate(){
        sPref = getPreferences(MODE_PRIVATE);
        BottleParams.count = sPref.getInt(COUNT_BOTTLE, 0);
        BottleParams.count_cancel = sPref.getInt(COUNT_CANCEL, BottleParams.COUNT_CANCEL);

        if (BottleParams.count != 0){
            imgBottle.setImageDrawable(getResources().getDrawable(
                    BottleParams.bottleImg[BottleParams.count - 1]));
            tvInfo.setText(BottleParams.bottleTxt[BottleParams.count - 1]);
        }
        else {
            imgBottle.setImageDrawable(getResources().getDrawable(R.drawable.empty));
            tvInfo.setText(R.string.ml0);
        }

        if (BottleParams.count == BottleParams.bottleTxt.length && BottleParams.count != 0){
            imgBottle.setImageDrawable(getResources().getDrawable(
                    BottleParams.bottleImg[BottleParams.count - 1]));
            imgFull.setImageResource(R.drawable.ic_full);
            tvInfo.setText(R.string.full);
        }
    }

    private void deleteImgFull(){
        imgFull.setImageDrawable(null);
    }
}
