package com.harrysoft.burstcoinexplorer.explore.browse;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.harrysoft.burstcoinexplorer.BurstExplorer;
import com.harrysoft.burstcoinexplorer.HSBurstExplorer;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.BurstUtils;
import com.harrysoft.burstcoinexplorer.burst.api.BurstAPIService;
import com.harrysoft.burstcoinexplorer.burst.api.PoccAPIService;
import com.harrysoft.burstcoinexplorer.burst.entity.Account;
import com.harrysoft.burstcoinexplorer.burst.entity.BurstAddress;
import com.harrysoft.burstcoinexplorer.util.TextViewUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ViewAccountDetailsActivity extends ViewDetailsActivity {

    private BurstExplorer burstExplorer;

    private BigInteger accountID;
    private Account account;

    private TextView addressText, publicKeyText, nameText, balanceText, sentAmountText, receivedAmountText, feesText, soloMinedBalanceText, poolMinedBalanceText, rewardRecipientText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account_details);

        accountID = new BigInteger(getIntent().getStringExtra(getString(R.string.extra_account_id)));

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

        viewExtraButton.setOnClickListener(view -> {
            if (account != null) {
                burstExplorer.viewAccountTransactions(account.address.getNumericID());
            } else if (accountID != null) {
                burstExplorer.viewAccountTransactions(accountID);
            }
        });

        BurstAPIService burstAPIService = new PoccAPIService(this);
        burstExplorer = new HSBurstExplorer(this);
        if (savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.extra_account_parcel))) {
            onAccount(savedInstanceState.getParcelable(getString(R.string.extra_account_parcel)));
        } else {
            burstAPIService.fetchAccount(accountID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onAccount, this::onError);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (account != null) {
            outState.putParcelable(getString(R.string.extra_account_parcel), account);
        }
    }

    private void onAccount(Account account) {
        this.account = account;
        this.accountID = account.address.getNumericID();
        addressText.setText(account.address.getFullAddress());
        publicKeyText.setText(account.publicKey);
        nameText.setText(BurstUtils.burstName(this, account.name));
        balanceText.setText(account.balance.toString());
        sentAmountText.setText(getString(R.string.transaction_count_display_format, account.totalSent.toString(), String.format(Locale.getDefault(), "%d", account.totalSentN)));
        receivedAmountText.setText(getString(R.string.transaction_count_display_format, account.totalReceived.toString(), String.format(Locale.getDefault(), "%d", account.totalReceivedN)));
        feesText.setText(account.totalFees.toString());
        soloMinedBalanceText.setText(getString(R.string.blocks_mined_count_display_format, account.soloMinedBalance.toString(), String.format(Locale.getDefault(), "%d", account.soloMinedBlocks)));
        poolMinedBalanceText.setText(getString(R.string.blocks_mined_count_display_format, account.poolMinedBalance.toString(), String.format(Locale.getDefault(), "%d", account.poolMinedBlocks)));
        rewardRecipientText.setText(getString(R.string.address_display_format, account.rewardRecipient.getFullAddress(), BurstUtils.burstName(this, account.rewardRecipientName)));
        updateLinks();
    }

    private void updateLinks() {
        if (account != null && !account.address.getRawAddress().equals(account.rewardRecipient.getRawAddress())) { // if sender != recipient
            TextViewUtils.makeTextViewHyperlink(rewardRecipientText);

            rewardRecipientText.setOnClickListener((view) -> {
                burstExplorer.viewAccountDetails(account.rewardRecipient.getNumericID());
            });
        }
    }

    private void onError(Throwable throwable) {
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
        throwable.printStackTrace();
    }
}
