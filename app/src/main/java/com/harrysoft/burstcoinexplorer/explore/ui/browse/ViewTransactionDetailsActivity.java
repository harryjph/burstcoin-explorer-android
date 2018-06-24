package com.harrysoft.burstcoinexplorer.explore.ui.browse;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.harry1453.burst.BurstUtils;
import com.harry1453.burst.explorer.entity.Transaction;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.util.BurstFormatUtils;
import com.harrysoft.burstcoinexplorer.burst.util.TransactionTypeUtils;
import com.harrysoft.burstcoinexplorer.explore.viewmodel.browse.ViewTransactionDetailsViewModel;
import com.harrysoft.burstcoinexplorer.explore.viewmodel.browse.ViewTransactionDetailsViewModelFactory;
import com.harrysoft.burstcoinexplorer.main.repository.ClipboardRepository;
import com.harrysoft.burstcoinexplorer.router.ExplorerRouter;
import com.harrysoft.burstcoinexplorer.util.TextViewUtils;

import java.math.BigInteger;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class ViewTransactionDetailsActivity extends ViewDetailsActivity {

    @Inject
    ClipboardRepository clipboardRepository;
    @Inject
    ViewTransactionDetailsViewModelFactory viewTransactionDetailsViewModelFactory;

    private TextView transactionIDText, senderText, recipientText, amountText, typeText, subTypeText, feeText, timestampText, blockIDText, confirmationsText, fullHashText, signatureText, signatureHashText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction_details);

        // Check for Transaction ID
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(getString(R.string.extra_transaction_id))) {
            viewTransactionDetailsViewModelFactory.setTransactionID(new BigInteger(getIntent().getExtras().getString(getString(R.string.extra_transaction_id))));
        }

        if (!viewTransactionDetailsViewModelFactory.canCreate()) {
            Toast.makeText(this, R.string.loading_error, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        ViewTransactionDetailsViewModel viewTransactionDetailsViewModel = ViewModelProviders.of(this, viewTransactionDetailsViewModelFactory).get(ViewTransactionDetailsViewModel.class);

        transactionIDText = findViewById(R.id.view_transaction_details_transaction_id_value);
        senderText = findViewById(R.id.view_transaction_details_sender_value);
        recipientText = findViewById(R.id.view_transaction_details_recipient_value);
        amountText = findViewById(R.id.view_transaction_details_amount_value);
        typeText = findViewById(R.id.view_transaction_details_type_value);
        subTypeText = findViewById(R.id.view_transaction_details_subtype_value);
        feeText = findViewById(R.id.view_transaction_details_fee_value);
        timestampText = findViewById(R.id.view_transaction_details_timestamp_value);
        blockIDText = findViewById(R.id.view_transaction_details_block_id_value);
        confirmationsText = findViewById(R.id.view_transaction_details_confirmations_value);
        fullHashText = findViewById(R.id.view_transaction_details_full_hash_value);
        signatureText = findViewById(R.id.view_transaction_details_signature_value);
        signatureHashText = findViewById(R.id.view_transaction_details_signature_hash_value);

        viewTransactionDetailsViewModel.getTransaction().observe(this, this::onTransaction);
    }

    private void onTransaction(@Nullable Transaction transaction) {
        if (transaction != null) {
            transactionIDText.setText(String.format(Locale.getDefault(), "%d", transaction.transactionID));
            senderText.setText(BurstFormatUtils.burstAddress(this, transaction.sender));
            recipientText.setText(BurstFormatUtils.burstAddress(this, transaction.recipient));
            amountText.setText(transaction.amount.toString());
            typeText.setText(TransactionTypeUtils.getTransactionTypes().get(transaction.type.byteValue()));
            subTypeText.setText(TransactionTypeUtils.getTransactionSubTypes().get(transaction.type.byteValue()).get(transaction.subType.byteValue()));
            feeText.setText(transaction.fee.toString());
            timestampText.setText(BurstUtils.formatBurstTimestamp(transaction.timestamp));
            blockIDText.setText(String.format(Locale.getDefault(), "%d", transaction.blockID));
            confirmationsText.setText(String.format(Locale.getDefault(), "%d", transaction.confirmations));
            fullHashText.setText(transaction.fullHash);
            signatureText.setText(transaction.signature);
            signatureHashText.setText(transaction.signatureHash);
            configureViews(transaction);
        } else {
            onError();
        }
    }

    private void configureViews(@NonNull Transaction transaction) {
        if (!TextUtils.isEmpty(transaction.sender.getFullAddress())) {
            TextViewUtils.setupTextViewAsHyperlink(senderText, (view) -> ExplorerRouter.viewAccountDetails(this, transaction.sender.getNumericID()));
            TextViewUtils.setupTextViewAsCopyable(clipboardRepository, senderText, transaction.sender.getFullAddress());
        }

        if (!TextUtils.isEmpty(transaction.recipient.getFullAddress())) {
            TextViewUtils.setupTextViewAsHyperlink(recipientText, (view) -> ExplorerRouter.viewAccountDetails(this, transaction.recipient.getNumericID()));
            TextViewUtils.setupTextViewAsCopyable(clipboardRepository, recipientText, transaction.recipient.getFullAddress());
        }

        TextViewUtils.setupTextViewAsHyperlink(blockIDText, (view) -> ExplorerRouter.viewBlockDetailsByID(this, transaction.blockID));

        TextViewUtils.setupTextViewAsCopyable(clipboardRepository, transactionIDText, transaction.transactionID.toString());
        TextViewUtils.setupTextViewAsCopyable(clipboardRepository, blockIDText, transaction.blockID.toString());
        TextViewUtils.setupTextViewAsCopyable(clipboardRepository, fullHashText, transaction.fullHash);
        TextViewUtils.setupTextViewAsCopyable(clipboardRepository, signatureText, transaction.signature);
        TextViewUtils.setupTextViewAsCopyable(clipboardRepository, signatureHashText, transaction.signatureHash);
    }

    private void onError() {
        transactionIDText.setText(R.string.loading_error);
        senderText.setText(R.string.loading_error);
        recipientText.setText(R.string.loading_error);
        amountText.setText(R.string.loading_error);
        typeText.setText(R.string.loading_error);
        subTypeText.setText(R.string.loading_error);
        feeText.setText(R.string.loading_error);
        timestampText.setText(R.string.loading_error);
        blockIDText.setText(R.string.loading_error);
        confirmationsText.setText(R.string.loading_error);
        fullHashText.setText(R.string.loading_error);
        signatureText.setText(R.string.loading_error);
        signatureHashText.setText(R.string.loading_error);
    }
}
