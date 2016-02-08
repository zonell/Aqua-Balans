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
import android.view.ContextMenu;
import android.view.MenuInflater;
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
        getAquaBalance();
        loadDate();
        setTextBtbMin();
        setTextBtbMax();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        calculationNormsWater = new CalculationNormsWater();

        tvInfo = (TextView) findViewById(R.id.tvInfo);
        tvPercentDrink = (TextView) findViewById(R.id.percentDrink);
        imgBottle = (ImageView) findViewById(R.id.imgBottle);
        imgFull = (ImageView) findViewById(R.id.imgFull);

        btnMin = (FloatingActionButton) findViewById(R.id.btnMin);
        btnMax = (FloatingActionButton) findViewById(R.id.btnMax);

        registerForContextMenu(btnMin);

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
        switch (id) {
            case R.id.nav_progress:
                if (Constants.COUNT_MENU != Constants.MENU_PROGRESS) {
                    Constants.COUNT_MENU = Constants.MENU_PROGRESS;
                    startActivity(new Intent(this, MainActivity.class));
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ml_50:
                calculationNormsWater.drink_volume_min = 50;
                setTextBtbMin();
                break;
            case R.id.ml_75:
                calculationNormsWater.drink_volume_min = 75;
                setTextBtbMin();
                break;
            case R.id.ml_100:
                calculationNormsWater.drink_volume_min = 100;
                setTextBtbMin();
                break;
            case R.id.ml_150:
                calculationNormsWater.drink_volume_min = 150;
                setTextBtbMin();
                break;
            case R.id.ml_200:
                calculationNormsWater.drink_volume_min = 200;
                setTextBtbMin();
                break;
            case R.id.ml_250:
                calculationNormsWater.drink_volume_min = 250;
                setTextBtbMin();
                break;
            case R.id.ml_500:
                calculationNormsWater.drink_volume_min = 500;
                setTextBtbMin();
                break;
        }
        return true;
    }

    private void setTextBtbMin() {
        btnMin.setTitle(getResources().getString(R.string.drink) + " "
                + calculationNormsWater.drink_volume_min
                + getResources().getString(R.string.ml));
    }

    private void setTextBtbMax() {
        btnMax.setTitle(getResources().getString(R.string.drink) + " "
                + calculationNormsWater.drink_volume_max
                + getResources().getString(R.string.ml));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnMin:
                fill(calculationNormsWater.drink_volume_min);
                break;
            case R.id.btnMax:
                fill(calculationNormsWater.drink_volume_max);
                break;
            case R.id.btnCancel:
                cancel();
                break;
            case R.id.btnClear:
                clear();
                break;
        }
    }

    private void fill(int drink_volume) {
        fillBottle(drink_volume);
    }

    private void fillBottle(int drink_volume) {
        saveCountDrink();
        int sum = BottleParams.count_drink += drink_volume;
        setText();
        if (sum > BottleParams.aqua_balance) {
            fullBottle();
        }
    }

    private void fullBottle() {
        setText();
        Toast.makeText(getApplicationContext(), R.string.full_bottle, Toast.LENGTH_SHORT).show();
    }

    private void cancel() {
        loadCountDrink();
        setText();
    }

    private void clear() {
        saveCountDrink();
        BottleParams.count_drink = BottleParams.EMPTY;
        BottleParams.percent_drink = BottleParams.EMPTY;
        imgBottle.setImageDrawable(getResources().getDrawable(R.drawable.empty));
        setText();
    }

    public void saveDate() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putInt(Constants.COUNT_DRINK, BottleParams.count_drink);
//        editor.putInt(Constants.DRINK_MIN, calculationNormsWater.drink_volume_min);
        editor.apply();
    }

    public void saveCountDrink(){
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putInt(Constants.COUNT_CANCEL, BottleParams.count_drink);
        editor.apply();
    }

    public void loadCountDrink(){
        sPref = getPreferences(MODE_PRIVATE);
        BottleParams.count_drink = sPref.getInt(Constants.COUNT_CANCEL, BottleParams.count_drink);
    }

    public void loadDate() {
        sPref = getPreferences(MODE_PRIVATE);
        BottleParams.count_drink = sPref.getInt(Constants.COUNT_DRINK, BottleParams.count_drink);
//        calculationNormsWater.drink_volume_min = sPref.getInt(Constants.DRINK_MIN, calculationNormsWater.drink_volume_min);
        setText();
    }

    private void deleteImgFull() {
        imgFull.setImageDrawable(null);
    }

    private void getAquaBalance() {
        BottleParams.aqua_balance = calculationNormsWater.calculationAquaBalance();
    }

    private void setText() {
        if (BottleParams.count_drink < BottleParams.aqua_balance) {
            tvInfo.setText(BottleParams.count_drink + "/" + BottleParams.aqua_balance);
            deleteImgFull();
        } else {
            tvInfo.setText(getResources().getString(BottleParams.FULL) + " "
                    + BottleParams.count_drink + "/" + BottleParams.aqua_balance);
            imgFull.setImageResource(R.drawable.ic_full);
        }

        getPercent();

        if (BottleParams.percent_drink < 30){
            tvPercentDrink.setTextColor(getResources().getColor(R.color.percent_30));
            setPercent();
        }
        else{
            if (BottleParams.percent_drink < 70){
                tvPercentDrink.setTextColor(getResources().getColor(R.color.percent_70));
                setPercent();
            }
            else {
                tvPercentDrink.setTextColor(getResources().getColor(R.color.percent_90));
                setPercent();
            }
        }
        saveDate();
    }

    private void getPercent() {
        BottleParams.percent_drink = calculationNormsWater.calculationPercentDrink();
    }

    private void setPercent(){
        tvPercentDrink.setText("" + BottleParams.percent_drink + "%");
    }
}
