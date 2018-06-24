package com.harrysoft.burstcoinexplorer.explore.ui.browse;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.harry1453.burst.explorer.entity.Transaction;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.util.BurstFormatUtils;
import com.harrysoft.burstcoinexplorer.explore.entity.TransactionDisplayType;
import com.harrysoft.burstcoinexplorer.router.ExplorerRouter;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class TransactionsRecyclerAdapter extends RecyclerView.Adapter<TransactionsRecyclerAdapter.ViewHolder> {

    private final TransactionDisplayType transactionDisplayType;

    private final Context context;

    private final List<BigInteger> transactionIDs;
    private Map<BigInteger, Transaction> transactions = new ArrayMap<>();

    private final OnLoadMoreRequestListener listener;

    private int displayedItems;
    private final int totalItems;

    private final static int TRANSACTION_VIEW_TYPE = 1; // todo enum
    private final static int LOAD_MORE_VIEW_TYPE = 2;

    TransactionsRecyclerAdapter(TransactionDisplayType transactionDisplayType, Context context, List<BigInteger> transactionIDs, OnLoadMoreRequestListener listener) {
        this.transactionDisplayType = transactionDisplayType;
        this.context = context;
        this.transactionIDs = transactionIDs;
        this.listener = listener;
        totalItems = transactionIDs.size();
        displayedItems = 0;
    }

    public void updateData(Map<BigInteger, Transaction> newTransactions) {
        if (transactions == null) {
            transactions = newTransactions;
            notifyDataSetChanged();
        } else {
            int oldSize = getItemCount(displayedItems, totalItems);
            int newSize = getItemCount(newTransactions.size(), totalItems);
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return oldSize;
                }

                @Override
                public int getNewListSize() {
                    return newSize;
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return oldItemPosition < oldSize - 1 && newItemPosition < newSize - 1 && Objects.equals(transactions.get(transactionIDs.get(oldItemPosition)).transactionID, newTransactions.get(transactionIDs.get(newItemPosition)).transactionID);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    if (oldItemPosition < oldSize - 1 && newItemPosition < newSize - 1) {
                        Transaction oldTransaction = transactions.get(transactionIDs.get(oldItemPosition));
                        Transaction newTransaction = newTransactions.get(transactionIDs.get(newItemPosition));
                        return Objects.equals(newTransaction.transactionID, oldTransaction.transactionID)
                                && Objects.equals(newTransaction.amount, oldTransaction.amount)
                                && Objects.equals(newTransaction.sender, oldTransaction.sender)
                                && Objects.equals(newTransaction.recipient, oldTransaction.recipient);
                    } else {
                        return false;
                    }
                }
            });
            transactions = newTransactions;
            result.dispatchUpdatesTo(this);
        }
        displayedItems = newTransactions.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == displayedItems) { // if it is longer than the list of displayed items (displayedItems counts from 1, position counts from 0)
            return LOAD_MORE_VIEW_TYPE;
        } else {
            return TRANSACTION_VIEW_TYPE;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch(viewType) {
            default:
            case TRANSACTION_VIEW_TYPE:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false), viewType);

            case LOAD_MORE_VIEW_TYPE:
                ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_load_more, parent, false), viewType);
                viewHolder.setOnClickListener(view -> listener.loadMore());
                return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < displayedItems) {
            BigInteger transactionID = transactionIDs.get(position);
            if (transactionID != null && transactions.get(transactionID) != null) {
                holder.setupView(transactions.get(transactionID));
            }
        }
    }

    @Override
    public int getItemCount() {
        return getItemCount(displayedItems, totalItems);
    }

    private static int getItemCount(int displayedItems, int totalItems) {
        if (displayedItems == totalItems) {
            return displayedItems;
        } else if (displayedItems == 0) {
            return 0;
        } else {
            return displayedItems + 1;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final RelativeLayout listItem;
        private final TextView text1;
        private final TextView text2;
        private final Button loadMore;
        private final int viewType;

        ViewHolder(View v, int viewType) {
            super(v);
            this.viewType = viewType;
            switch (viewType) {
                case TRANSACTION_VIEW_TYPE:
                    listItem = v.findViewById(R.id.list_item);
                    text1 = v.findViewById(R.id.list_item_text1);
                    text2 = v.findViewById(R.id.list_item_text2);
                    loadMore = null;
                    break;

                case LOAD_MORE_VIEW_TYPE:
                    listItem = null;
                    text1 = null;
                    text2 = null;
                    loadMore = v.findViewById(R.id.view_transactions_load_more);
                    break;

                default:
                    listItem = null;
                    text1 = null;
                    text2 = null;
                    loadMore = null;
            }
        }

        void setupView(Transaction transaction) {
            if (viewType == TRANSACTION_VIEW_TYPE) {
                text1.setText(context.getString(R.string.transaction_id_with_data, transaction.transactionID.toString()));
                text2.setText(BurstFormatUtils.transactionSummary(context, transaction, transactionDisplayType));
                listItem.setOnClickListener(view -> ExplorerRouter.viewTransactionDetailsByID(context, transaction.transactionID));
            }
        }

        void setOnClickListener(View.OnClickListener onClickListener) {
            if (viewType == LOAD_MORE_VIEW_TYPE) {
                loadMore.setOnClickListener(view -> {
                    onClickListener.onClick(view);
                    Toast.makeText(context, R.string.loading, Toast.LENGTH_LONG).show(); // todo button text -> loading
                });
            }
        }
    }

    public interface OnLoadMoreRequestListener {
        void loadMore();
    }
}