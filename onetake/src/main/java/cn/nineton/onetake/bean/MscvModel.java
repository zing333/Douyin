package cn.nineton.onetake.bean;

import com.android.volley.VolleyError;
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.bean.error.ErrorBean;
//import com.blink.academy.onetake.bean.mscv.MscvKeyBean;
//import com.blink.academy.onetake.bean.tag.OfficialTagBean;
//import com.blink.academy.onetake.controller.MSCVController;
//import com.blink.academy.onetake.support.callbacks.IControllerCallback;
//import com.blink.academy.onetake.support.events.OfficialTagListEvent;
//import com.blink.academy.onetake.support.helper.LBSHelper;
//import com.blink.academy.onetake.support.manager.GlobalLocationManager;
//import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import cn.nineton.onetake.App;
import cn.nineton.onetake.controller.MSCVController;
import cn.nineton.onetake.event.OfficialTagListEvent;
import cn.nineton.onetake.listener.IControllerCallback;

public class MscvModel {
    private static MscvModel sMscvModel;
    private JSONObject mAMapGeoCoding;
    private JSONObject mAccuMapGeoCoding;
    private List<OfficialTagBean> mExifTagsList;
    private HashMap<String, JSONObject> mGMapGeoCoding;
    private List<OfficialTagBean> mGpsRecTagList;
    private MscvKeyBean mMscvKeyBean;
    private List<OfficialTagBean> mMscvRecTagList;
    private JSONObject mMscvResultObject;
    private List<OfficialTagBean> mNetTagBeanArrayList;
    private List<OfficialTagBean> mPromoteTagsList;

    public static MscvModel getInstance() {
        if (sMscvModel == null) {
            sMscvModel = new MscvModel();
        }
        return sMscvModel;
    }

    public List<OfficialTagBean> getNetTagBeanArrayList() {
        return this.mNetTagBeanArrayList;
    }

    public void getRecommendCategories(int vType) {
        MSCVController.getRecommendCategories(vType, new IControllerCallback<List<OfficialTagBean>>() {
            public void success(final List<OfficialTagBean> officialTagBeen, String jsonContent, long cursorId, boolean isAllDone) {
                super.success(officialTagBeen, jsonContent, cursorId, isAllDone);
                App.runOnUiThread(new Runnable() {
                    public void run() {
                        MscvModel.this.setNetTagBeanArrayList(officialTagBeen);
                        EventBus.getDefault().post(new OfficialTagListEvent(4));
                    }
                });
            }

            public void error(ErrorBean error) {
                super.error(error);
                App.runOnUiThread(new Runnable() {
                    public void run() {
                        MscvModel.this.setNetTagBeanArrayList(new ArrayList());
                        EventBus.getDefault().post(new OfficialTagListEvent(4));
                    }
                });
            }

            public void failure(VolleyError error) {
                super.failure(error);
                App.runOnUiThread(new Runnable() {
                    public void run() {
                        MscvModel.this.setNetTagBeanArrayList(new ArrayList());
                        EventBus.getDefault().post(new OfficialTagListEvent(4));
                    }
                });
            }
        });
    }

    private void setNetTagBeanArrayList(List<OfficialTagBean> netTagBeanArrayList) {
        if (netTagBeanArrayList == null) {
            netTagBeanArrayList = new ArrayList();
        }
        this.mNetTagBeanArrayList = netTagBeanArrayList;
    }

    public List<OfficialTagBean> getExifTagsList() {
        return this.mExifTagsList;
    }

    public void setExifTagsList(List<OfficialTagBean> exifTagsList) {
        if (exifTagsList != null) {
            for (int i = exifTagsList.size() - 1; i >= 0; i--) {
                OfficialTagBean bean = (OfficialTagBean) exifTagsList.get(i);
                if (bean.isNotValid()) {
                    exifTagsList.remove(bean);
                }
            }
        }
        this.mExifTagsList = exifTagsList;
    }

    public List<OfficialTagBean> getPromoteTagsList() {
        return this.mPromoteTagsList;
    }

    public void setPromoteTagsList(List<OfficialTagBean> promoteTagsList) {
        if (promoteTagsList != null) {
            for (int i = promoteTagsList.size() - 1; i >= 0; i--) {
                OfficialTagBean bean = (OfficialTagBean) promoteTagsList.get(i);
                if (bean.isNotValid()) {
                    promoteTagsList.remove(bean);
                }
            }
        }
        this.mPromoteTagsList = promoteTagsList;
    }

