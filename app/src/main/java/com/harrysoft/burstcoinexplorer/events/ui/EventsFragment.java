package com.harrysoft.burstcoinexplorer.events.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.events.viewmodel.EventsViewModel;
import com.harrysoft.burstcoinexplorer.events.viewmodel.EventsViewModelFactory;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;


public class EventsFragment extends Fragment {

    @Inject
    EventsViewModelFactory eventsViewModelFactory;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        EventsViewModel eventsViewModel = ViewModelProviders.of(this, eventsViewModelFactory).get(EventsViewModel.class);

        TextView errorMessage = view.findViewById(R.id.events_error_message);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.events_swiperefresh);
        RecyclerView recyclerView = view.findViewById(R.id.events_list);

        RecyclerView.LayoutManager listManager = new LinearLayoutManager(getActivity());
        EventListRecyclerAdapter eventListRecyclerAdapter = new EventListRecyclerAdapter(getContext());
        recyclerView.setLayoutManager(listManager);
        recyclerView.setAdapter(eventListRecyclerAdapter);

        swipeRefreshLayout.setOnRefreshListener(eventsViewModel);

        eventsViewModel.getEventsList().observe(this, eventListRecyclerAdapter::updateData);
        eventsViewModel.getErrorMessageVisibility().observe(this, errorMessage::setVisibility);
        eventsViewModel.getRefreshing().observe(this, swipeRefreshLayout::setRefreshing);

        return view;
    }
}
