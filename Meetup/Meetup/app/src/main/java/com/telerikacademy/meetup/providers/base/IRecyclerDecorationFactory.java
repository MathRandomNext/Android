package com.telerikacademy.meetup.providers.base;

import android.support.v7.widget.RecyclerView;

public interface IRecyclerDecorationFactory {

    RecyclerView.ItemDecoration createDividerDecoration(int orientation);
}
