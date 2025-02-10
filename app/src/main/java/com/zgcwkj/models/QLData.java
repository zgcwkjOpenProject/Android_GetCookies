package com.zgcwkj.models;

//青龙数据结构
public class QLData {
    //青龙地址
    private String weburl;
    //客户端ID
    private String clientId;
    //客户端凭据
    private String clientSecret;
    //自动启用环境变量
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
