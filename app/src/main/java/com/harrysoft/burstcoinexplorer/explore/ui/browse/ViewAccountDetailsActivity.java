package com.harrysoft.burstcoinexplorer.explore.ui.browse;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.harry1453.burst.explorer.entity.Account;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.accounts.db.SavedAccount;
import com.harrysoft.burstcoinexplorer.explore.viewmodel.browse.ViewAccountDetailsViewModel;
import com.harrysoft.burstcoinexplorer.explore.viewmodel.browse.ViewAccountDetailsViewModelFactory;
import com.harrysoft.burstcoinexplorer.main.repository.ClipboardRepository;
import com.harrysoft.burstcoinexplorer.main.router.ExplorerRouter;
import com.harrysoft.burstcoinexplorer.util.TextFormatUtils;
import com.harrysoft.burstcoinexplorer.util.TextViewUtils;

import java.math.BigInteger;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class ViewAccountDetailsActivity extends ViewDetailsActivity {

    @Inject
    ClipboardRepository clipboardRepository;
    @Inject
    ViewAccountDetailsViewModelFactory viewAccountDetailsViewModelFactory;
    private ViewAccountDetailsViewModel viewAccountDetailsViewModel;

    private TextView addressText, publicKeyText, nameText, descriptionText, balanceText, forgedBalanceText, rewardRecipientText;
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
        descriptionText = findViewById(R.id.view_account_details_description_value);
        balanceText = findViewById(R.id.view_account_details_balance_value);
        forgedBalanceText = findViewById(R.id.view_account_details_forged_balance_value);
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
            nameText.setText(TextFormatUtils.checkIfSet(this, account.name));
            descriptionText.setText(TextFormatUtils.checkIfSet(this, account.description));
            balanceText.setText(account.balance.toString());
            forgedBalanceText.setText(account.forgedBalance.toString());
            if (!TextUtils.isEmpty(account.rewardRecipient.getFullAddress())) {
                rewardRecipientText.setText(getString(R.string.address_display_format, account.rewardRecipient.getFullAddress(), TextFormatUtils.checkIfSet(this, account.rewardRecipientName)));
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
        descriptionText.setText(R.string.loading_error);
        balanceText.setText(R.string.loading_error);
        forgedBalanceText.setText(R.string.loading_error);
        rewardRecipientText.setText(R.string.loading_error);
    }
}
