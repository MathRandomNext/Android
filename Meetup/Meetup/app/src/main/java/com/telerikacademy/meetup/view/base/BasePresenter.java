package com.telerikacademy.meetup.view.base;

public interface BasePresenter<T extends BaseView> {

    void load();

    void setView(T view);
}
