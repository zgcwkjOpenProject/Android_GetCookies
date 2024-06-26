package com.zgcwkj.models;

public class WebData {

    private String id;
    private Boolean isselect;
    private String weburl;
    private String cookie;
    private String cookiekey;
    private String remark;

    public WebData(Boolean isselect, String weburl, String cookie, String cookiekey, String remark) {
        this.weburl = weburl;
        this.cookie = cookie;
        this.cookiekey = cookiekey;
        this.isselect = isselect;
        this.remark = remark;
    }

    public WebData(Boolean isselect, String weburl, String cookie, String cookiekey, String remark, String id) {
        this.weburl = weburl;
        this.cookie = cookie;
        this.cookiekey = cookiekey;
        this.isselect = isselect;
        this.remark = remark;
        this.id = id;
    }

    public String getId() {
        if (id == null) return "";
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIsselect() {
        return isselect;
    }

    public void setIsselect(Boolean isselect) {
        this.isselect = isselect;
    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getCookiekey() {
        return cookiekey;
    }

    public void setCookiekey(String cookiekey) {
        this.cookiekey = cookiekey;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
