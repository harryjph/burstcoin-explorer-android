package com.harrysoft.burstcoinexplorer.observe;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.entity.ForkInfo;
import com.harrysoft.burstcoinexplorer.burst.utils.ForkUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

class ForkListRecyclerAdapter extends RecyclerView.Adapter<ForkListRecyclerAdapter.ViewHolder> {

    private final Context context;

    private BigInteger currentBlockHeight;

    private List<ForkInfo> forkList = new ArrayList<>();

    ForkListRecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setupView(forkList.get(position));
    }

    @Override
    public int getItemCount() {
        return forkList.size();
    }

    void setCurrentBlockHeight(@NonNull BigInteger newBlockHeight) {
        currentBlockHeight = newBlockHeight;
        notifyDataSetChanged();
    }

    void updateData(List<ForkInfo> newForkList) {
        forkList = newForkList;
        notifyDataSetChanged();
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

        void setupView(ForkInfo forkInfo) {
            text1.setText(context.getString(R.string.basic_data, forkInfo.name));
            if (currentBlockHeight != null) {
                String description = forkInfo.blockHeightSet ? ForkUtils.formatForkInfo(context, currentBlockHeight, forkInfo.name, forkInfo.blockHeight) : context.getString(R.string.fork_height_not_set);
                text2.setText(context.getString(R.string.basic_data, description));
            } else {
                text2.setText(R.string.fork_info_unavailable);
            }
            layout.setOnClickListener(view -> {
                if (forkInfo.infoPageSet) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(forkInfo.infoPage);
                    context.startActivity(i);
                }
            });
        }
    }
}