package com.zgcwkj.models;

//网站数据结构
public class WebData {
    //ID
    private String id;
    //选择状态
    private boolean isselect;
    //网站地址
    private String weburl;
    //Cookie
    private String cookie;
    //Cookie Key
    private String cookiekey;
    //Cookie 隔离
    private boolean cookieisolate;
    //备注
    private String remark;

    public WebData(boolean isselect) {
        this.weburl = "";
        this.cookie = "";
        this.cookiekey = "";
        this.cookieisolate = false;
        this.isselect = isselect;
        this.remark = "";
    }

    public WebData(boolean isselect, String weburl, String cookie, String cookiekey, boolean cookieIsolate, String remark) {
        this.weburl = weburl;
        this.cookie = cookie;
        this.cookiekey = cookiekey;
        this.cookieisolate = cookieIsolate;
        this.isselect = isselect;
        this.remark = remark;
    }

    public WebData(boolean isselect, String weburl, String cookie, String cookiekey, boolean cookieIsolate, String remark, String id) {
        this.weburl = weburl;
        this.cookie = cookie;
        this.cookiekey = cookiekey;
        this.cookieisolate = cookieIsolate;
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

    public boolean getIsselect() {
        return isselect;
    }

    public void setIsselect(boolean isselect) {
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

    public boolean getCookieIsolate() {
        return cookieisolate;
    }

    public void setCookieIsolate(boolean cookieIsolate) {
        this.cookieisolate = cookieIsolate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
