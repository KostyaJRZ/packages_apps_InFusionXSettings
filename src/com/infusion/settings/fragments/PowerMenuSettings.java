/*
 * Copyright (C) 2014-2015 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.infusion.settings.fragments;

import android.content.Context;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.UserInfo;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;
import android.provider.Settings;
import com.android.settings.R;
import androidx.annotation.NonNull;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.SettingsPreferenceFragment;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import com.infusion.settings.preferences.Utils;

public class PowerMenuSettings extends SettingsPreferenceFragment
                implements Preference.OnPreferenceChangeListener {

    private static final String KEY_POWERMENU_LOGOUT = "powermenu_logout";
    private static final String KEY_POWERMENU_TORCH = "powermenu_torch";
    private static final String KEY_POWERMENU_USERS = "powermenu_users";

    private SwitchPreference mPowermenuLogout;
    private SwitchPreference mPowermenuTorch;
    private SwitchPreference mPowermenuUsers;

    private UserManager mUserManager;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.infusion_settings_power);

        final ContentResolver resolver = getActivity().getContentResolver();
        final PreferenceScreen prefScreen = getPreferenceScreen();

        mPowermenuTorch = (SwitchPreference) findPreference(KEY_POWERMENU_TORCH);
        mPowermenuTorch.setOnPreferenceChangeListener(this);
        if (!Utils.deviceSupportsFlashLight(getActivity())) {
            prefScreen.removePreference(mPowermenuTorch);
        } else {
        mPowermenuTorch.setChecked((Settings.System.getInt(resolver,
                Settings.System.POWERMENU_TORCH, 0) == 1));
        }

        mPowermenuLogout = (SwitchPreference) findPreference(KEY_POWERMENU_LOGOUT);
        mPowermenuLogout.setOnPreferenceChangeListener(this);
        mPowermenuUsers = (SwitchPreference) findPreference(KEY_POWERMENU_USERS);
        mPowermenuUsers.setOnPreferenceChangeListener(this);
        if (!mUserManager.supportsMultipleUsers()) {
            prefScreen.removePreference(mPowermenuLogout);
            prefScreen.removePreference(mPowermenuUsers);
        } else {
            mPowermenuLogout.setChecked((Settings.System.getInt(resolver,
                    Settings.System.POWERMENU_LOGOUT, 0) == 1));
            mPowermenuUsers.setChecked((Settings.System.getInt(resolver,
                    Settings.System.POWERMENU_USERS, 0) == 1));
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mPowermenuLogout) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.POWERMENU_LOGOUT, value ? 1 : 0);
            return true;
        } else if (preference == mPowermenuTorch) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.POWERMENU_TORCH, value ? 1 : 0);
            return true;
        } else if (preference == mPowermenuUsers) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.POWERMENU_USERS, value ? 1 : 0);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.INFUSION_SETTINGS;
    }

}
