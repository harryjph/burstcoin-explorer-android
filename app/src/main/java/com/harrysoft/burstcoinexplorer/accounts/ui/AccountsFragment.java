package com.harrysoft.burstcoinexplorer.accounts.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.accounts.db.SavedAccount;
import com.harrysoft.burstcoinexplorer.accounts.viewmodel.SavedAccountViewModel;
import com.harrysoft.burstcoinexplorer.accounts.viewmodel.SavedAccountViewModelFactory;
import com.harrysoft.burstcoinexplorer.burst.utils.BurstUtils;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class AccountsFragment extends Fragment {

    private SavedAccountViewModel savedAccountViewModel;

    @Inject
    SavedAccountViewModelFactory savedAccountViewModelFactory;

    private RecyclerView accountsList;
    private TextView accountsLabel;
    private EditText addressBox;

    private boolean updated = false;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accounts, container, false);
        savedAccountViewModel = ViewModelProviders.of(this, savedAccountViewModelFactory).get(SavedAccountViewModel.class);

        accountsList = view.findViewById(R.id.accounts_list);
        accountsLabel = view.findViewById(R.id.accounts_list_label);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.accounts_swiperefresh);
        addressBox = view.findViewById(R.id.accounts_new_address);
        Button addButton = view.findViewById(R.id.accounts_new_add);

        RecyclerView.LayoutManager listManager = new LinearLayoutManager(getContext());
        accountsList.setLayoutManager(listManager);

        addButton.setOnClickListener(v -> addToDatabase());
        swipeRefreshLayout.setOnRefreshListener(savedAccountViewModel);

        savedAccountViewModel.getRefreshing().observe(this, swipeRefreshLayout::setRefreshing);
        savedAccountViewModel.getAddressBoxText().observe(this, addressBox::setText);
        savedAccountViewModel.getAddressBoxError().observe(this, addressBox::setError);
        savedAccountViewModel.getSavedAccountsList().observe(this, this::onSavedAccountsList);

        return view;
    }

    private void addToDatabase() {
        try {
            savedAccountViewModel.addToDatabase(getContext(), BurstUtils.toNumericID(addressBox.getText().toString()));
        } catch (BurstUtils.ReedSolomon.DecodeException e) {
            addressBox.setError(getString(R.string.error_burst_rs_invalid));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        savedAccountViewModel.onRefresh();
    }

    private void onSavedAccountsList(LiveData<List<SavedAccount>> savedAccountList) {
        updated = false;
        SavedAccountsRecyclerAdapter adapter = new SavedAccountsRecyclerAdapter(getContext());
        savedAccountList.observe(this, newSavedAccountList -> {
            adapter.updateData(newSavedAccountList);

            if (newSavedAccountList == null || newSavedAccountList.size() == 0) {
                accountsLabel.setText(R.string.pinned_accounts_empty);
            } else {
                accountsLabel.setText(R.string.pinned_accounts);
            }

            if (newSavedAccountList != null && !updated) {
                for (SavedAccount savedAccount : newSavedAccountList) {
                    savedAccountViewModel.updateSavedInfo(savedAccount.getNumericID());
                }
                updated = true;
            }
        });
        accountsList.setAdapter(adapter);
    }
}