    public List<OfficialTagBean> getMscvRecTagList() {
        return this.mMscvRecTagList;
    }

    public List<OfficialTagBean> getGpsRecTagList() {
        return this.mGpsRecTagList;
    }

    public void setMscvRecTagList(List<OfficialTagBean> mscvRecTagList) {
        if (mscvRecTagList != null) {
            for (int i = mscvRecTagList.size() - 1; i >= 0; i--) {
                OfficialTagBean bean = (OfficialTagBean) mscvRecTagList.get(i);
                if (bean.isNotValid()) {
                    mscvRecTagList.remove(bean);
                }
            }
        }
        this.mMscvRecTagList = mscvRecTagList;
    }

    public void setGpsRecTagList(List<OfficialTagBean> gpsRecTagList) {
        if (gpsRecTagList != null) {
            for (int i = gpsRecTagList.size() - 1; i >= 0; i--) {
                OfficialTagBean bean = (OfficialTagBean) gpsRecTagList.get(i);
                if (bean.isNotValid()) {
                    gpsRecTagList.remove(bean);
                }
            }
        }
        this.mGpsRecTagList = gpsRecTagList;
    }

    public String getLatlngRadius() {
//        if (GlobalLocationManager.getInstance().getPointUsedToGetLBS() == null) {
//            return String.format("0,0:0", new Object[0]);
//        }
//        Float accuracy = GlobalLocationManager.getInstance().getAccuracy();
//        return String.format("%s,%s:%s", new Object[]{Double.valueOf(pointUsedToGetLBS.getLatitude()), Double.valueOf(pointUsedToGetLBS.getLongitude()), String.valueOf(accuracy)});
        return String.format("%s,%s:%s",1,2,3);
    }

    public void setAMapGeoCoding(JSONObject AMapGeoCoding) {
        this.mAMapGeoCoding = AMapGeoCoding;
        MSCVController.postAMapgeoGetOfficialTag(this.mAMapGeoCoding, getLatlngRadius());
    }

    public JSONObject getAMapGeoCoding() {
        return this.mAMapGeoCoding;
    }

    public void setAccuMapGeoCoding(JSONObject accuMapGeoCoding) {
        this.mAccuMapGeoCoding = accuMapGeoCoding;
        MSCVController.postAccuMapgeoGetOfficialTag(this.mAccuMapGeoCoding, getLatlngRadius());
    }

    public JSONObject getAccuMapGeoCoding() {
        return this.mAccuMapGeoCoding;
    }

    public JSONObject getGMapGeoCoding(String language) {
        if (this.mGMapGeoCoding == null) {
            return null;
        }
        return (JSONObject) this.mGMapGeoCoding.get(language);
    }

    public void saveGMapGeocoding(String language, JSONObject object) {
        if (this.mGMapGeoCoding == null) {
            this.mGMapGeoCoding = new HashMap();
        }
        this.mGMapGeoCoding.put(language, object);
        if (this.mGMapGeoCoding.size() == 2) {
            MSCVController.postGooglegeoGetOfficialTag((JSONObject) this.mGMapGeoCoding.get("en"), (JSONObject) this.mGMapGeoCoding.get("zh-CN"), getLatlngRadius());
        }
    }

    public MscvKeyBean getMscvKeyBean() {
        return this.mMscvKeyBean;
    }

    public void setMscvKeyBean(MscvKeyBean mscvKeyBean) {
        this.mMscvKeyBean = mscvKeyBean;
    }

    public void setMscvResultObject(JSONObject mscvResultObject) {
        this.mMscvResultObject = mscvResultObject;
        MSCVController.postRecognitionGetOfficialTag(this.mMscvResultObject);
    }

    public JSONObject getMscvResultObject() {
        return this.mMscvResultObject;
    }

    public void clear() {
        this.mMscvKeyBean = null;
        this.mMscvResultObject = null;
        this.mGMapGeoCoding = null;
        this.mAMapGeoCoding = null;
        this.mGpsRecTagList = null;
        this.mMscvRecTagList = null;
        this.mPromoteTagsList = null;
        this.mNetTagBeanArrayList = null;
    }

    public void clearGPSbody() {
        this.mGMapGeoCoding = null;
        this.mAMapGeoCoding = null;
        this.mGpsRecTagList = null;
    }
}