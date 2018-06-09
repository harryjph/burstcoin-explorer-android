package com.harrysoft.burstcoinexplorer.main.repository;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.harrysoft.burstcoinexplorer.R;

public class AndroidClipboardRepository implements ClipboardRepository {

    private final Context context;
    @Nullable
    private final ClipboardManager clipboardManager;

    public AndroidClipboardRepository(Context context) {
        this.context = context;
        clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    public void copyToClipboard(String label, String text) {
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(ClipData.newPlainText(label, text));
            Toast.makeText(context, R.string.copied_to_clipboard, Toast.LENGTH_LONG).show();
        }
    }
}
