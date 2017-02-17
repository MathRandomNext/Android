package com.telerikacademy.meetup.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Filterable;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.telerikacademy.meetup.R;
import com.telerikacademy.meetup.ui.fragments.base.ISearchBar;

public class SearchHeaderFragment extends Fragment
        implements ISearchBar {

    @BindView(R.id.et_search)
    AutoCompleteTextView searchInput;

    public SearchHeaderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_header, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void setFilter(@NonNull final Filterable filterable) {
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterable.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
