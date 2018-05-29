package com.harrysoft.burstcoinexplorer.explore.browse;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.api.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.burst.entity.AccountTransactions;
import com.harrysoft.burstcoinexplorer.burst.entity.BurstAddress;
import com.harrysoft.burstcoinexplorer.burst.explorer.AndroidBurstExplorer;
import com.harrysoft.burstcoinexplorer.burst.explorer.BurstExplorer;

import java.math.BigInteger;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ViewAccountTransactionsActivity extends ViewTransactionsActivity {

    private TextView transactionsLabel;

    @Inject
    BurstBlockchainService burstBlockchainService;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // to/do onSaveInstanceState to avoid re-fetching the account transactions EDIT: This will take too much effort for now.
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account_transactions);
        BurstExplorer burstExplorer = new AndroidBurstExplorer(this);
        setupViewTransactionsActivity(TransactionDisplayType.TO, burstBlockchainService, burstExplorer);

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
        setTransactionsList(findViewById(R.id.view_account_transactions_list));

        addressText.setText(account.getFullAddress());

        getBurstBlockchainService().fetchAccountTransactions(account.getNumericID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onAccountTransactions, this::onError);
    }

    private void onAccountTransactions(AccountTransactions accountTransactions) {
        transactionsLabel.setText(R.string.transactions);
        onTransactionIDs(accountTransactions.transactions);
    }

    @Override
    protected void onError(Throwable throwable) {
        transactionsLabel.setText(R.string.transactions_error);
    }

    @Override
    protected void onNoTransactions() {
        transactionsLabel.setText(R.string.transactions_empty);
    }
}
