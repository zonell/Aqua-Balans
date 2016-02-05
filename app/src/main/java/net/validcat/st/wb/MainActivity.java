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

import com.getbase.floatingactionbutton.FloatingActionButton;

import net.validcat.st.wb.model.Constants;
import net.validcat.st.wb.support.BottleParams;
import net.validcat.st.wb.support.CalculationNormsWater;
import net.validcat.st.wb.support.SettingsActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CalculationNormsWater calculationNormsWater;

    private int aquaBalance;
    private double percentDrink;

    public SharedPreferences sPref;
    private ImageView imgBottle;
    private ImageView imgFull;
    private TextView tvInfo;
    private TextView tvPercentDrink;

    private FloatingActionButton btnMax;
    private FloatingActionButton btnMin;

    @Override
    protected void onStart() {
        super.onStart();
        calculationNormsWater.getPref(this);
        aquaBalance  = calculationNormsWater.calculationAquaBalance();
        percentDrink = calculationNormsWater.calculationPercentDrink();

        btnMin.setTitle(getResources().getString(R.string.drink) + " "
                + calculationNormsWater.drink_volume_min
                + getResources().getString(R.string.ml));

        btnMax.setTitle(getResources().getString(R.string.drink) + " "
                + calculationNormsWater.drink_volume_max
                + getResources().getString(R.string.ml));

        loadDate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
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
                if (Constants.COUNT_MENU != Constants.MENU_PROGRESS){
                    Constants.COUNT_MENU = Constants.MENU_PROGRESS;
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
        tvPercentDrink = (TextView) findViewById(R.id.percentDrink);
        imgBottle = (ImageView) findViewById(R.id.imgBottle);
        imgFull = (ImageView) findViewById(R.id.imgFull);
        calculationNormsWater = new CalculationNormsWater();

        btnMin = (FloatingActionButton) findViewById(R.id.btnMin);
        btnMax = (FloatingActionButton) findViewById(R.id.btnMax);

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
            case R.id.btnMin:
                fill(calculationNormsWater.drink_volume_min, BottleParams.CANCEL_200_ML);
                break;
            case R.id.btnMax:
                fill(calculationNormsWater.drink_volume_max, BottleParams.CANCEL_400_ML);
                break;
            case R.id.btnCancel:
                cancel();
                break;
            case R.id.btnClear:
                clear();
                break;
        }
    }

    private void fill(int drink_volume_min, int COUNT_CANCEL){
        fillBottle(drink_volume_min);
        BottleParams.count_cancel = COUNT_CANCEL;
    }

    private void fillBottle(int volume) {
        int sum = BottleParams.count_drink += volume;
        setText();
        saveDate();
        if (sum > aquaBalance){
            fullBottle();
        }
    }

    private void fullBottle() {
        setText();
        saveDate();
        Toast.makeText(getApplicationContext(), R.string.full_bottle, Toast.LENGTH_SHORT).show();
    }

    private void cancel(){
        switch (BottleParams.count_cancel){
            case 1:
                break;
            case 2:
                remove(calculationNormsWater.drink_volume_min);
                break;
            case 3:
                remove(calculationNormsWater.drink_volume_max);
                break;
        }
        saveDate();
    }

    private void remove(int drink_volume){
        if (BottleParams.count_drink > 0) {
            BottleParams.count_drink -= drink_volume;
            BottleParams.count_cancel = BottleParams.COUNT_CANCEL;
            setText();
        }
        if (BottleParams.count_drink < aquaBalance){
            deleteImgFull();
        }
    }

    private void clear(){
        BottleParams.count_drink = 0;
        tvInfo.setText(BottleParams.count_drink + "/" + aquaBalance);
        imgBottle.setImageDrawable(getResources().getDrawable(R.drawable.empty));
        deleteImgFull();
        saveDate();

    }

    public void saveDate(){
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putInt(Constants.COUNT_CANCEL, BottleParams.count_cancel);
        editor.putInt(Constants.COUNT_DRINK, BottleParams.count_drink);
        editor.apply();
    }

    public void loadDate(){
        sPref = getPreferences(MODE_PRIVATE);
        BottleParams.count_cancel = sPref.getInt(Constants.COUNT_CANCEL, BottleParams.COUNT_CANCEL);
        BottleParams.count_drink = sPref.getInt(Constants.COUNT_DRINK, BottleParams.count_drink);

        setText();
    }

    private void deleteImgFull(){
        imgFull.setImageDrawable(null);
    }

    private void setText(){
        if (BottleParams.count_drink < aquaBalance){
            tvInfo.setText(BottleParams.count_drink + "/" + aquaBalance);
            deleteImgFull();
        }
        else {
            tvInfo.setText(getResources().getString(BottleParams.FULL)
                    + " " + BottleParams.count_drink + "/" + aquaBalance);
            imgFull.setImageResource(R.drawable.ic_full);
        }
    }
}
