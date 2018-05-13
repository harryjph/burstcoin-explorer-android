package com.harrysoft.burstcoinexplorer.burst.explorer;

import android.content.Context;
import android.content.Intent;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.entity.Block;
import com.harrysoft.burstcoinexplorer.burst.entity.Transaction;
import com.harrysoft.burstcoinexplorer.explore.browse.ViewAccountDetailsActivity;
import com.harrysoft.burstcoinexplorer.explore.browse.ViewAccountTransactionsActivity;
import com.harrysoft.burstcoinexplorer.explore.browse.ViewBlockDetailsActivity;
import com.harrysoft.burstcoinexplorer.explore.browse.ViewBlockExtraDetailsActivity;
import com.harrysoft.burstcoinexplorer.explore.browse.ViewTransactionDetailsActivity;

import java.math.BigInteger;

public class AndroidBurstExplorer implements BurstExplorer {

    private final Context context;

    public AndroidBurstExplorer(Context context) {
        this.context = context;
    }

    @Override
    public void viewBlockDetailsByBlock(Block block) {
        Intent i = new Intent(context, ViewBlockDetailsActivity.class);
        i.putExtra(context.getString(R.string.extra_block_parcel), block);
        context.startActivity(i);
    }

    @Override
    public void viewBlockDetailsByNumber(BigInteger blockNumber) {
        Intent i = new Intent(context, ViewBlockDetailsActivity.class);
        i.putExtra(context.getString(R.string.extra_block_number), blockNumber.toString());
        context.startActivity(i);
    }

    @Override
    public void viewBlockDetailsByID(BigInteger blockID) {
        Intent i = new Intent(context, ViewBlockDetailsActivity.class);
        i.putExtra(context.getString(R.string.extra_block_id), blockID.toString());
        context.startActivity(i);
    }

    @Override
    public void viewBlockExtraDetails(BigInteger blockID) {
        Intent i = new Intent(context, ViewBlockExtraDetailsActivity.class);
        i.putExtra(context.getString(R.string.extra_block_id), blockID.toString());
        context.startActivity(i);
    }

    @Override
    public void viewAccountDetails(BigInteger accountID) {
        Intent i = new Intent(context, ViewAccountDetailsActivity.class);
        i.putExtra(context.getString(R.string.extra_account_id), accountID.toString());
        context.startActivity(i);
    }

    @Override
    public void viewAccountTransactions(BigInteger accountID) {
        Intent i = new Intent(context, ViewAccountTransactionsActivity.class);
        i.putExtra(context.getString(R.string.extra_account_id), accountID.toString());
        context.startActivity(i);
    }

    @Override
    public void viewTransactionDetailsByTransaction(Transaction transaction) {
        Intent i = new Intent(context, ViewTransactionDetailsActivity.class);
        i.putExtra(context.getString(R.string.extra_transaction_parcel), transaction);
        context.startActivity(i);
    }

    @Override
    public void viewTransactionDetailsByID(BigInteger transactionID) {
        Intent i = new Intent(context, ViewTransactionDetailsActivity.class);
        i.putExtra(context.getString(R.string.extra_transaction_id), transactionID.toString());
        context.startActivity(i);
    }
}
