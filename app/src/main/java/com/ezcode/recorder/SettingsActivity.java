package com.ezcode.recorder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.MITLicense;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;

public class SettingsActivity extends PreferenceActivity {

    public int buildPress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSettings);
        toolbar.setTitle(R.string.action_settings);
        toolbar.inflateMenu(R.menu.menu_settings);
        findViewById(R.id.menu_item_settings_close).setOnClickListener(v -> {
            //TODO: Save preferences before closing
            finish();
        });
        addPreferencesFromResource(R.xml.preferences_settings_activity);
        if (BuildConfig.BUILD_TYPE.contentEquals("beta") || BuildConfig.BUILD_TYPE.contentEquals("dogfood") || BuildConfig.BUILD_TYPE.contentEquals("debug")) {
            Preference preferenceJoinBeta = findPreference("key_join_beta");
            preferenceJoinBeta.setEnabled(false);
        }
        for(int x = 0; x < getPreferenceScreen().getPreferenceCount(); x++){
            PreferenceCategory lol = (PreferenceCategory) getPreferenceScreen().getPreference(x);
            for(int y = 0; y < lol.getPreferenceCount(); y++){
                Preference pref = lol.getPreference(y);
                pref.setOnPreferenceClickListener(preference -> {
                    String key = preference.getKey();
                    if (key.equals("key_join_beta")) {
                        dialogBeta();
                    } else if (key.equals("key_app_copyright")){
                        dialogLicenses();
                    } else if (key.equals("key_app_build")){
                        if (buildPress<7) {
                            buildPress = buildPress + 1;
                        } else if (buildPress>=7) {
                            buildPress=0;
                            Toast.makeText(SettingsActivity.this, "Advanced settings are now enabled. Enjoy!", Toast.LENGTH_SHORT).show();
                            Preference preferenceJoinBeta = findPreference("key_join_beta");
                            preferenceJoinBeta.setEnabled(false);
                            //SECRET CODE GOES HERE
                        }
                    }
                    return false;
                });
            }
        }
    }

    public void dialogLicenses() {

        final Notices notices = new Notices();
        notices.addNotice(new Notice("Sound Recorder", "https://play.google.com/store/apps/details?id=com.ezcode.recorder", "Copyright (c) 2016-2017 EZ Code Developers", new MITLicense()));
        notices.addNotice(new Notice("MaterialDialogs API", "https://github.com/afollestad/material-dialogs", "Copyright (c) 2014-2016 Aidan Michael Follestad", new MITLicense()));

        new LicensesDialog.Builder(this)
                .setNotices(notices)
                .setIncludeOwnLicense(true)
                .build()
                .show();
    }

    public void dialogBeta() {

        new MaterialDialog.Builder(this)
                .title(R.string.dialog_join_beta_title)
                .content(R.string.dialog_join_beta_content)
                .iconRes(R.drawable.bug)
                .negativeText(R.string.action_no_thanks)
                .positiveText(R.string.action_yeah_sure)
                .onPositive((dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/apps/testing/com.ezcode.recorder"));
                    startActivity(intent);
                })
                .onNegative((dialog, which) -> dialog.dismiss())
                .show();
    }
}
