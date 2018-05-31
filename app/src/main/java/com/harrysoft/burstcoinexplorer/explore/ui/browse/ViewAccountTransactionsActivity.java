package com.harrysoft.burstcoinexplorer.explore.ui.browse;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.service.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.burst.entity.AccountTransactions;
import com.harrysoft.burstcoinexplorer.burst.entity.BurstAddress;
import com.harrysoft.burstcoinexplorer.explore.entity.TransactionDisplayType;
import com.harrysoft.burstcoinexplorer.explore.viewmodel.browse.ViewTransactionsViewModelFactory;

import java.math.BigInteger;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ViewAccountTransactionsActivity extends ViewTransactionsActivity {

    private TextView transactionsLabel;

    @Inject
    BurstBlockchainService burstBlockchainService;
    @Inject
    ViewTransactionsViewModelFactory viewTransactionsViewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account_transactions);

        BurstAddress account;

        try {
            account = new BurstAddress(new BigInteger(getIntent().getStringExtra(getString(R.string.extra_account_id))));
        } catch (NullPointerException | NumberFormatException e) {
            Toast.makeText(this, R.string.loading_error, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        TextView addressText = findViewById(R.id.view_account_transactions_address_value);
        transactionsLabel = findViewById(R.id.view_account_transactions_label);

        addressText.setText(account.getFullAddress());

        burstBlockchainService.fetchAccountTransactions(account.getNumericID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onAccountTransactions, t -> onError());
    }

    private void onAccountTransactions(AccountTransactions accountTransactions) {
        transactionsLabel.setText(R.string.transactions);
        if (accountTransactions.transactions.size() == 0) {
            transactionsLabel.setText(R.string.transactions_empty);
        } else {
            setupViewTransactionsActivity(findViewById(R.id.view_account_transactions_list), viewTransactionsViewModelFactory, TransactionDisplayType.TO, accountTransactions.transactions);
        }
    }

    protected void onError() {
        transactionsLabel.setText(R.string.transactions_error);
    }
}
