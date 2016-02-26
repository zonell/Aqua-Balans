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
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import net.validcat.st.wb.model.Constants;
import net.validcat.st.wb.support.BottleParams;
import net.validcat.st.wb.support.CalculationNormsWater;
import net.validcat.st.wb.support.SettingsActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private CalculationNormsWater calculationNormsWater;
    public SharedPreferences sPref;

    private ImageView imgBottle;
    private ImageView imgWater;
    private ImageView imgFull;
    private TextView tvInfo;
    private TextView tvDailyRate;
    private TextView tvDrink;
    private TextView tvPercentDrink;

    private FloatingActionButton btnMax;
    private FloatingActionButton btnMin;
    private FloatingActionsMenu fab;

    private int width;
    private int height;
    private int widthRL;

    private RelativeLayout container;

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
        tvDailyRate = (TextView) findViewById(R.id.tvDailyRate);
        tvDrink = (TextView) findViewById(R.id.tvDrink);
        tvPercentDrink = (TextView) findViewById(R.id.percentDrink);
        imgBottle = (ImageView) findViewById(R.id.imgBottle);
        imgWater = (ImageView) findViewById(R.id.imgWater);
        imgFull = (ImageView) findViewById(R.id.imgFull);

        container = (RelativeLayout) findViewById(R.id.container);

        fab = (FloatingActionsMenu) findViewById(R.id.fab);
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
        editor.putInt(Constants.WIDTH, width);
        editor.putInt(Constants.HEIGHT, height);
        editor.putInt(Constants.WIDTH_RL, widthRL);
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
        width = sPref.getInt(Constants.WIDTH, BottleParams.EMPTY);
        height = sPref.getInt(Constants.HEIGHT, BottleParams.EMPTY);
        widthRL = sPref.getInt(Constants.WIDTH_RL, BottleParams.EMPTY);
//        calculationNormsWater.drink_volume_min = sPref.getInt(Constants.DRINK_MIN, calculationNormsWater.drink_volume_min);
        setText();
    }

    private void deleteFull() {
        imgFull.setImageDrawable(null);
        tvDailyRate.setText(null);
    }

    private void getAquaBalance() {
        BottleParams.aqua_balance = calculationNormsWater.calculationAquaBalance();
    }

    private void setText() {
        if (BottleParams.count_drink < BottleParams.aqua_balance) {
            tvInfo.setText(BottleParams.count_drink + "/" + BottleParams.aqua_balance);
            deleteFull();
        } else {
            tvDailyRate.setText(getResources().getString(BottleParams.FULL));
            tvInfo.setText( BottleParams.count_drink + "/" + BottleParams.aqua_balance);
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
        draw();
        saveDate();
    }

    private void draw(){
        container.removeView(imgBottle);
        container.removeView(imgWater);
        container.removeView(imgFull);
        container.removeView(tvInfo);
        container.removeView(tvPercentDrink);
        container.removeView(tvDailyRate);
        container.removeView(tvDrink);
        container.removeView(fab);

        if (Constants.FIRST){
            Constants.FIRST = false;
        }
        else {
            getViewProperty(imgBottle, container);
        }

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((width/2), height);
        layoutParams.setMargins((widthRL / 4) + 10, getWidthWater(), 0, 0);

        imgWater.setLayoutParams(layoutParams);

        container.addView(imgWater);
        container.addView(imgBottle);
        container.addView(imgFull);
        container.addView(tvInfo);
        container.addView(tvPercentDrink);
        container.addView(tvDailyRate);
        container.addView(tvDrink);
        container.addView(fab);
    }

    private int getWidthWater(){
        int waterMaxHeight = (int) ((height*Constants.PERCENT_15)
                + (height - (height*Constants.PERCENT_5))
                - ((height - (height*Constants.PERCENT_25))
                * BottleParams.percent_drink/Constants.PERCENT));
        return waterMaxHeight < (height*Constants.PERCENT_35)
                ? (int) (height * Constants.PERCENT_35) : waterMaxHeight;
    }

    private void getViewProperty(View imgView, View containerView)
    {
        width = imgView.getWidth();
        height = imgView.getHeight();
        widthRL = containerView.getWidth();

        Log.d("width", "" + width);
        Log.d("height", "" + height);
        Log.d("widthRL", "" + widthRL);
    }

    private void getPercent() {
        BottleParams.percent_drink = calculationNormsWater.calculationPercentDrink();
    }

    private void setPercent(){
        tvPercentDrink.setText("" + BottleParams.percent_drink + "%");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Constants.FIRST = true;
    }
}