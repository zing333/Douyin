package cn.nineton.onetake.bean;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

public class AudioPurchaseBean {
    public static final int PURCHASED_STATE_FAILED = 0;
    public static final int PURCHASED_STATE_SUCCESS = 1;
    private ErrorBean error;
    private int filters_id;
    private int purchased;
    private int user_wallet;
    private String zip_url;

    public static AudioPurchaseBean objectFromData(String str) {
        return (AudioPurchaseBean) new Gson().fromJson(str, AudioPurchaseBean.class);
    }

    public static AudioPurchaseBean objectFromData(String str, String key) {
        try {
            return (AudioPurchaseBean) new Gson().fromJson(new JSONObject(str).getString(str), AudioPurchaseBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getUser_wallet() {
        return this.user_wallet;
    }

    public void setUser_wallet(int user_wallet) {
        this.user_wallet = user_wallet;
    }

    public int getFilters_id() {
        return this.filters_id;
    }

    public void setFilters_id(int filters_id) {
        this.filters_id = filters_id;
    }

    public int getPurchased() {
        return this.purchased;
    }

    public void setPurchased(int purchased) {
        this.purchased = purchased;
    }

    public String getZip_url() {
        return this.zip_url;
    }

    public void setZip_url(String zip_url) {
        this.zip_url = zip_url;
    }

    public ErrorBean getError() {
        return this.error;
    }

    public void setError(ErrorBean error) {
        this.error = error;
    }
}