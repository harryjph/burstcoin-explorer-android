package com.harrysoft.burstcoinexplorer.explore.browse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.harrysoft.burstcoinexplorer.burst.api.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.burst.explorer.BurstExplorer;
import com.harrysoft.burstcoinexplorer.burst.explorer.AndroidBurstExplorer;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.accounts.SavedAccountsUtils;
import com.harrysoft.burstcoinexplorer.accounts.db.AccountsDatabase;
import com.harrysoft.burstcoinexplorer.accounts.db.SavedAccount;
import com.harrysoft.burstcoinexplorer.burst.utils.BurstUtils;
import com.harrysoft.burstcoinexplorer.burst.entity.Account;
import com.harrysoft.burstcoinexplorer.util.TextViewUtils;

import java.math.BigInteger;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ViewAccountDetailsActivity extends ViewDetailsActivity {

    BurstExplorer burstExplorer;
    @Inject
    BurstBlockchainService burstBlockchainService;
    @Inject
    AccountsDatabase accountsDatabase;

    private BigInteger accountID;
    private Account account;

    private TextView addressText, publicKeyText, nameText, balanceText, sentAmountText, receivedAmountText, feesText, soloMinedBalanceText, poolMinedBalanceText, rewardRecipientText;
    Button saveAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account_details);
        burstExplorer = new AndroidBurstExplorer(this);

        accountID = new BigInteger(getIntent().getStringExtra(getString(R.string.extra_account_id)));
        setupAccountSaveChecker();

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

        viewExtraButton.setOnClickListener(view -> {
            if (account != null) {
                burstExplorer.viewAccountTransactions(account.address.getNumericID());
            } else if (accountID != null) {
                burstExplorer.viewAccountTransactions(accountID);
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.extra_account_parcel))) {
            onAccount(savedInstanceState.getParcelable(getString(R.string.extra_account_parcel)));
        } else {
            burstBlockchainService.fetchAccount(accountID)
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
        if (!TextUtils.isEmpty(account.rewardRecipient.getFullAddress())) {
            rewardRecipientText.setText(getString(R.string.address_display_format, account.rewardRecipient.getFullAddress(), BurstUtils.burstName(this, account.rewardRecipientName)));
        } else {
            rewardRecipientText.setText(R.string.not_set);
        }
        updateLinks();
    }

    private void updateLinks() {
        if (account != null && !account.address.getRawAddress().equals(account.rewardRecipient.getRawAddress()) && !TextUtils.isEmpty(account.rewardRecipient.getFullAddress())) { // if sender != recipient && recipient is set
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

    @Nullable
    private Completable saveAccountBasedOnLoadState(AccountsDatabase accountsDatabase) {
        if (account != null) {
            SavedAccount savedAccount =  new SavedAccount();
            savedAccount.setNumericID(account.address.getNumericID());
            savedAccount.setLastKnownName(account.name);
            savedAccount.setLastKnownBalance(account.balance);
            return SavedAccountsUtils.saveAccount(this, accountsDatabase, savedAccount);
        } else if (accountID != null) {
            return SavedAccountsUtils.saveAccount(this, accountsDatabase, accountID);
        } else {
            return null;
        }
    }

    @Nullable
    private Completable deleteAccountBasedOnLoadState(AccountsDatabase accountsDatabase) {
        if (account != null) {
            return SavedAccountsUtils.deleteAccount(this, accountsDatabase, account.address.getNumericID());
        } else if (accountID != null) {
            return SavedAccountsUtils.deleteAccount(this, accountsDatabase, accountID);
        } else {
            return null;
        }
    }

    private void setupAccountSaveChecker() {
        SavedAccountsUtils.getLiveAccount(accountsDatabase, accountID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(liveData -> {
                    liveData.observe(this, savedAccount -> {
                        boolean accountSaved = savedAccount != null;
                        if (accountSaved) {
                            saveAccountButton.setText(R.string.unsave_account);
                            saveAccountButton.setOnClickListener(deleteOnClickListener);
                        } else {
                            saveAccountButton.setText(R.string.save_account);
                            saveAccountButton.setOnClickListener(saveOnClickListener);
                        }
                    });
                }, t -> saveAccountButton.setVisibility(View.GONE));
    }

    private View.OnClickListener saveOnClickListener = v -> {
        Completable saveAccountCompletable = saveAccountBasedOnLoadState(accountsDatabase);
        if (saveAccountCompletable != null) {
            saveAccountCompletable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {},
                            t -> {
                                if (t.getMessage().equals(getString(R.string.error_account_already_in_database))) {
                                    Toast.makeText(ViewAccountDetailsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                } else {
                                    t.printStackTrace();
                                }
                            });
        }
    };

    private View.OnClickListener deleteOnClickListener = v -> {
        Completable deleteAccountCompletable = deleteAccountBasedOnLoadState(accountsDatabase);
        if (deleteAccountCompletable != null) {
            deleteAccountCompletable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {},
                            t -> {
                                if (t.getMessage().equals(getString(R.string.error_account_already_in_database))) {
                                    Toast.makeText(ViewAccountDetailsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                } else {
                                    t.printStackTrace();
                                }
                            });
        }
    };
}
