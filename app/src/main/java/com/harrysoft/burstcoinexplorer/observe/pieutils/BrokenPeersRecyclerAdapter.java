package com.harrysoft.burstcoinexplorer.observe.pieutils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.harrysoft.burstcoinexplorer.BurstExplorer;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.entity.NetworkStatus;

import java.util.ArrayList;

public class BrokenPeersRecyclerAdapter extends RecyclerView.Adapter<BrokenPeersRecyclerAdapter.ViewHolder> {

    private final Context context;

    private final ArrayList<NetworkStatus.BrokenPeer> brokenPeers;

    public BrokenPeersRecyclerAdapter(Context context, ArrayList<NetworkStatus.BrokenPeer> brokenPeers) {
        this.context = context;
        this.brokenPeers = brokenPeers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setupView(brokenPeers.get(position));
    }

    @Override
    public int getItemCount() {
        return brokenPeers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final Context context;

        private final RelativeLayout layout;

        private final TextView text1;
        private final TextView text2;
        private final TextView type;
        private final TextView data;

        ViewHolder(Context context, View v) {
            super(v);
            this.context = context;
            layout = v.findViewById(R.id.list_item);
            text1 = v.findViewById(R.id.list_item_text1);
            text2 = v.findViewById(R.id.list_item_text2);
            type = v.findViewById(R.id.list_item_type);
            data = v.findViewById(R.id.list_item_data);
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
            type.setText(context.getString(R.string.extra_broken_peer));
            data.setText(context.getString(R.string.basic_data, brokenPeer.address));
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
