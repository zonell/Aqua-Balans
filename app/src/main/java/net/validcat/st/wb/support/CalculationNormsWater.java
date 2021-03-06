package net.validcat.st.wb.support;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class CalculationNormsWater {
    private Integer weightPreference;
    private int agePreference;
    private int sportPreference;
    private int weatherPreference;
    private int aquaBalance;

    public int drink_volume_min;
    public int drink_volume_max;

    private int percentOfSport;
    private int percentOfWeather;

    public void getPref(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        try {
            weightPreference = Integer.parseInt(preferences.getString("menu_weight", "70"));
            if (weightPreference == 0){
                weightPreference = 70;
            }
        }
        catch (Exception e){
            weightPreference = 70;
        }
        agePreference = Integer.parseInt(preferences.getString("menu_age", "33"));
        sportPreference = Integer.parseInt(preferences.getString("menu_sport", "10"));
        weatherPreference = Integer.parseInt(preferences.getString("menu_weather", "5"));

        try{
            drink_volume_min = Integer.parseInt(preferences.getString("menu_volume_min", "200"));
        }
        catch (Exception e){
            drink_volume_min = 200;
        }
        if (drink_volume_min > 1000){
            drink_volume_min = 1000;
        }

        try{
            drink_volume_max = Integer.parseInt(preferences.getString("menu_volume_max", "400"));
        }
        catch (Exception e){
            drink_volume_max = 400;
        }
        if (drink_volume_max > 1000) {
            drink_volume_max = 1000;
        }
    }

    public int calculationAquaBalance(){
        aquaBalance = (agePreference * weightPreference);
        percentOfSport = (aquaBalance * sportPreference) / 100;
        percentOfWeather = (aquaBalance * weatherPreference) / 100;

        return aquaBalance += percentOfSport + percentOfWeather;
    }

    public int calculationPercentDrink(){
        return (BottleParams.count_drink * 100) / aquaBalance;
    }
}
