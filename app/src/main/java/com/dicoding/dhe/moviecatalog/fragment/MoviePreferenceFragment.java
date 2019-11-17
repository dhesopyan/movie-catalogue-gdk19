/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.moviecatalog.fragment;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.dicoding.dhe.moviecatalog.R;
import com.dicoding.dhe.moviecatalog.helper.LocaleHelper;
import com.dicoding.dhe.moviecatalog.util.AlarmReceiver;

import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass.
 */

public class MoviePreferenceFragment extends android.preference.PreferenceFragment implements Preference.OnPreferenceChangeListener {
    String reminder_daily;
    String reminder_release;
    String language;
    private static AlarmReceiver alarmReceiver = new AlarmReceiver();

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Paper.init(getActivity());

        language = Paper.book().read("language");
        if(language == null)
            Paper.book().write("language","en");

        updateView((String)Paper.book().read("language"));

        reminder_daily = getString(R.string.key_reminder_daily);
        reminder_release = getString(R.string.key_reminder_release);

        findPreference(reminder_daily).setOnPreferenceChangeListener(this);
        findPreference(reminder_release).setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        String key = preference.getKey();
        boolean isOn = (boolean) o;

        if (key.equals(reminder_daily)) {
            if (isOn) {
                alarmReceiver.setRepeatingAlarm(getActivity(), alarmReceiver.TYPE_REPEATING, getString(R.string.label_alarm_daily_reminder));
            } else {
                alarmReceiver.cancelAlarm(getActivity(), AlarmReceiver.TYPE_REPEATING);
            }

            Toast.makeText(getActivity(), getString(R.string.label_daily_reminder) + " " + (isOn ? getString(R.string.label_activated) : getString(R.string.label_deactivated)), Toast.LENGTH_SHORT).show();
            return true;
        }

        if (key.equals(reminder_release)) {
            if (isOn) {
                alarmReceiver.setMovieAlarm(getActivity(), alarmReceiver.TYPE_UPCOMING, getString(R.string.label_daily_reminder));
            } else {
                alarmReceiver.cancelAlarm(getActivity(), AlarmReceiver.TYPE_UPCOMING);
            }
            Toast.makeText(getActivity(), getString(R.string.label_release_reminder) + " " + (isOn ? getString(R.string.label_activated) : getString(R.string.label_deactivated)), Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    private void updateView(String lang) {
        Context context = LocaleHelper.setLocale(getActivity(),lang);
        Resources resources = context.getResources();
        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(resources.getString(R.string.settings));
        }
    }




}