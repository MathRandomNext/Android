package com.telerikacademy.meetup.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.AutoCompleteTextView;
import com.telerikacademy.meetup.R;
import com.telerikacademy.meetup.fragments.SearchHeaderFragment;
import com.telerikacademy.meetup.interfaces.IMenuInflater;
import com.telerikacademy.meetup.models.Venue;
import com.telerikacademy.meetup.views.adapters.NearbyVenuesRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class NearbyVenuesActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_venues);

        this.fragmentManager = getSupportFragmentManager();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_venues);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        NearbyVenuesRecyclerAdapter recyclerAdapter = new NearbyVenuesRecyclerAdapter(generateSampleData());

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation()));
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);

        final AutoCompleteTextView searchInput = (AutoCompleteTextView) findViewById(R.id.et_search);

        final SearchHeaderFragment searchFragment = (SearchHeaderFragment)
                this.fragmentManager.findFragmentById(R.id.fragment_search_header);

        if (searchFragment != null) {
            searchFragment.setFilter(searchInput, recyclerAdapter);
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .show(searchFragment)
                            .commit();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                boolean hasChanged = dy > 100 || dy < -100;
                boolean isVisible = searchFragment != null && searchFragment.isVisible();
                if (hasChanged && isVisible) {
                    fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .hide(searchFragment)
                            .commit();
                }

                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        IMenuInflater menuInflater = (IMenuInflater)
                this.fragmentManager.findFragmentById(R.id.fragment_tool_bar);

        if (menuInflater != null) {
            menuInflater.inflateMenu(R.menu.main, menu, getMenuInflater());
        }

        return true;
    }

    // TODO: Delete
    private List<Venue> generateSampleData() {
        List<Venue> venues = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Venue venue = new Venue(Integer.toString(i),
                    "Pri Ilyo #" + i, "zh.k. Lyulin " + i + 1, null, 0);
            venues.add(venue);
        }
        Venue someVen = new Venue("123", "Gosho", "Kostinbrod", null, 0);
        venues.add(someVen);

        return venues;
    }
}
