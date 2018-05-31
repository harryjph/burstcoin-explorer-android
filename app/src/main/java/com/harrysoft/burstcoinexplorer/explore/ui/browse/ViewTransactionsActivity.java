package com.harrysoft.burstcoinexplorer.explore.ui.browse;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.entity.Transaction;
import com.harrysoft.burstcoinexplorer.burst.service.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.explore.entity.TransactionDisplayType;
import com.harrysoft.burstcoinexplorer.router.ExplorerRouter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class ViewTransactionsActivity extends ViewDetailsActivity implements AdapterView.OnItemClickListener {

    private TransactionDisplayType displayType;

    private BurstBlockchainService burstBlockchainService;

    private List<BigInteger> transactionIDs = new ArrayList<>();
    private final ArrayMap<BigInteger, Transaction> transactions = new ArrayMap<>();
    private int displayedItems = 0;
    private boolean loading;

    private TransactionsRecyclerAdapter transactionsAdapter;

    private RecyclerView transactionsList;

    void setupViewTransactionsActivity(TransactionDisplayType displayType, BurstBlockchainService burstBlockchainService) {
        this.displayType = displayType;
        this.burstBlockchainService = burstBlockchainService;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView type = view.findViewById(R.id.list_item_type);
        TextView data = view.findViewById(R.id.list_item_data);

        if (type.getText().toString().equals(getString(R.string.extra_block_extra))) {
            ExplorerRouter.viewTransactionDetailsByID(this, new BigInteger(data.getText().toString()));
        }
    }

    void onTransactionIDs(ArrayList<BigInteger> transactionIDs) {
        if (transactionIDs.size() > 0) {
            updateList(transactionIDs);
        } else {
            onNoTransactions();
        }
    }

    BurstBlockchainService getBurstBlockchainService() {
        return burstBlockchainService;
    }

    void setTransactionsList(RecyclerView transactionsList) {
        this.transactionsList = transactionsList;
        RecyclerView.LayoutManager transactionsManager = new LinearLayoutManager(this);
        transactionsList.setLayoutManager(transactionsManager);
    }

    private void updateList(List<BigInteger> transactionIDs) {
        this.transactionIDs = transactionIDs;
        transactionsAdapter = new TransactionsRecyclerAdapter(displayType, this, transactionIDs, this::loadMore);
        transactionsList.setAdapter(transactionsAdapter);
        loadMore();
    }

    private void loadMore() {
        if (!loading) {
            loading = true;
            int tempDisplayedItems = displayedItems + 25;
            if (tempDisplayedItems > transactionIDs.size()) {
                tempDisplayedItems = transactionIDs.size();
            }

            int transactionsToAdd = tempDisplayedItems - displayedItems;

            ArrayMap<BigInteger, Transaction> newTransactions = new ArrayMap<>();

            for (int i = 1; i <= transactionsToAdd; i++) {
                BigInteger transactionID = transactionIDs.get(displayedItems + i - 1); // get counts from 0, i counts from 1
                burstBlockchainService.fetchTransaction(transactionID)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(transaction -> {
                            newTransactions.put(transactionID, transaction);
                            if (newTransactions.size() == transactionsToAdd) {
                                finaliseLoadMore(newTransactions, transactionsToAdd);
                            }
                        }, error -> Toast.makeText(this, "Error loading transaction #" + transactionID.toString(), Toast.LENGTH_LONG).show());
            }
        }
    }

    private void finaliseLoadMore(Map<BigInteger, Transaction> newTransactions, int newDisplayedItems) {
        displayedItems += newDisplayedItems;
        transactions.putAll(newTransactions);
        transactionsAdapter.updateData(transactions);
        loading = false;
    }

    protected abstract void onError(Throwable throwable);
    protected abstract void onNoTransactions();
}
