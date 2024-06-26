package com.zgcwkj.getcks.web;

import androidx.lifecycle.ViewModel;

public class WebViewModel extends ViewModel {

    private WebHandler handler;

    public WebViewModel() {
    }

    public void set(WebHandler handler) {
        this.handler = handler;
    }
}