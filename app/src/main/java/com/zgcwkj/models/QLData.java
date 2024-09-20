package com.zgcwkj.models;

public class QLData {

    private String weburl;
    private String clientId;
    private String clientSecret;
    private boolean autoEnable;

    public QLData() {
        weburl = "";
        clientId = "";
        clientSecret = "";
        autoEnable = false;
    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public boolean getAutoEnable() {
        return autoEnable;
    }

    public void setAutoEnable(boolean autoEnable) {
        this.autoEnable = autoEnable;
    }
}
