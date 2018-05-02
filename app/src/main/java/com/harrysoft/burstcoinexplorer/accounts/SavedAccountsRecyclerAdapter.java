package com.harrysoft.burstcoinexplorer.accounts;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.harrysoft.burstcoinexplorer.BurstExplorer;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.accounts.db.SavedAccount;
import com.harrysoft.burstcoinexplorer.burst.BurstUtils;
import com.harrysoft.burstcoinexplorer.burst.entity.BurstAddress;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SavedAccountsRecyclerAdapter extends RecyclerView.Adapter<SavedAccountsRecyclerAdapter.ViewHolder> {

    private final Context context;

    private final BurstExplorer burstExplorer;

    private List<SavedAccount> savedAccounts = new ArrayList<>();

    SavedAccountsRecyclerAdapter(Context context, BurstExplorer burstExplorer) {
        this.context = context;
        this.burstExplorer = burstExplorer;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(context, burstExplorer, LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setupView(savedAccounts.get(position));
    }

    @Override
    public int getItemCount() {
        return savedAccounts.size();
    }

    void updateData(List<SavedAccount> newSavedAccounts) {
        if (savedAccounts == null) {
            savedAccounts = newSavedAccounts;
            notifyDataSetChanged();
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return savedAccounts.size();
                }

                @Override
                public int getNewListSize() {
                    return newSavedAccounts.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return savedAccounts.get(oldItemPosition).getId() == newSavedAccounts.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    SavedAccount newAccount = newSavedAccounts.get(newItemPosition);
                    SavedAccount oldAccount = savedAccounts.get(oldItemPosition);
                    return newAccount.getId() == oldAccount.getId()
                            && Objects.equals(newAccount.getNumericID(), oldAccount.getNumericID())
                            && Objects.equals(newAccount.getLastKnownName(), oldAccount.getLastKnownName())
                            && Objects.equals(newAccount.getLastKnownBalance(), oldAccount.getLastKnownBalance());
                }
            });
            savedAccounts = newSavedAccounts;
            result.dispatchUpdatesTo(this);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final Context context;

        private final BurstExplorer burstExplorer;

        private final RelativeLayout layout;

        private final TextView text1;
        private final TextView text2;
        private final TextView type;
        private final TextView data;

        ViewHolder(Context context, BurstExplorer burstExplorer, View v) {
            super(v);
            this.context = context;
            this.burstExplorer = burstExplorer;
            layout = v.findViewById(R.id.list_item);
            text1 = v.findViewById(R.id.list_item_text1);
            text2 = v.findViewById(R.id.list_item_text2);
            type = v.findViewById(R.id.list_item_type);
            data = v.findViewById(R.id.list_item_data);
        }

        void setupView(SavedAccount savedAccount) {
            text1.setText(context.getString(R.string.basic_data, new BurstAddress(savedAccount.getNumericID()).getFullAddress()));
            String lastKnownBalance = savedAccount.getLastKnownBalance() == null ? context.getString(R.string.unknown_balance) : savedAccount.getLastKnownBalance().toString();
            String lastKnownName = savedAccount.getLastKnownName() == null ? context.getString(R.string.unknown_balance) : BurstUtils.burstName(context, savedAccount.getLastKnownName());
            String details = context.getString(R.string.saved_account_details_display_format, lastKnownName, lastKnownBalance);
            text2.setText(context.getString(R.string.basic_data, details));
            type.setText(context.getString(R.string.extra_account_id));
            data.setText(context.getString(R.string.basic_data, savedAccount.getNumericID().toString())); // todo we can probably remove type and data
            layout.setOnClickListener(view -> {
                burstExplorer.viewAccountDetails(savedAccount.getNumericID());
            });
        }
    }
}
