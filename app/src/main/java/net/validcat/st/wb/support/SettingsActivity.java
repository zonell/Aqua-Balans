package net.validcat.st.wb.support;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import net.validcat.st.wb.R;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
