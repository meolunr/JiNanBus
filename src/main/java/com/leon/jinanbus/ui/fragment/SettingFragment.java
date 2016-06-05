package com.leon.jinanbus.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.leon.jinanbus.R;

public class SettingFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setting);
    }
}