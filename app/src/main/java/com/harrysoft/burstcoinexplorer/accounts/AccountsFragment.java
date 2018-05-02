package com.harrysoft.burstcoinexplorer.accounts;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.harrysoft.burstcoinexplorer.BurstExplorer;
import com.harrysoft.burstcoinexplorer.HSBurstExplorer;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.accounts.db.AccountsDatabase;
import com.harrysoft.burstcoinexplorer.accounts.db.SavedAccount;
import com.harrysoft.burstcoinexplorer.burst.BurstUtils;
import com.harrysoft.burstcoinexplorer.burst.api.BurstAPIService;
import com.harrysoft.burstcoinexplorer.burst.api.PoccAPIService;
import com.harrysoft.burstcoinexplorer.burst.entity.BurstAddress;

import java.math.BigInteger;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AccountsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private BurstExplorer burstExplorer;
    private BurstAPIService burstAPIService;

    private RecyclerView accountsList;
    private TextView accountsLabel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText addressBox;
    private AccountsDatabase accountsDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accounts, container, false);

        accountsList = view.findViewById(R.id.accounts_list);
        accountsLabel = view.findViewById(R.id.accounts_list_label);
        swipeRefreshLayout = view.findViewById(R.id.accounts_swiperefresh);
        addressBox = view.findViewById(R.id.accounts_new_address);
        Button addButton = view.findViewById(R.id.accounts_new_add);

        burstExplorer = new HSBurstExplorer(getContext());
        burstAPIService = new PoccAPIService(getContext());

        accountsDatabase = SavedAccountsUtils.getAccountsDatabase(getContext());

        addButton.setOnClickListener(v -> addToDatabase());

        RecyclerView.LayoutManager listManager = new LinearLayoutManager(getActivity());
        accountsList.setLayoutManager(listManager);

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.setRefreshing(true);
        onRefresh();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accountsDatabase.close();
    }

    private void setAdapter(LiveData<List<SavedAccount>> savedAccountList) {
        swipeRefreshLayout.setRefreshing(false);
        SavedAccountsRecyclerAdapter adapter = new SavedAccountsRecyclerAdapter(getContext(), burstExplorer);
        savedAccountList.observe(this, newSavedAccountList -> {
            adapter.updateData(newSavedAccountList);

            if (newSavedAccountList == null || newSavedAccountList.size() == 0) {
                accountsLabel.setText(R.string.pinned_accounts_empty);
            } else {
                accountsLabel.setText(R.string.pinned_accounts);
            }

            if (newSavedAccountList != null) {
                for (SavedAccount savedAccount : newSavedAccountList) {
                    updateSavedInfo(savedAccount.getNumericID());
                }
            }
        });
        accountsList.setAdapter(adapter);
    }

    private void addToDatabase() {
        try {
            SavedAccountsUtils.saveAccount(getContext(), accountsDatabase, BurstUtils.toNumericID(addressBox.getText().toString()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {}, // it should update all by itself as it's a livedata
                            t -> {
                                if (t.getMessage().equals(getString(R.string.error_account_already_in_database))) {
                                    addressBox.setError(t.getMessage());
                                } else {
                                    t.printStackTrace();
                                }
                            });
        } catch (BurstUtils.ReedSolomon.DecodeException e) {
            addressBox.setError(getString(R.string.error_burst_rs_invalid));
        }
    }

    private void updateSavedInfo(BigInteger accountID) {
        Completable.fromRunnable(() -> {
            SavedAccount savedAccount = accountsDatabase.savedAccountDao().findByNumericID(accountID);

            if (savedAccount != null) {
                burstAPIService.fetchAccount(savedAccount.getNumericID())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(account -> {
                            savedAccount.setLastKnownBalance(account.balance);
                            savedAccount.setLastKnownName(account.name);
                            Completable.fromRunnable(() -> accountsDatabase.savedAccountDao().update(savedAccount))
                                    .subscribeOn(Schedulers.io())
                                    .subscribe();
                        }, Throwable::printStackTrace);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void onRefresh() {
        Single.fromCallable(() -> accountsDatabase.savedAccountDao().getAll())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setAdapter, t -> swipeRefreshLayout.setRefreshing(false));
    }
}
