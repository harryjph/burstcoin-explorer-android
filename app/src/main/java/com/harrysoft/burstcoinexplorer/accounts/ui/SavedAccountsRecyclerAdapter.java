package com.harrysoft.burstcoinexplorer.accounts.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.harry1453.burst.explorer.entity.BurstAddress;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.accounts.db.SavedAccount;
import com.harrysoft.burstcoinexplorer.main.router.ExplorerRouter;
import com.harrysoft.burstcoinexplorer.util.TextFormatUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import burst.kit.entity.BurstID;

public class SavedAccountsRecyclerAdapter extends RecyclerView.Adapter<SavedAccountsRecyclerAdapter.ViewHolder> {

    private final Context context;

    private List<SavedAccount> savedAccounts = new ArrayList<>();

    SavedAccountsRecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setupView(savedAccounts.get(position));
    }

    @Override
    public int getItemCount() {
        return savedAccounts.size();
    }

    public void updateData(List<SavedAccount> newSavedAccounts) {
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
                            && Objects.equals(newAccount.getAddress(), oldAccount.getAddress())
                            && Objects.equals(newAccount.getLastKnownName(), oldAccount.getLastKnownName())
                            && Objects.equals(newAccount.getLastKnownBalance(), oldAccount.getLastKnownBalance());
                }
            });
            savedAccounts = newSavedAccounts;
            result.dispatchUpdatesTo(this);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final RelativeLayout layout;

        private final TextView text1;
        private final TextView text2;

        ViewHolder(View v) {
            super(v);
            layout = v.findViewById(R.id.list_item);
            text1 = v.findViewById(R.id.list_item_text1);
            text2 = v.findViewById(R.id.list_item_text2);
        }

        void setupView(SavedAccount savedAccount) {
            text1.setText(context.getString(R.string.basic_data, savedAccount.getAddress().getFullAddress()));
            String lastKnownBalance = savedAccount.getLastKnownBalance() == null ? context.getString(R.string.unknown_balance) : savedAccount.getLastKnownBalance().toString();
            String lastKnownName = savedAccount.getLastKnownName() == null ? context.getString(R.string.unknown_name) : TextFormatUtils.checkIfSet(context, savedAccount.getLastKnownName());
            String details = context.getString(R.string.saved_account_details_display_format, lastKnownName, lastKnownBalance);
            text2.setText(context.getString(R.string.basic_data, details));
            layout.setOnClickListener(view -> ExplorerRouter.viewAccountDetails(context, savedAccount.getAddress().getBurstID()));
        }
    }
}
