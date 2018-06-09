package com.harrysoft.burstcoinexplorer.observe.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.entity.NetworkStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BrokenPeersRecyclerAdapter extends RecyclerView.Adapter<BrokenPeersRecyclerAdapter.ViewHolder> {

    private final Context context;

    private List<NetworkStatus.BrokenPeer> brokenPeers = new ArrayList<>();

    BrokenPeersRecyclerAdapter(Context context) {
        this.context = context;
    }
    
    public void updateData(List<NetworkStatus.BrokenPeer> newBrokenPeers) {
        if (brokenPeers == null) {
            brokenPeers = newBrokenPeers;
            notifyDataSetChanged();
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return brokenPeers.size();
                }

                @Override
                public int getNewListSize() {
                    return newBrokenPeers.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return Objects.equals(brokenPeers.get(oldItemPosition).address, newBrokenPeers.get(newItemPosition).address);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    NetworkStatus.BrokenPeer newBrokenPeer = newBrokenPeers.get(newItemPosition);
                    NetworkStatus.BrokenPeer oldBrokenPeer = brokenPeers.get(oldItemPosition);
                    return Objects.equals(newBrokenPeer.address, oldBrokenPeer.address)
                            && Objects.equals(newBrokenPeer.countryCode, oldBrokenPeer.countryCode)
                            && Objects.equals(newBrokenPeer.height, oldBrokenPeer.height)
                            && Objects.equals(newBrokenPeer.platform, oldBrokenPeer.platform)
                            && Objects.equals(newBrokenPeer.status, oldBrokenPeer.status)
                            && Objects.equals(newBrokenPeer.version, oldBrokenPeer.version);
                }
            });
            brokenPeers = newBrokenPeers;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setupView(brokenPeers.get(position));
    }

    @Override
    public int getItemCount() {
        return brokenPeers.size();
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

        void setupView(NetworkStatus.BrokenPeer brokenPeer) {
            brokenPeer.status = brokenPeer.status.substring(0, 1).toUpperCase() + brokenPeer.status.substring(1);

            String reason;

            switch(brokenPeer.status) {
                case "Fork":
                    reason = context.getString(R.string.observe_broken_reason_fork);
                    break;

                case "Stuck":
                    reason = context.getString(R.string.observe_broken_reason_stuck);
                    break;

                case "Unreachable":
                    reason = context.getString(R.string.observe_broken_reason_unreachable);
                    break;

                case "Resync":
                    reason = context.getString(R.string.observe_broken_reason_resync);
                    break;

                default:
                    reason = context.getString(R.string.observe_broken_reason_unreachable);
                    break;
            }

            text1.setText(context.getString(R.string.basic_data, brokenPeer.address));
            text2.setText(context.getString(R.string.observe_broken_peer_description, brokenPeer.version, brokenPeer.platform, brokenPeer.countryCode, reason, String.valueOf(brokenPeer.height)));
            layout.setOnClickListener(view -> {
                String address = brokenPeer.address;
                if (address.contains("https://")) {
                    address = address.replace("https://", "");
                }
                if (address.contains("http://")) {
                    address = address.replace("http://", "");
                }
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://explore.burst.cryptoguru.org/peer/" + address));
                context.startActivity(i);
            });
        }
    }
}
