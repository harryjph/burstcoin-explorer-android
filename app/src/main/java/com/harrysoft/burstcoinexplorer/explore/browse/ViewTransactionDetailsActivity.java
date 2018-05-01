package com.harrysoft.burstcoinexplorer.explore.browse;

import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;

import com.harrysoft.burstcoinexplorer.BurstExplorer;
import com.harrysoft.burstcoinexplorer.HSBurstExplorer;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.api.BurstAPIService;
import com.harrysoft.burstcoinexplorer.burst.api.PoccAPIService;
import com.harrysoft.burstcoinexplorer.burst.entity.BurstAddress;
import com.harrysoft.burstcoinexplorer.burst.entity.Transaction;
import com.harrysoft.burstcoinexplorer.util.TextViewUtils;

import java.math.BigInteger;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ViewTransactionDetailsActivity extends ViewDetailsActivity {

    private BurstExplorer burstExplorer;

    private Transaction transaction;

    private TextView transactionIDText, senderText, recipientText, amountText, typeText, feeText, timestampText, blockIDText, confirmationsText, fullHashText, signatureText, signatureHashText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction_details);

        transactionIDText = findViewById(R.id.view_transaction_details_transaction_id_value);
        senderText = findViewById(R.id.view_transaction_details_sender_value);
        recipientText = findViewById(R.id.view_transaction_details_recipient_value);
        amountText = findViewById(R.id.view_transaction_details_amount_value);
        typeText = findViewById(R.id.view_transaction_details_type_value);
        feeText = findViewById(R.id.view_transaction_details_fee_value);
        timestampText = findViewById(R.id.view_transaction_details_timestamp_value);
        blockIDText = findViewById(R.id.view_transaction_details_block_id_value);
        confirmationsText = findViewById(R.id.view_transaction_details_confirmations_value);
        fullHashText = findViewById(R.id.view_transaction_details_full_hash_value);
        signatureText = findViewById(R.id.view_transaction_details_signature_value);
        signatureHashText = findViewById(R.id.view_transaction_details_signature_hash_value);

        BigInteger transactionID = null;
        Transaction transaction = null;

        try {
            transactionID = new BigInteger(getIntent().getStringExtra(getString(R.string.extra_transaction_id)));
        } catch (Exception e) {
            // ignored
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.extra_transaction_parcel))) {
            transaction = savedInstanceState.getParcelable(getString(R.string.extra_transaction_parcel));
        } else {
            try {
                transaction = getIntent().getParcelableExtra(getString(R.string.extra_transaction_parcel));
            } catch (Exception e) {
                // ignored
            }
        }

        BurstAPIService burstAPIService = new PoccAPIService(this);
        burstExplorer = new HSBurstExplorer(this);

        if (transaction != null) {
            onTransaction(transaction);
        }

        else if (transactionID != null) {
            burstAPIService.fetchTransaction(transactionID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onTransaction, this::onError);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (transaction != null) {
            outState.putParcelable(getString(R.string.extra_transaction_parcel), transaction);
        }
    }

    private void onTransaction(Transaction transaction) {
        this.transaction = transaction;
        transactionIDText.setText(String.format(Locale.getDefault(), "%d", transaction.transactionID));
        senderText.setText(transaction.sender.getFullAddress());
        recipientText.setText(transaction.recipient.getFullAddress());
        amountText.setText(transaction.amount.toString());
        typeText.setText(String.format(Locale.getDefault(), "%d", transaction.type));
        feeText.setText(transaction.fee.toString());
        timestampText.setText(transaction.timestamp);
        blockIDText.setText(String.format(Locale.getDefault(), "%d", transaction.blockID));
        confirmationsText.setText(String.format(Locale.getDefault(), "%d", transaction.confirmations));
        fullHashText.setText(transaction.fullHash);
        signatureText.setText(transaction.signature);
        signatureHashText.setText(transaction.signatureHash);
        updateLinks();
    }

    private void updateLinks() {
        TextViewUtils.makeTextViewHyperlink(senderText);
        TextViewUtils.makeTextViewHyperlink(recipientText);
        TextViewUtils.makeTextViewHyperlink(blockIDText);

        senderText.setOnClickListener((view) -> {
            if (transaction != null) {
                burstExplorer.viewAccountDetails(transaction.sender.getNumericID());
            }
        });

        recipientText.setOnClickListener((view) -> {
            if (transaction != null) {
                burstExplorer.viewAccountDetails(transaction.recipient.getNumericID());
            }
        });

        blockIDText.setOnClickListener((view) -> {
            if (transaction != null) {
                burstExplorer.viewBlockDetailsByID(transaction.blockID);
            }
        });
    }

    private void onError(Throwable throwable) {
        transactionIDText.setText(R.string.loading_error);
        senderText.setText(R.string.loading_error);
        recipientText.setText(R.string.loading_error);
        amountText.setText(R.string.loading_error);
        typeText.setText(R.string.loading_error);
        feeText.setText(R.string.loading_error);
        timestampText.setText(R.string.loading_error);
        blockIDText.setText(R.string.loading_error);
        confirmationsText.setText(R.string.loading_error);
        fullHashText.setText(R.string.loading_error);
        signatureText.setText(R.string.loading_error);
        signatureHashText.setText(R.string.loading_error);
        throwable.printStackTrace();
    }
}
