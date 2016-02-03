/*
 * Copyright (C) 2014 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.mallow;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.widget.SeekBarPreferenceCham;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.internal.logging.MetricsLogger;

public class HeadsUpSettings extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String HEADS_UP_TIMEOUT = "heads_up_timeout";
    private static final String HEADS_UP_SNOOZE_LENGTH_MS = "heads_up_snooze_length_ms";

    private SeekBarPreferenceCham mHeadsUpTimeout;
    private SeekBarPreferenceCham mHeadsUpSnooze;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get launch-able applications
        addPreferencesFromResource(R.xml.heads_up);

        mHeadsUpTimeout = (SeekBarPreferenceCham) findPreference(HEADS_UP_TIMEOUT);
        mHeadsUpTimeout.setValue(Settings.System.getInt(getContentResolver(),
                Settings.System.HEADS_UP_TIMEOUT, 10000));
        mHeadsUpTimeout.setOnPreferenceChangeListener(this);

        mHeadsUpSnooze = (SeekBarPreferenceCham) findPreference(HEADS_UP_SNOOZE_LENGTH_MS);
        mHeadsUpSnooze.setValue(Settings.System.getInt(getContentResolver(),
                Settings.System.HEADS_UP_SNOOZE_LENGTH_MS, 60 / 1000));
        mHeadsUpSnooze.setOnPreferenceChangeListener(this);
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.DONT_TRACK_ME_BRO;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mHeadsUpTimeout) {
            int length = ((Integer) objValue).intValue();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.HEADS_UP_TIMEOUT, length);
        } else if (preference == mHeadsUpSnooze) {
            int snooze = ((Integer) objValue).intValue();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.HEADS_UP_SNOOZE_LENGTH_MS, snooze);
        }
        return true;
    }
}
