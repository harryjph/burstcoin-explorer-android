package com.harrysoft.burstcoinexplorer.explore.ui.browse;

import android.arch.lifecycle.ViewModelProviders;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.explore.entity.TransactionDisplayType;
import com.harrysoft.burstcoinexplorer.explore.viewmodel.browse.ViewAccountTransactionsViewModel;
import com.harrysoft.burstcoinexplorer.explore.viewmodel.browse.ViewAccountTransactionsViewModelFactory;
import com.harrysoft.burstcoinexplorer.explore.viewmodel.browse.ViewTransactionsViewModelFactory;
import com.harrysoft.burstcoinexplorer.main.repository.ClipboardRepository;
import com.harrysoft.burstcoinexplorer.util.TextViewUtils;

import javax.inject.Inject;

import burst.kit.entity.BurstAddress;
import dagger.android.AndroidInjection;

public class ViewAccountTransactionsActivity extends ViewTransactionsActivity {

    @Inject
    ClipboardRepository clipboardRepository;
    @Inject
    ViewTransactionsViewModelFactory viewTransactionsViewModelFactory;
    @Inject
    ViewAccountTransactionsViewModelFactory viewAccountTransactionsViewModelFactory;

    private ViewAccountTransactionsViewModel viewAccountTransactionsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account_transactions);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(getString(R.string.extra_account_id))) {
            viewAccountTransactionsViewModelFactory.setAccount(BurstAddress.fromId(getIntent().getStringExtra(getString(R.string.extra_account_id))));
        } else {
            Toast.makeText(this, R.string.loading_error, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        viewAccountTransactionsViewModel = ViewModelProviders.of(this, viewAccountTransactionsViewModelFactory).get(ViewAccountTransactionsViewModel.class);

        TextView addressText = findViewById(R.id.view_account_transactions_address_value);
        TextView transactionsLabel = findViewById(R.id.view_account_transactions_label);

        viewAccountTransactionsViewModel.getTransactionIDs().observe(this, transactionIDs -> setupViewTransactionsActivity(findViewById(R.id.view_account_transactions_list), viewTransactionsViewModelFactory, TransactionDisplayType.TO, transactionIDs));
        viewAccountTransactionsViewModel.getTransactionsLabel().observe(this, transactionsLabel::setText);
        viewAccountTransactionsViewModel.getAddress().observe(this, address -> { addressText.setText(address); TextViewUtils.setupTextViewAsCopyable(clipboardRepository, addressText, address); });

        if (NfcAdapter.getDefaultAdapter(this) != null) {
            NfcAdapter.getDefaultAdapter(this).setNdefPushMessageCallback(viewAccountTransactionsViewModel, this);
        }
    }

    @Override
    protected void setTransactionsLabelText(int text) {
        viewAccountTransactionsViewModel.setTransactionsLabel(text);
    }
}
