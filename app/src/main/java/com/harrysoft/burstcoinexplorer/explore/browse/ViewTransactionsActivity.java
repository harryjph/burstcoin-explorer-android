package com.harrysoft.burstcoinexplorer.explore.browse;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.service.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.router.ExplorerRouter;

import java.math.BigInteger;
import java.util.ArrayList;

public abstract class ViewTransactionsActivity extends ViewDetailsActivity implements AdapterView.OnItemClickListener {

    private TransactionDisplayType displayType;

    private BurstBlockchainService burstBlockchainService;

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

    private void updateList(ArrayList<BigInteger> listItems) {
        RecyclerView.Adapter transactionsAdapter = new TransactionsRecyclerAdapter(displayType, this, burstBlockchainService, listItems);
        transactionsList.setAdapter(transactionsAdapter);
    }

    protected abstract void onError(Throwable throwable);
    protected abstract void onNoTransactions();
}
