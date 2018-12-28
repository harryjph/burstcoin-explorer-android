package com.harrysoft.burstcoinexplorer.main.router;

import android.content.Context;
import android.content.Intent;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.explore.ui.browse.ViewAccountDetailsActivity;
import com.harrysoft.burstcoinexplorer.explore.ui.browse.ViewAccountTransactionsActivity;
import com.harrysoft.burstcoinexplorer.explore.ui.browse.ViewBlockDetailsActivity;
import com.harrysoft.burstcoinexplorer.explore.ui.browse.ViewBlockExtraDetailsActivity;
import com.harrysoft.burstcoinexplorer.explore.ui.browse.ViewTransactionDetailsActivity;

import burst.kit.entity.BurstID;

public class ExplorerRouter {

    public static void viewBlockDetailsByNumber(Context context, long blockNumber) {
        Intent i = new Intent(context, ViewBlockDetailsActivity.class);
        i.putExtra(context.getString(R.string.extra_block_number), blockNumber);
        context.startActivity(i);
    }

    public static void viewBlockDetailsByID(Context context, BurstID blockID) {
        Intent i = new Intent(context, ViewBlockDetailsActivity.class);
        i.putExtra(context.getString(R.string.extra_block_id), blockID.toString());
        context.startActivity(i);
    }

    public static void viewBlockExtraDetails(Context context, BurstID blockID) {
        Intent i = new Intent(context, ViewBlockExtraDetailsActivity.class);
        i.putExtra(context.getString(R.string.extra_block_id), blockID.toString());
        context.startActivity(i);
    }

    public static void viewAccountDetails(Context context, BurstID accountID) {
        Intent i = new Intent(context, ViewAccountDetailsActivity.class);
        i.putExtra(context.getString(R.string.extra_account_id), accountID.toString());
        context.startActivity(i);
    }

    public static void viewAccountTransactions(Context context, BurstID accountID) {
        Intent i = new Intent(context, ViewAccountTransactionsActivity.class);
        i.putExtra(context.getString(R.string.extra_account_id), accountID.toString());
        context.startActivity(i);
    }

    public static void viewTransactionDetailsByID(Context context, BurstID transactionID) {
        Intent i = new Intent(context, ViewTransactionDetailsActivity.class);
        i.putExtra(context.getString(R.string.extra_transaction_id), transactionID.toString());
        context.startActivity(i);
    }
}
