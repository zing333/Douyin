package cn.nineton.onetake.bean;

import java.util.List;

public class ProxyFileSaveInfos {
    List<ProxySaveBean> proxySaveBeans;

    public ProxyFileSaveInfos(List<ProxySaveBean> proxySaveBeans) {
        this.proxySaveBeans = proxySaveBeans;
    }

    public List<ProxySaveBean> getProxySaveBeans() {
        return this.proxySaveBeans;
    }

    public void setProxySaveBeans(List<ProxySaveBean> proxySaveBeans) {
        this.proxySaveBeans = proxySaveBeans;
    }
}