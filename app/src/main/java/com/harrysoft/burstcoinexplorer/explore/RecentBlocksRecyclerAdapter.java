package com.harrysoft.burstcoinexplorer.explore;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.harrysoft.burstcoinexplorer.BurstExplorer;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.entity.Block;

import java.math.BigInteger;

class RecentBlocksRecyclerAdapter extends RecyclerView.Adapter<RecentBlocksRecyclerAdapter.ViewHolder> {

    private final Context context;
    private final BurstExplorer burstExplorer;

    private final Block[] blocks;

    RecentBlocksRecyclerAdapter(Context context, BurstExplorer burstExplorer, Block[] blocks) {
        this.context = context;
        this.burstExplorer = burstExplorer;
        this.blocks = blocks;
    }

    @Override
    public RecentBlocksRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(context, burstExplorer, LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecentBlocksRecyclerAdapter.ViewHolder holder, int position) {
        holder.setupView(blocks[position]);
    }

    @Override
    public int getItemCount() {
        return blocks.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final Context context;

        private final RelativeLayout layout;

        private final BurstExplorer burstExplorer;

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

        void setupView(Block block) {
            text1.setText(context.getString(R.string.block_number_with_data, block.blockNumber.toString()));
            text2.setText(context.getString(R.string.number_of_transactions_with_data, block.transactionCount.toString(), block.total.toString()));
            type.setText(context.getString(R.string.extra_block));
            data.setText(context.getString(R.string.basic_data, block.blockNumber.toString()));

            layout.setOnClickListener(view -> {
                if (type.getText().toString().equals(context.getString(R.string.extra_block))) {
                    burstExplorer.viewBlockDetailsByBlock(block);
                }
            });
        }
    }
}