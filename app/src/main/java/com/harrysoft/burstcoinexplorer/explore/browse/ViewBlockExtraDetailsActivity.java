package com.harrysoft.burstcoinexplorer.explore.browse;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.TextView;

import com.harrysoft.burstcoinexplorer.burst.explorer.BurstExplorer;
import com.harrysoft.burstcoinexplorer.burst.explorer.AndroidBurstExplorer;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.api.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.burst.entity.BlockExtra;

import java.math.BigInteger;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ViewBlockExtraDetailsActivity extends ViewTransactionsActivity implements AdapterView.OnItemClickListener {

    @Inject
    BurstBlockchainService burstBlockchainService;
    BurstExplorer burstExplorer;

    private TextView blockNumberText, blockRewardText, transactionsLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // to/do onSaveInstanceState to avoid re-fetching the block extra details EDIT: This will take too much effort for now.
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_block_extra_details);
        burstExplorer = new AndroidBurstExplorer(this);
        setupViewTransactionsActivity(TransactionDisplayType.FROM, burstBlockchainService, burstExplorer);

        BigInteger blockID = new BigInteger(getIntent().getStringExtra(getString(R.string.extra_block_id)));

        blockNumberText = findViewById(R.id.view_block_extra_details_block_number_value);
        blockRewardText = findViewById(R.id.view_block_extra_details_block_reward_value);
        transactionsLabel = findViewById(R.id.view_block_extra_details_transactions_label);
        setTransactionsList(findViewById(R.id.view_block_extra_details_transactions_list));

        getBurstBlockchainService().fetchBlockExtra(blockID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onBlock, this::onError);
    }

    private void onBlock(BlockExtra block) {
        transactionsLabel.setText(R.string.transactions);
        onTransactionIDs(block.transactionIDs);
        blockNumberText.setText(String.format(Locale.getDefault(), "%d", block.blockNumber));
        blockRewardText.setText(block.blockReward.toString());
    }

    @Override
    protected void onError(Throwable throwable) {
        transactionsLabel.setText(R.string.transactions_error);
        blockNumberText.setText(R.string.loading_error);
        blockRewardText.setText(R.string.loading_error);
        throwable.printStackTrace();
    }

    @Override
    protected void onNoTransactions() {
        transactionsLabel.setText(R.string.transactions_empty);
    }
}
