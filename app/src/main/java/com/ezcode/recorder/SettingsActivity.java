package com.ezcode.recorder;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Set;

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
        findViewById(R.id.menu_item_settings_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Save preferences before closing
                //Intent returnToMainIntent = new Intent(SettingsActivity.this, MainActivity.class);
                //startActivity(returnToMainIntent);
                finish();
            }
        });
        addPreferencesFromResource(R.xml.preferences_settings_activity);
        for(int x = 0; x < getPreferenceScreen().getPreferenceCount(); x++){
            PreferenceCategory lol = (PreferenceCategory) getPreferenceScreen().getPreference(x);
            for(int y = 0; y < lol.getPreferenceCount(); y++){
                Preference pref = lol.getPreference(y);
                pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
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
                                //SECRET CODE GOES HERE
                            }
                        }
                        return false;
                    }
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
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/apps/testing/com.ezcode.recorder"));
                        startActivity(intent);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
