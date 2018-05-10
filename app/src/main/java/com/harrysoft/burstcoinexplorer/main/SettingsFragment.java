package com.harrysoft.burstcoinexplorer.main;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.widget.Toast;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.util.VersionUtils;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getContext() == null) {
            return;
        }

        String versionString = VersionUtils.getVersionName(getContext());
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        final Preference burstWallet = findPreference(getString(R.string.burst_wallet));
        burstWallet.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=org.icewave.burstcoinwallet"));
            startActivity(intent);
            return false;
        });

        final Preference burstTelegram = findPreference(getString(R.string.burst_telegram));
        burstTelegram.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.telegram.me/burstcoin")); // todo open telegram app directly
            startActivity(intent);
            return false;
        });

        final Preference burstWebsite = findPreference(getString(R.string.burst_website));
        burstWebsite.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://burst-coin.org"));
            startActivity(intent);
            return false;
        });

        final Preference burstPoCCExplorer = findPreference(getString(R.string.burst_pocc_explorer));
        burstPoCCExplorer.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://explore.burst.cryptoguru.org/"));
            startActivity(intent);
            return false;
        });

        final Preference appGithub = findPreference(getString(R.string.app_github));
        appGithub.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/harry1453/burstcoin-explorer-android/"));
            startActivity(intent);
            return false;
        });

        final Preference appDonate = findPreference(getString(R.string.app_donate));
        appDonate.setOnPreferenceClickListener(preference -> {
            ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(getString(R.string.app_donate), getString(R.string.donate_address));
            if (clipboard != null) {
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), R.string.app_donate_copied, Toast.LENGTH_LONG).show();
            }
            return false;
        });

        final Preference appVersion = findPreference(getString(R.string.app_version));
        appVersion.setSummary(versionString);
    }
}
