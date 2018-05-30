package com.harrysoft.burstcoinexplorer.explore.browse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.api.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.burst.entity.Block;
import com.harrysoft.burstcoinexplorer.burst.utils.BurstUtils;
import com.harrysoft.burstcoinexplorer.router.ExplorerRouter;
import com.harrysoft.burstcoinexplorer.util.FileSizeUtils;
import com.harrysoft.burstcoinexplorer.util.TextViewUtils;

import java.math.BigInteger;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ViewBlockDetailsActivity extends ViewDetailsActivity {

    @Inject
    BurstBlockchainService burstBlockchainService;

    private TextView blockNumberText, blockIDText, timestampText, txCountText, totalText, sizeText, generatorText, rewardRecipientText, feeText;

    private BigInteger displayedBlockID;

    @Nullable
    private Block displayedBlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_block_details);

        BigInteger blockNumber = null;
        BigInteger blockID = null;
        Block block = null;

        try {
            blockNumber = new BigInteger(getIntent().getStringExtra(getString(R.string.extra_block_number)));
        } catch (NullPointerException | NumberFormatException ignored) {}

        if (savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.extra_block_id))) {
            blockID = new BigInteger(savedInstanceState.getString(getString(R.string.extra_block_id)));
        } else {
            try {
                blockID = new BigInteger(getIntent().getStringExtra(getString(R.string.extra_block_id)));
            } catch (Exception e) {
                // ignore
            }
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.extra_block_parcel))) {
            block = savedInstanceState.getParcelable(getString(R.string.extra_block_parcel));
        } else {
            try {
                block = getIntent().getParcelableExtra(getString(R.string.extra_block_parcel));
            } catch (Exception e) {
                // ignore
            }
        }

        blockNumberText = findViewById(R.id.view_block_details_block_number_value);
        blockIDText = findViewById(R.id.view_block_details_block_id_value);
        timestampText = findViewById(R.id.view_block_details_timestamp_value);
        txCountText = findViewById(R.id.view_block_details_transaction_count_value);
        totalText = findViewById(R.id.view_block_details_total_value);
        sizeText = findViewById(R.id.view_block_details_size_value);
        generatorText = findViewById(R.id.view_block_details_generator_value);
        rewardRecipientText = findViewById(R.id.view_block_details_reward_recipient_value);
        feeText = findViewById(R.id.view_block_details_fee_value);
        Button viewExtraButton = findViewById(R.id.view_block_details_view_extra);

        viewExtraButton.setOnClickListener(view -> {
            if (displayedBlockID != null) {
                ExplorerRouter.viewBlockExtraDetails(this, displayedBlockID);
            }
        });

        if (block != null) {
            onBlock(block);
        }

        else if (blockID != null) {
            displayedBlockID = blockID; // To enable the button
            burstBlockchainService.fetchBlockByID(blockID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onBlock, this::onError);
        }

        else if (blockNumber != null) {
            burstBlockchainService.fetchBlockByHeight(blockNumber)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onBlock, this::onError);
        }

        else { // Nothing could be found to display. Show error
            Toast.makeText(this, "Unknown block", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (displayedBlock != null) {
            outState.putParcelable(getString(R.string.extra_block_parcel), displayedBlock);
        }
        if (displayedBlockID != null) {
            outState.putString(getString(R.string.extra_block_id), displayedBlockID.toString());
        }
    }

    private void onBlock(Block block) {
        displayedBlockID = block.blockID;
        displayedBlock = block;
        blockNumberText.setText(String.format(Locale.getDefault(), "%d", block.blockNumber));
        blockIDText.setText(String.format(Locale.getDefault(), "%d", block.blockID));
        timestampText.setText(block.timestamp); // todo figure out how to localise this
        txCountText.setText(String.format(Locale.getDefault(), "%d", block.transactionCount));
        totalText.setText(block.total.toString());
        sizeText.setText(FileSizeUtils.formatFileSize(block.size));
        generatorText.setText(getString(R.string.address_display_format, block.generator.getFullAddress(), BurstUtils.burstName(this, block.generatorName)));
        rewardRecipientText.setText(getString(R.string.address_display_format, block.rewardRecipient.getFullAddress(), BurstUtils.burstName(this, block.rewardRecipientName)));
        feeText.setText(block.fee.toString());
        updateLinks();
    }

    private void updateLinks() {
        if (displayedBlock != null) {
            if (displayedBlock.generator != null) {
                TextViewUtils.setupTextViewAsHyperlink(generatorText, (view) -> ExplorerRouter.viewAccountDetails(this, displayedBlock.generator.getNumericID()));
            }

            if (displayedBlock.rewardRecipient != null) {
                TextViewUtils.setupTextViewAsHyperlink(rewardRecipientText, (view) -> ExplorerRouter.viewAccountDetails(this, displayedBlock.rewardRecipient.getNumericID()));
            }
        }
    }

    private void onError(Throwable throwable) {
        blockNumberText.setText(R.string.loading_error);
        blockIDText.setText(R.string.loading_error);
        timestampText.setText(R.string.loading_error);
        txCountText.setText(R.string.loading_error);
        totalText.setText(R.string.loading_error);
        sizeText.setText(R.string.loading_error);
        generatorText.setText(R.string.loading_error);
        rewardRecipientText.setText(R.string.loading_error);
        feeText.setText(R.string.loading_error);
    }
}
