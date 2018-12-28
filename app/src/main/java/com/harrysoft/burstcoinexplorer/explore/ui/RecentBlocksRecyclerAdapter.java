package com.harrysoft.burstcoinexplorer.explore.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.harry1453.burst.explorer.entity.Block;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.main.router.ExplorerRouter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import burst.kit.entity.response.BlockResponse;

class RecentBlocksRecyclerAdapter extends RecyclerView.Adapter<RecentBlocksRecyclerAdapter.ViewHolder> {

    private final Context context;

    private List<BlockResponse> blocks = new ArrayList<>();

    RecentBlocksRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void updateData(List<BlockResponse> newBlocks) {
        if (blocks == null) {
            blocks = newBlocks;
            notifyDataSetChanged();
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return blocks.size();
                }

                @Override
                public int getNewListSize() {
                    return newBlocks.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return Objects.equals(blocks.get(oldItemPosition).getBlock(), newBlocks.get(newItemPosition).getBlock());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    BlockResponse newBlock = newBlocks.get(newItemPosition);
                    BlockResponse oldBlock = blocks.get(oldItemPosition);
                    return Objects.equals(newBlock.getBlock(), oldBlock.getBlock())
                            && Objects.equals(newBlock.getHeight(), oldBlock.getHeight())
                            && Objects.equals(newBlock.getTimestamp(), oldBlock.getTimestamp())
                            && Objects.equals(newBlock.getNumberOfTransactions(), oldBlock.getNumberOfTransactions())
                            && Objects.equals(newBlock.getTotalAmountNQT(), oldBlock.getTotalAmountNQT())
                            && Objects.equals(newBlock.getPayloadLength(), oldBlock.getPayloadLength())
                            && Objects.equals(newBlock.getGenerator(), oldBlock.getGenerator())
                            && Objects.equals(newBlock.getTotalFeeNQT(), oldBlock.getTotalFeeNQT());
                }
            });
            blocks = newBlocks;
            result.dispatchUpdatesTo(this);
        }
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

        ViewHolder(View v) {
            super(v);
            layout = v.findViewById(R.id.list_item);
            text1 = v.findViewById(R.id.list_item_text1);
            text2 = v.findViewById(R.id.list_item_text2);
        }

        void setupView(BlockResponse block) {
            text1.setText(context.getString(R.string.block_number_with_data, String.valueOf(block.getBlock())));
            text2.setText(context.getString(R.string.number_of_transactions_with_data, String.valueOf(block.getNumberOfTransactions()), block.getTotalAmountNQT().toString()));

            layout.setOnClickListener(view -> ExplorerRouter.viewBlockDetailsByID(context, block.getBlock()));
        }
    }
}