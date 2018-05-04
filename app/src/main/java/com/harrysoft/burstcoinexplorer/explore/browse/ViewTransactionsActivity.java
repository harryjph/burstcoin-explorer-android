package com.harrysoft.burstcoinexplorer.explore.browse;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.harrysoft.burstcoinexplorer.burst.explorer.BurstExplorer;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.api.BurstAPIService;

import java.math.BigInteger;
import java.util.ArrayList;

public abstract class ViewTransactionsActivity extends ViewDetailsActivity implements AdapterView.OnItemClickListener {

    private BurstAPIService burstAPIService;
    private BurstExplorer burstExplorer;

    private RecyclerView transactionsList;

    public void setupBurstServices(BurstAPIService burstAPIService, BurstExplorer burstExplorer) {
        this.burstAPIService = burstAPIService;
        this.burstExplorer = burstExplorer;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView type = view.findViewById(R.id.list_item_type);
        TextView data = view.findViewById(R.id.list_item_data);

        if (type.getText().toString().equals(getString(R.string.extra_block_extra))) {
            burstExplorer.viewTransactionDetailsByID(new BigInteger(data.getText().toString()));
        }
    }

    protected void onTransactionIDs(ArrayList<BigInteger> transactionIDs) {
        if (transactionIDs.size() > 0) {
            updateList(transactionIDs);
        } else {
            onNoTransactions();
        }
    }

    protected BurstAPIService getBurstAPIService() {
        return burstAPIService;
    }

    public void setTransactionsList(RecyclerView transactionsList) {
        this.transactionsList = transactionsList;
        RecyclerView.LayoutManager transactionsManager = new LinearLayoutManager(this);
        transactionsList.setLayoutManager(transactionsManager);
    }

    private void updateList(ArrayList<BigInteger> listItems) {
        RecyclerView.Adapter transactionsAdapter = new TransactionsRecyclerAdapter(this, burstAPIService, burstExplorer, listItems);
        transactionsList.setAdapter(transactionsAdapter);
    }

    protected abstract void onError(Throwable throwable);
    protected abstract void onNoTransactions();
}
