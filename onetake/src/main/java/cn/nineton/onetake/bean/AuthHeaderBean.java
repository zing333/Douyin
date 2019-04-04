package cn.nineton.onetake.bean;

import com.google.gson.annotations.SerializedName;

public class AuthHeaderBean {
    @SerializedName("Ocp-Apim-Subscription-Key")
    private String OcpApimSubscriptionKey;

    public String getOcpApimSubscriptionKey() {
        return this.OcpApimSubscriptionKey;
    }

    public void setOcpApimSubscriptionKey(String OcpApimSubscriptionKey) {
        this.OcpApimSubscriptionKey = OcpApimSubscriptionKey;
    }
}