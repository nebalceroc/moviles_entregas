package com.example.nicolas.reto7_tictactoepref;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerDialog;
import com.skydoves.colorpickerpreference.ColorPickerPreference;

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
        colorPickerPreference.setSummary((CharSequence) "#000000");
        colorPickerPreference.getColorPickerDialogBuilder().setOnColorListener(new ColorListener() {
            @Override
            public void onColorSelected(ColorEnvelope colorEnvelope) {
                Log.e("CD",colorEnvelope.toString());
                colorPickerPreference.setSummary((CharSequence) colorEnvelope.getColorHtml());
                SharedPreferences.Editor ed = prefs.edit();
                ed.putString("color", colorEnvelope.getColorHtml());
                ed.commit();
            }
        });
    }

}
