/*
* Copyright (C) 2015 MallowRom
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

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

import com.android.settings.R;
import com.android.internal.logging.MetricsLogger;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.mallow.SeekBarPreferenceCHOS;

public class StatusBar extends SettingsPreferenceFragment
        implements OnPreferenceChangeListener {

    private static final String TAG = "StatusBar";

    private static final String CUSTOM_HEADER_IMAGE_SHADOW = "status_bar_custom_header_shadow";

    private SeekBarPreferenceCHOS mHeaderShadow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.status_bar);

        // Status bar custom header shadow
		mHeaderShadow = (SeekBarPreferenceCHOS) findPreference(CUSTOM_HEADER_IMAGE_SHADOW);
        int headerShadow = Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_CUSTOM_HEADER_SHADOW, 0);
        mHeaderShadow.setValue(headerShadow);
        mHeaderShadow.setOnPreferenceChangeListener(this);
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.DONT_TRACK_ME_BRO;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference == mHeaderShadow) {
            int headerShadow = (Integer) newValue;
            Settings.System.putInt(getContentResolver(),
                Settings.System.STATUS_BAR_CUSTOM_HEADER_SHADOW, headerShadow);
            return true;
        }
        return false;
    }
}
