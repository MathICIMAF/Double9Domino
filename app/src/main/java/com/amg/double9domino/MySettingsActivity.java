package com.amg.double9domino;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class MySettingsActivity extends PreferenceActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
