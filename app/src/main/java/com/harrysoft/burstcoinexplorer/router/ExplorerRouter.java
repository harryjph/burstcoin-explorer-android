package com.harrysoft.burstcoinexplorer.router;

import android.content.Context;
import android.content.Intent;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.entity.Block;
import com.harrysoft.burstcoinexplorer.burst.entity.Transaction;
import com.harrysoft.burstcoinexplorer.explore.ui.browse.ViewAccountDetailsActivity;
import com.harrysoft.burstcoinexplorer.explore.ui.browse.ViewAccountTransactionsActivity;
import com.harrysoft.burstcoinexplorer.explore.ui.browse.ViewBlockDetailsActivity;
import com.harrysoft.burstcoinexplorer.explore.ui.browse.ViewBlockExtraDetailsActivity;
import com.harrysoft.burstcoinexplorer.explore.ui.browse.ViewTransactionDetailsActivity;

import java.math.BigInteger;

public class ExplorerRouter {

    public static void viewBlockDetailsByBlock(Context context, Block block) {
        Intent i = new Intent(context, ViewBlockDetailsActivity.class);
        i.putExtra(context.getString(R.string.extra_block_parcel), block);
        context.startActivity(i);
    }

    public static void viewBlockDetailsByNumber(Context context, BigInteger blockNumber) {
        Intent i = new Intent(context, ViewBlockDetailsActivity.class);
        i.putExtra(context.getString(R.string.extra_block_number), blockNumber.toString());
        context.startActivity(i);
    }

    public static void viewBlockDetailsByID(Context context, BigInteger blockID) {
        Intent i = new Intent(context, ViewBlockDetailsActivity.class);
        i.putExtra(context.getString(R.string.extra_block_id), blockID.toString());
        context.startActivity(i);
    }

    public static void viewBlockExtraDetails(Context context, Block block) {
        Intent i = new Intent(context, ViewBlockExtraDetailsActivity.class);
        i.putExtra(context.getString(R.string.extra_block_parcel), block);
        context.startActivity(i);
    }

    public static void viewAccountDetails(Context context, BigInteger accountID) {
        Intent i = new Intent(context, ViewAccountDetailsActivity.class);
        i.putExtra(context.getString(R.string.extra_account_id), accountID.toString());
        context.startActivity(i);
    }

    public static void viewAccountTransactions(Context context, BigInteger accountID) {
        Intent i = new Intent(context, ViewAccountTransactionsActivity.class);
        i.putExtra(context.getString(R.string.extra_account_id), accountID.toString());
        context.startActivity(i);
    }

    public static void viewTransactionDetailsByTransaction(Context context, Transaction transaction) {
        Intent i = new Intent(context, ViewTransactionDetailsActivity.class);
        i.putExtra(context.getString(R.string.extra_transaction_parcel), transaction);
        context.startActivity(i);
    }

    public static void viewTransactionDetailsByID(Context context, BigInteger transactionID) {
        Intent i = new Intent(context, ViewTransactionDetailsActivity.class);
        i.putExtra(context.getString(R.string.extra_transaction_id), transactionID.toString());
        context.startActivity(i);
    }
}
