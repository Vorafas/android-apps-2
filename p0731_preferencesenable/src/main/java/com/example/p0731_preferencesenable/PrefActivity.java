package com.example.p0731_preferencesenable;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.widget.CheckBox;

public class PrefActivity extends PreferenceActivity {
    CheckBoxPreference chb3;
    PreferenceCategory categ2;

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        addPreferencesFromResource(R.xml.pref);

        chb3 = (CheckBoxPreference) findPreference("chb3");
        categ2 = (PreferenceCategory) findPreference("categ2");
        categ2.setEnabled(chb3.isChecked());

        chb3.setOnPreferenceClickListener((Preference preference) -> {
            categ2.setEnabled(chb3.isChecked());
            return false;
        });
    }
}
