package com.harrysoft.burstcoinexplorer.explore.ui.browse;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.accounts.db.SavedAccount;
import com.harrysoft.burstcoinexplorer.burst.entity.Account;
import com.harrysoft.burstcoinexplorer.burst.util.BurstUtils;
import com.harrysoft.burstcoinexplorer.explore.viewmodel.browse.ViewAccountDetailsViewModel;
import com.harrysoft.burstcoinexplorer.explore.viewmodel.browse.ViewAccountDetailsViewModelFactory;
import com.harrysoft.burstcoinexplorer.main.repository.ClipboardRepository;
import com.harrysoft.burstcoinexplorer.router.ExplorerRouter;
import com.harrysoft.burstcoinexplorer.util.TextViewUtils;

import java.math.BigInteger;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class ViewAccountDetailsActivity extends ViewDetailsActivity {

    @Inject
    ClipboardRepository clipboardRepository;
    @Inject
    ViewAccountDetailsViewModelFactory viewAccountDetailsViewModelFactory;
    private ViewAccountDetailsViewModel viewAccountDetailsViewModel;

    private TextView addressText, publicKeyText, nameText, balanceText, sentAmountText, receivedAmountText, feesText, soloMinedBalanceText, poolMinedBalanceText, rewardRecipientText;
    private Button saveAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account_details);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(getString(R.string.extra_account_id))) {
            viewAccountDetailsViewModelFactory.setAccountID(new BigInteger(getIntent().getExtras().getString(getString(R.string.extra_account_id))));
        } else {
            Toast.makeText(this, R.string.loading_error, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        viewAccountDetailsViewModel = ViewModelProviders.of(this, viewAccountDetailsViewModelFactory).get(ViewAccountDetailsViewModel.class);

        addressText = findViewById(R.id.view_account_details_address_value);
        publicKeyText = findViewById(R.id.view_account_details_public_key_value);
        nameText = findViewById(R.id.view_account_details_name_value);
        balanceText = findViewById(R.id.view_account_details_balance_value);
        sentAmountText = findViewById(R.id.view_account_details_sent_amount_value);
        receivedAmountText = findViewById(R.id.view_account_details_received_amount_value);
        feesText = findViewById(R.id.view_account_details_total_fees_value);
        soloMinedBalanceText = findViewById(R.id.view_account_details_solo_mined_balance_value);
        poolMinedBalanceText = findViewById(R.id.view_account_details_pool_mined_balance_value);
        rewardRecipientText = findViewById(R.id.view_account_details_reward_recipient_value);
        Button viewExtraButton = findViewById(R.id.view_account_details_view_transactions);
        saveAccountButton = findViewById(R.id.view_account_details_save_account);

        viewExtraButton.setOnClickListener(view -> viewAccountDetailsViewModel.viewExtra(this));

        viewAccountDetailsViewModel.getAccount().observe(this, this::onAccount);
        viewAccountDetailsViewModel.getSavedAccount().observe(this, this::onSavedAccount);
        viewAccountDetailsViewModel.getSaveButtonVisibility().observe(this, saveAccountButton::setVisibility);
    }

    private void onAccount(@Nullable Account account) {
        if (account != null) {
            addressText.setText(account.address.getFullAddress());
            publicKeyText.setText(account.publicKey);
            nameText.setText(BurstUtils.burstName(this, account.name));
            balanceText.setText(account.balance.toString());
            sentAmountText.setText(getString(R.string.transaction_count_display_format, account.totalSent.toString(), String.format(Locale.getDefault(), "%d", account.totalSentN)));
            receivedAmountText.setText(getString(R.string.transaction_count_display_format, account.totalReceived.toString(), String.format(Locale.getDefault(), "%d", account.totalReceivedN)));
            feesText.setText(account.totalFees.toString());
            soloMinedBalanceText.setText(getString(R.string.blocks_mined_count_display_format, account.soloMinedBalance.toString(), String.format(Locale.getDefault(), "%d", account.soloMinedBlocks)));
            poolMinedBalanceText.setText(getString(R.string.blocks_mined_count_display_format, account.poolMinedBalance.toString(), String.format(Locale.getDefault(), "%d", account.poolMinedBlocks)));
            if (!TextUtils.isEmpty(account.rewardRecipient.getFullAddress())) {
                rewardRecipientText.setText(getString(R.string.address_display_format, account.rewardRecipient.getFullAddress(), BurstUtils.burstName(this, account.rewardRecipientName)));
            } else {
                rewardRecipientText.setText(R.string.not_set);
            }
            configureViews(account);
        } else {
            onError();
        }
    }

    private void onSavedAccount(LiveData<SavedAccount> savedAccountData) {
        savedAccountData.observe(this, savedAccount -> {
            boolean accountSaved = savedAccount != null;
            if (accountSaved) {
                saveAccountButton.setText(R.string.unsave_account);
                saveAccountButton.setOnClickListener(viewAccountDetailsViewModel.getDeleteOnClickListener());
            } else {
                saveAccountButton.setText(R.string.save_account);
                saveAccountButton.setOnClickListener(viewAccountDetailsViewModel.getSaveOnClickListener());
            }
        });
    }

    private void configureViews(Account account) {
        if (!account.address.getRawAddress().equals(account.rewardRecipient.getRawAddress()) && !TextUtils.isEmpty(account.rewardRecipient.getFullAddress())) { // if sender != recipient && recipient is set
            TextViewUtils.setupTextViewAsHyperlink(rewardRecipientText, (view) -> ExplorerRouter.viewAccountDetails(this, account.rewardRecipient.getNumericID()));
            TextViewUtils.setupTextViewAsCopyable(clipboardRepository, rewardRecipientText, account.rewardRecipient.getFullAddress());
        }

        TextViewUtils.setupTextViewAsCopyable(clipboardRepository, addressText, account.address.getFullAddress());
        TextViewUtils.setupTextViewAsCopyable(clipboardRepository, publicKeyText, account.publicKey);
    }

    private void onError() {
        addressText.setText(R.string.loading_error);
        publicKeyText.setText(R.string.loading_error);
        nameText.setText(R.string.loading_error);
        balanceText.setText(R.string.loading_error);
        sentAmountText.setText(R.string.loading_error);
        receivedAmountText.setText(R.string.loading_error);
        feesText.setText(R.string.loading_error);
        soloMinedBalanceText.setText(R.string.loading_error);
        poolMinedBalanceText.setText(R.string.loading_error);
        rewardRecipientText.setText(R.string.loading_error);
    }
}
