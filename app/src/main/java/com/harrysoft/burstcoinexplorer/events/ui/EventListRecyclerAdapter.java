package com.harrysoft.burstcoinexplorer.events.ui;

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
import android.widget.Toast;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.entity.EventInfo;
import com.harrysoft.burstcoinexplorer.events.entity.EventsList;
import com.harrysoft.burstcoinexplorer.events.util.EventUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

class EventListRecyclerAdapter extends RecyclerView.Adapter<EventListRecyclerAdapter.ViewHolder> {

    private final Context context;

    private EventsList eventsList = EventsList.EMPTY;

    EventListRecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setupView(eventsList.events.get(position));
    }

    @Override
    public int getItemCount() {
        return eventsList.events.size();
    }

    void updateData(EventsList eventsList) {
        this.eventsList = eventsList;
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

        void setupView(EventInfo eventInfo) {
            text1.setText(context.getString(R.string.basic_data, eventInfo.name));
            text2.setText(eventInfo.blockHeightSet ? EventUtils.formatEventInfo(context, eventsList.blockHeight, eventInfo.name, eventInfo.blockHeight) : context.getString(R.string.event_height_not_set));
            layout.setOnClickListener(view -> {
                if (eventInfo.infoPageSet) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(eventInfo.infoPage);
                    context.startActivity(i);
                } else {
                    Toast.makeText(context, R.string.event_info_unavailable, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}