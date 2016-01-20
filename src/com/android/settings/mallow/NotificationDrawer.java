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

import com.android.internal.logging.MetricsLogger;

import android.app.ActivityManager;
import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.text.TextUtils;
import android.view.View;

import com.android.settings.mallow.SeekBarPreferenceCHOS;

import java.util.ArrayList;
import java.util.Locale;
import java.util.List;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

public class NotificationDrawer extends SettingsPreferenceFragment
    implements OnPreferenceChangeListener {

    private static final String PRE_QUICK_PULLDOWN = "quick_pulldown";
    private static final String PREF_QS_TRANSPARENT_SHADE = "qs_transparent_shade";
	private static final String PREF_QS_TRANSPARENT_HEADER = "qs_transparent_header";

    private ListPreference mQuickPulldown;
    private SeekBarPreferenceCHOS mQSShadeAlpha;
    private SeekBarPreferenceCHOS mQSHeaderAlpha;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.notification_drawer);

        PreferenceScreen prefSet = getPreferenceScreen();

        // Quick pulldown
        mQuickPulldown = (ListPreference) findPreference(PRE_QUICK_PULLDOWN);
        if (!Utils.isPhone(getActivity())) {
            prefSet.removePreference(mQuickPulldown);
        } else {
            mQuickPulldown.setOnPreferenceChangeListener(this);
            int statusQuickPulldown = Settings.System.getInt(getContentResolver(),
                    Settings.System.QS_QUICK_PULLDOWN, 0);
            mQuickPulldown.setValue(String.valueOf(statusQuickPulldown));
            updateQuickPulldownSummary(statusQuickPulldown);
        }

        // QS shade alpha
        mQSShadeAlpha =
        (SeekBarPreferenceCham) prefSet.findPreference(PREF_QS_TRANSPARENT_SHADE);
        int qSShadeAlpha = Settings.System.getInt(getContentResolver(),
                    Settings.System.QS_TRANSPARENT_SHADE, 255);
        mQSShadeAlpha.setValue(qSShadeAlpha / 1);
        mQSShadeAlpha.setOnPreferenceChangeListener(this);

		// QS header alpha
        mQSHeaderAlpha =
        	(SeekBarPreferenceCham) prefSet.findPreference(PREF_QS_TRANSPARENT_HEADER);
        int qSHeaderAlpha = Settings.System.getInt(getContentResolver(),
        	Settings.System.QS_TRANSPARENT_HEADER, 255);
        mQSHeaderAlpha.setValue(qSHeaderAlpha / 1);
        mQSHeaderAlpha.setOnPreferenceChangeListener(this);
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.DONT_TRACK_ME_BRO;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mQuickPulldown) {
            int statusQuickPulldown = Integer.valueOf((String) objValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.QS_QUICK_PULLDOWN,
                    statusQuickPulldown);
            updateQuickPulldownSummary(statusQuickPulldown);
            return true;
        } else if (preference == mQSShadeAlpha) {
            int alpha = (Integer) objValue;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.QS_TRANSPARENT_SHADE, alpha * 1);
            return true;
		} else if (preference == mQSHeaderAlpha) {
            int alpha = (Integer) objValue;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.QS_TRANSPARENT_HEADER, alpha * 1);
            return true;			
        }
        return false;
    }

    private void updateQuickPulldownSummary(int value) {
        Resources res = getResources();

        if (value == 0) {
            // quick pulldown deactivated
            mQuickPulldown.setSummary(res.getString(R.string.quick_pulldown_off));
        } else {
            Locale l = Locale.getDefault();
            boolean isRtl = TextUtils.getLayoutDirectionFromLocale(l) == View.LAYOUT_DIRECTION_RTL;
            String direction = res.getString(value == 2
                    ? (isRtl ? R.string.quick_pulldown_right : R.string.quick_pulldown_left)
                    : (isRtl ? R.string.quick_pulldown_left : R.string.quick_pulldown_right));
            mQuickPulldown.setSummary(res.getString(R.string.summary_quick_pulldown, direction));
        }
    }
}
