package com.telerikacademy.meetup.view.nearby_venues;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import com.telerikacademy.meetup.BaseApplication;
import com.telerikacademy.meetup.R;
import com.telerikacademy.meetup.config.di.module.ControllerModule;
import com.telerikacademy.meetup.provider.base.IRecyclerDecorationFactory;

import javax.inject.Inject;

public class NearbyVenuesContentFragment extends Fragment {

    @Inject
    IRecyclerDecorationFactory decorationFactory;

    @BindView(R.id.rv_venues)
    RecyclerView recyclerView;

    public NearbyVenuesContentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nearby_venues_content, container, false);
        BaseApplication.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        injectDependencies();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.addItemDecoration(decorationFactory.createDividerDecoration(
                linearLayoutManager.getOrientation()
        ));
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    private void injectDependencies() {
        BaseApplication
                .from(getContext())
                .getComponent()
                .getControllerComponent(new ControllerModule(
                        getActivity(), getFragmentManager()
                ))
                .inject(this);
    }
}