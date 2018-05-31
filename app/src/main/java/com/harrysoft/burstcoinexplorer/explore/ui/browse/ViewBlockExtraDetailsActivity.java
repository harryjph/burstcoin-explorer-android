package com.harrysoft.burstcoinexplorer.explore.ui.browse;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.service.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.burst.entity.BlockExtra;
import com.harrysoft.burstcoinexplorer.explore.entity.TransactionDisplayType;
import com.harrysoft.burstcoinexplorer.explore.viewmodel.browse.ViewTransactionsViewModelFactory;

import java.math.BigInteger;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ViewBlockExtraDetailsActivity extends ViewTransactionsActivity {

    @Inject
    BurstBlockchainService burstBlockchainService;
    @Inject
    ViewTransactionsViewModelFactory viewTransactionsViewModelFactory;

    private TextView blockNumberText, blockRewardText, transactionsLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_block_extra_details);

        blockNumberText = findViewById(R.id.view_block_extra_details_block_number_value);
        blockRewardText = findViewById(R.id.view_block_extra_details_block_reward_value);
        transactionsLabel = findViewById(R.id.view_block_extra_details_transactions_label);

        BigInteger blockID;

        try {
            blockID = new BigInteger(getIntent().getStringExtra(getString(R.string.extra_block_id)));
        } catch (NullPointerException | NumberFormatException e) {
            Toast.makeText(this, R.string.loading_error, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        burstBlockchainService.fetchBlockExtra(blockID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onBlock, t -> onError());
    }

    private void onBlock(BlockExtra blockExtra) {
        transactionsLabel.setText(R.string.transactions);
        blockNumberText.setText(String.format(Locale.getDefault(), "%d", blockExtra.blockNumber));
        blockRewardText.setText(blockExtra.blockReward.toString());

        if (blockExtra.transactionIDs.size() == 0) {
            transactionsLabel.setText(R.string.transactions_empty);
        } else {
            setupViewTransactionsActivity(findViewById(R.id.view_block_extra_details_transactions_list), viewTransactionsViewModelFactory, TransactionDisplayType.FROM, blockExtra.transactionIDs);
        }
    }

    protected void onError() {
        transactionsLabel.setText(R.string.transactions_error);
        blockNumberText.setText(R.string.loading_error);
        blockRewardText.setText(R.string.loading_error);
    }
}
