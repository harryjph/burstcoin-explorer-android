package com.harrysoft.burstcoinexplorer.explore.ui.browse;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.harrysoft.burstcoinexplorer.explore.entity.TransactionDisplayType;
import com.harrysoft.burstcoinexplorer.explore.viewmodel.browse.ViewTransactionsViewModel;
import com.harrysoft.burstcoinexplorer.explore.viewmodel.browse.ViewTransactionsViewModelFactory;

import java.math.BigInteger;
import java.util.List;

public abstract class ViewTransactionsActivity extends ViewDetailsActivity {
    protected void setupViewTransactionsActivity(RecyclerView recyclerView, ViewTransactionsViewModelFactory factory, TransactionDisplayType displayType, List<BigInteger> transactionIDs) {
        factory.setup(displayType, transactionIDs);
        ViewTransactionsViewModel viewTransactionsViewModel = ViewModelProviders.of(this, factory).get(ViewTransactionsViewModel.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TransactionsRecyclerAdapter transactionsAdapter = new TransactionsRecyclerAdapter(viewTransactionsViewModel.getDisplayType(), this, viewTransactionsViewModel.getTransactionIDs(), viewTransactionsViewModel::loadMoreTransactions);
        recyclerView.setAdapter(transactionsAdapter);
        viewTransactionsViewModel.getTransactions().observe(this, transactionsAdapter::updateData);
        viewTransactionsViewModel.getTransactionsLabel().observe(this, this::setTransactionsLabelText);
    }

    protected abstract void setTransactionsLabelText(@StringRes int text);
}
