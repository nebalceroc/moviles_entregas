package com.example.nicolas.reto7_tictactoepref;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class Settings extends PreferenceActivity{

    @Override
    protected void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final ListPreference difficultyLevelPref = (ListPreference) findPreference("difficulty_level");
        String difficulty = prefs.getString("difficulty_level",
                getResources().getString(R.string.difficulty_expert));
        difficultyLevelPref.setSummary((CharSequence) difficulty);

        difficultyLevelPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                difficultyLevelPref.setSummary((CharSequence) newValue);
                SharedPreferences.Editor ed = prefs.edit();
                ed.putString("difficulty_level", newValue.toString());
                ed.commit();
                return true;
            }
        });

        final EditTextPreference victoryMessagePref = (EditTextPreference) findPreference("victory_message");
        String victoryMessage = prefs.getString("victory_message",
                getResources().getString(R.string.result_human_wins));
        victoryMessagePref.setSummary((CharSequence) victoryMessage);
        victoryMessagePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                victoryMessagePref.setSummary((CharSequence) newValue);
                SharedPreferences.Editor ed = prefs.edit();
                ed.putString("victory_message", newValue.toString());
                ed.commit();
                return true;
            }
        });

        final ColorPickerPreference colorPickerPreference = (ColorPickerPreference) findPreference("color");
        String colorMessage = String.valueOf(prefs.getInt("color",0));
        colorPickerPreference.setSummary((CharSequence) colorMessage);
        colorPickerPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                colorPickerPreference.setSummary((CharSequence) newValue.toString());
                SharedPreferences.Editor ed = prefs.edit();
                ed.putInt("color", (int) newValue);
                ed.commit();
                return true;
            }
        });

    }

}
