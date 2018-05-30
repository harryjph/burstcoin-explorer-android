package com.harrysoft.burstcoinexplorer.explore.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.entity.Block;
import com.harrysoft.burstcoinexplorer.router.ExplorerRouter;

import java.util.ArrayList;
import java.util.List;

class RecentBlocksRecyclerAdapter extends RecyclerView.Adapter<RecentBlocksRecyclerAdapter.ViewHolder> {

    private final Context context;

    private List<Block> blocks = new ArrayList<>();

    RecentBlocksRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void updateData(List<Block> blocks) {
        this.blocks = blocks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecentBlocksRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecentBlocksRecyclerAdapter.ViewHolder holder, int position) {
        holder.setupView(blocks.get(position));
    }

    @Override
    public int getItemCount() {
        return blocks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final RelativeLayout layout;

        private final TextView text1;
        private final TextView text2;
        private final TextView type;
        private final TextView data;

        ViewHolder(View v) {
            super(v);
            layout = v.findViewById(R.id.list_item);
            text1 = v.findViewById(R.id.list_item_text1);
            text2 = v.findViewById(R.id.list_item_text2);
            type = v.findViewById(R.id.list_item_type);
            data = v.findViewById(R.id.list_item_data);
        }

        void setupView(Block block) {
            text1.setText(context.getString(R.string.block_number_with_data, block.blockNumber.toString()));
            text2.setText(context.getString(R.string.number_of_transactions_with_data, block.transactionCount.toString(), block.total.toString()));
            type.setText(context.getString(R.string.extra_block));
            data.setText(context.getString(R.string.basic_data, block.blockNumber.toString()));

            layout.setOnClickListener(view -> {
                if (type.getText().toString().equals(context.getString(R.string.extra_block))) {
                    ExplorerRouter.viewBlockDetailsByBlock(context, block);
                }
            });
        }
    }
}