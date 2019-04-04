package cn.nineton.onetake.bean;

//import com.blink.academy.onetake.bean.IExceptionCallback;
//import com.blink.academy.onetake.bean.UploadFilterBean;
//import com.blink.academy.onetake.bean.UploadFilterBean.LeakBean;
//import com.blink.academy.onetake.bean.filterview.FilterEffectBean;
//import com.blink.academy.onetake.bean.utils.JsonParserUtil;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.utils.FilterEffectCompare;
//import com.blink.academy.onetake.support.utils.StaticLayoutUtil;
//import com.blink.academy.onetake.ui.activity.video.FilterActivity;
//import com.lzy.okhttputils.model.HttpHeaders;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import cn.nineton.onetake.FilterActivity;
import cn.nineton.onetake.util.FilterEffectCompare;
import cn.nineton.onetake.util.JsonParserUtil;
import cn.nineton.onetake.util.LogUtil;
import cn.nineton.onetake.widget.StaticLayoutUtil;

public class FilterEffectManager {
    private static EffectType[] HideEffectTypes;
    public static List<EffectType> VideoHideEffectTypes;
    private static HashMap<String, EffectType> effectMap;
    private List<FilterEffectBean> currentChangeEffectList;
    private List<FilterEffectBean> currentFilterBeans;
    private float exposureFileCurrentValue;
    private List<FilterEffectBean> longClickFilterBeans;

    /* renamed from: com.blink.academy.onetake.support.manager.FilterEffectManager$2 */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType = new int[EffectType.values().length];

        static {
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[EffectType.VERTICAL.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[EffectType.HORIZONTAL.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[EffectType.ROTATE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[EffectType.MIRROR.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[EffectType.CROP.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[EffectType.DATE.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[EffectType.DUST.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[EffectType.PRISM.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[EffectType.STRENGTH.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[EffectType.EXPOSURE.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[EffectType.BEAUTIFY.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[EffectType.CONTRAST.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[EffectType.SATURATION.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[EffectType.SHARPEN.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[EffectType.TEMPERATURE.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[EffectType.TINGE.ordinal()] = 16;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[EffectType.GRAIN.ordinal()] = 17;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[EffectType.TILT.ordinal()] = 18;
            } catch (NoSuchFieldError e18) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[EffectType.HIGHLIGHT.ordinal()] = 19;
            } catch (NoSuchFieldError e19) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[EffectType.SKY.ordinal()] = 20;
            } catch (NoSuchFieldError e20) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[EffectType.SHADOW.ordinal()] = 21;
            } catch (NoSuchFieldError e21) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[EffectType.FADED.ordinal()] = 22;
            } catch (NoSuchFieldError e22) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[EffectType.VIGNETTE.ordinal()] = 23;
            } catch (NoSuchFieldError e23) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[EffectType.LEAK.ordinal()] = 24;
            } catch (NoSuchFieldError e24) {
            }
        }
    }

    public enum EffectType {
        STRENGTH,
        BEAUTIFY,
        EXPOSURE,
        CONTRAST,
        SATURATION,
        TEMPERATURE,
        TINGE,
        GRAIN,
        VIGNETTE,
        LEAK,
        TILT,
        FADED,
        SHADOW,
        HIGHLIGHT,
        SKY,
        CROP,
        ROTATE,
        VERTICAL,
        HORIZONTAL,
        MIRROR,
        SHARPEN,
        DATE,
        DUST,
        PRISM,
        NONE
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: bogus opcode: 00e9 in method: com.blink.academy.onetake.support.manager.FilterEffectManager.<clinit>():void, dex: 
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:118)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:248)
        	at jadx.core.ProcessClass.process(ProcessClass.java:29)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.lang.Iterable.forEach(Unknown Source)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:293)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JavaClass.getCode(JavaClass.java:48)
        Caused by: java.lang.IllegalArgumentException: bogus opcode: 00e9
        	at com.android.dx.io.OpcodeInfo.get(OpcodeInfo.java:1217)
        	at com.android.dx.io.OpcodeInfo.getName(OpcodeInfo.java:1224)
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:595)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:79)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:104)
        	... 9 more
        */
//    static {
//        /*
//        // Can't load method instructions: Load method exception: bogus opcode: 00e9 in method: com.blink.academy.onetake.support.manager.FilterEffectManager.<clinit>():void, dex:
//        */
//        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.support.manager.FilterEffectManager.<clinit>():void");
//    }

    public static EffectType getEffectTypeByType(String type) {
        return (EffectType) effectMap.get(type);
    }

    public float getExposureFileCurrentValue() {
        return this.exposureFileCurrentValue;
    }

    public void setExposureFileCurrentValue(float exposureFileCurrentValue) {
        this.exposureFileCurrentValue = exposureFileCurrentValue;
    }

    public void getLongClickFilterBeans() {
        if (this.currentFilterBeans == null) {
            this.currentFilterBeans = new ArrayList();
        } else {
            this.currentFilterBeans.clear();
        }
        this.currentFilterBeans.addAll(this.currentChangeEffectList);
        if (this.longClickFilterBeans == null) {
            this.longClickFilterBeans = new ArrayList();
        } else {
            this.longClickFilterBeans.clear();
        }
        if (this.currentChangeEffectList != null && this.currentChangeEffectList.size() > 0) {
            for (FilterEffectBean bean : this.currentChangeEffectList) {
                switch (AnonymousClass2.$SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[bean.effectType.ordinal()]) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                        this.longClickFilterBeans.add(bean.cloneBean());
                        break;
                    default:
                        break;
                }
            }
        }
        this.currentChangeEffectList.clear();
        this.currentChangeEffectList.addAll(this.longClickFilterBeans);
    }

    public void returnSelectFilterBeans() {
        if (this.currentFilterBeans != null && this.currentFilterBeans.size() > 0) {
            this.currentChangeEffectList.clear();
            this.currentChangeEffectList.addAll(this.currentFilterBeans);
            this.currentFilterBeans.clear();
        }
    }

    public FilterEffectManager() {
        this.longClickFilterBeans = new ArrayList();
        this.currentFilterBeans = new ArrayList();
        this.exposureFileCurrentValue = StaticLayoutUtil.DefaultSpacingadd;
        this.currentChangeEffectList = new ArrayList();
    }

    public FilterEffectManager(List<FilterEffectBean> list) {
        this.longClickFilterBeans = new ArrayList();
        this.currentFilterBeans = new ArrayList();
        this.exposureFileCurrentValue = StaticLayoutUtil.DefaultSpacingadd;
        this.currentChangeEffectList = new ArrayList();
    }

    public FilterEffectBean createBean(EffectType effectType, float[] value, float[] selectPosition) {
        return new FilterEffectBean(effectType, value, selectPosition);
    }

    public FilterEffectBean getFilterEffectBeanByName(String name) {
        return getSelectEffectValues(getEffectTypeByType(name));
    }

    public static FilterEffectBean getEffectBeanByName(List<FilterEffectBean> effectTypeList, String name) {
        EffectType effectType = getEffectTypeByType(name);
        int currentSize = effectTypeList.size();
        if (currentSize != 0) {
            for (int i = 0; i < currentSize; i++) {
                if (effectType == ((FilterEffectBean) effectTypeList.get(i)).effectType) {
                    return (FilterEffectBean) effectTypeList.get(i);
                }
            }
        }
        return null;
    }

    public FilterEffectBean getSelectEffectValues(EffectType effectType) {
        int currentSize = this.currentChangeEffectList.size();
        if (currentSize != 0) {
            for (int i = 0; i < currentSize; i++) {
                if (effectType == ((FilterEffectBean) this.currentChangeEffectList.get(i)).effectType) {
                    return (FilterEffectBean) this.currentChangeEffectList.get(i);
                }
            }
        }
        return getDefaultBean(effectType);
    }

    public FilterEffectBean getModel5Bean() {
        EffectType effectType = EffectType.CROP;
        float[] fArr = new float[]{4.0f, StaticLayoutUtil.DefaultSpacingadd, 10.0f};
        float[] fArr2 = new float[1];
        fArr2[0] = StaticLayoutUtil.DefaultSpacingadd;
        return new FilterEffectBean(effectType, fArr, fArr2);
    }

    public void setCurrentListItem(FilterEffectBean bean) {
        if (this.currentChangeEffectList == null) {
            this.currentChangeEffectList = new ArrayList();
        }
        this.currentChangeEffectList.add(bean);
    }

    public void setDraftFilterData(List<FilterEffectBean> mList) {
        if (this.currentChangeEffectList == null) {
            this.currentChangeEffectList = new ArrayList();
        }
        if (mList != null) {
            this.currentChangeEffectList.addAll(mList);
        }
    }

    public void changeFilterEffectList(List<FilterEffectBean> mList) {
        if (this.currentChangeEffectList == null) {
            this.currentChangeEffectList = new ArrayList();
        }
        this.currentChangeEffectList.clear();
        if (mList != null) {
            this.currentChangeEffectList.addAll(mList);
        }
    }

    public boolean setSelectEffect(FilterEffectBean filterEffectBean) {
        int size = this.currentChangeEffectList.size();
        boolean isHasSelect = false;
        for (int i = 0; i < size; i++) {
            if (filterEffectBean.effectType == ((FilterEffectBean) this.currentChangeEffectList.get(i)).effectType) {
                this.currentChangeEffectList.set(i, filterEffectBean);
                isHasSelect = true;
                break;
            }
        }
        if (!isHasSelect) {
            this.currentChangeEffectList.add(filterEffectBean);
        }
        return filterEffectBean.getCurrentTypeSelected(filterEffectBean.effectType, filterEffectBean.value, filterEffectBean.selectPosition);
    }

    public List<FilterEffectBean> copyCurrentList() {
        List<FilterEffectBean> filterEffectBeanList = new ArrayList();
        filterEffectBeanList.addAll(getCurrentList());
        return filterEffectBeanList;
    }

    public List<FilterEffectBean> getCurrentList() {
        if (this.currentChangeEffectList == null) {
            this.currentChangeEffectList = new ArrayList();
        }
        return this.currentChangeEffectList;
    }

    public FilterEffectBean getFilterEffectBeanByEffectType(List<FilterEffectBean> list, EffectType effectType) {
        if (list != null && list.size() > 0) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                FilterEffectBean bean = (FilterEffectBean) list.get(i);
                if (bean.effectType == effectType) {
                    return bean;
                }
            }
        }
        return getDefaultBean(effectType);
    }

    public FilterEffectBean getCurrentListItem(EffectType effectType) {
        if (this.currentChangeEffectList != null && this.currentChangeEffectList.size() > 0) {
            int size = this.currentChangeEffectList.size();
            for (int i = 0; i < size; i++) {
                FilterEffectBean bean = (FilterEffectBean) this.currentChangeEffectList.get(i);
                if (bean.effectType == effectType) {
                    return bean;
                }
            }
        }
        return getDefaultBean(effectType);
    }

    private String value2String(float value) {
        return value >= StaticLayoutUtil.DefaultSpacingadd ? "+" + value : "" + value;
    }

    public String getUploadFilterInfoString(EffectType currentEffectType, float currentVlaue, String filterMode) {
        String filterInfoString = "";
        List<FilterEffectBean> showFilterEffectList = getShowFilterEffectList(currentEffectType);
        List<UploadFilterBean> mFilterInfoList = new ArrayList();
        int size = showFilterEffectList.size();
        for (int i = 0; i < size; i++) {
            FilterEffectBean bean = (FilterEffectBean) showFilterEffectList.get(i);
            switch (AnonymousClass2.$SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[bean.effectType.ordinal()]) {
                case 4:
                    mFilterInfoList.add(new UploadFilterBean("Mirror", value2String(bean.value[0]), getMirroMode(bean.selectPosition[0])));
                    break;
                case 6:
                    //mFilterInfoList.add(new UploadFilterBean(HttpHeaders.HEAD_KEY_DATE, value2String(currentVlaue), true));
                    break;
                case 8:
                    mFilterInfoList.add(new UploadFilterBean("Prism", value2String(currentVlaue), true));
                    break;
                case 9:
                    mFilterInfoList.add(new UploadFilterBean("Preset", value2String(currentVlaue), filterMode));
                    break;
                case 10:
                    mFilterInfoList.add(new UploadFilterBean("Exposure", value2String(bean.value[1]), true));
                    break;
                case 11:
                    mFilterInfoList.add(new UploadFilterBean("Beauty", value2String(bean.value[1]), true));
                    break;
                case 12:
                    mFilterInfoList.add(new UploadFilterBean("Contrast", value2String(bean.value[1]), true));
                    break;
                case 13:
                    mFilterInfoList.add(new UploadFilterBean("Saturation", value2String(bean.value[1]), true));
                    break;
                case 14:
                    mFilterInfoList.add(new UploadFilterBean("Sharpen", value2String(bean.value[1]), true));
                    break;
                case 15:
                    mFilterInfoList.add(new UploadFilterBean("WhiteBalance", value2String(bean.value[1]), true));
                    break;
                case 16:
                    mFilterInfoList.add(new UploadFilterBean("Tint", value2String(bean.value[1]), true));
                    break;
                case 17:
                    mFilterInfoList.add(new UploadFilterBean("Grains", value2String(bean.value[1]), "400"));
                    break;
                case 18:
                    mFilterInfoList.add(new UploadFilterBean("Tilt", value2String(bean.value[1]), true));
                    break;
                case 19:
                    mFilterInfoList.add(new UploadFilterBean("HighlightsSave", value2String(bean.value[1]), true));
                    break;
                case 20:
                    mFilterInfoList.add(new UploadFilterBean("Sky", value2String(bean.value[1]), true));
                    break;
                case 21:
                    mFilterInfoList.add(new UploadFilterBean("ShadowsSave", value2String(bean.value[1]), true));
                    break;
                case 22:
                    mFilterInfoList.add(new UploadFilterBean("Fade", value2String(bean.value[1]), true));
                    break;
                case 23:
                    mFilterInfoList.add(new UploadFilterBean("VIGNETTE", value2String(bean.value[1]), getVignetteMode(bean.selectPosition[0])));
                    break;
                case 24:
//                    String value2String = value2String(bean.value[1]);
//                    String leakMode = getLeakMode(bean.selectPosition[0]);
//                    UploadFilterBean uploadFilterBean = new UploadFilterBean();
//                    uploadFilterBean.getClass();
//                    mFilterInfoList.add(new UploadFilterBean("Leak", value2String, leakMode, new UploadFilterBean.LeakBean(uploadFilterBean, false, false)));
                    break;
                default:
                    break;
            }
        }
        return JsonParserUtil.fromList(mFilterInfoList, new IExceptionCallback() {
            public void doException() {
                LogUtil.d("HAHA", "WRONG");
            }
        });
    }

    private String getLeakMode(float value) {
        String mode = "01";
        if (value == StaticLayoutUtil.DefaultSpacingadd) {
            return "01";
        }
        if (value == 1.0f) {
            return "02";
        }
        if (value == 2.0f) {
            return "03";
        }
        if (value == 3.0f) {
            return "04";
        }
        return mode;
    }

    private String getVignetteMode(float value) {
        return "Camera";
    }

    private String getMirroMode(float value) {
        String mode = FilterActivity.NONE_FILTER;
        if (value == StaticLayoutUtil.DefaultSpacingadd) {
            return FilterActivity.NONE_FILTER;
        }
        if (value == 1.0f) {
            return "Top";
        }
        if (value == 2.0f) {
            return "Down";
        }
        if (value == 3.0f) {
            return "Left";
        }
        if (value == 4.0f) {
            return "Right";
        }
        return mode;
    }

    public List<FilterEffectBean> getSpecialShowFilterEffectList(EffectType currentEffectType) {
        List mList = new ArrayList();
        if (!(this.currentChangeEffectList == null || this.currentChangeEffectList.size() == 0)) {
            for (int i = 0; i < this.currentChangeEffectList.size(); i++) {
                mList.add((FilterEffectBean) this.currentChangeEffectList.get(i));
            }
        }
        if (!(currentEffectType == EffectType.NONE || listAContainItem(mList, currentEffectType))) {
            mList.add(getDefaultBean(currentEffectType));
        }
        if (!listAContainItem(mList, EffectType.STRENGTH)) {
            mList.add(getDefaultBean(EffectType.STRENGTH));
        }
        return mList;
    }

    public List<FilterEffectBean> getShowFilterEffectList(EffectType currentEffectType) {
        List mList = new ArrayList();
        if (!(this.currentChangeEffectList == null || this.currentChangeEffectList.size() == 0)) {
            for (int i = 0; i < this.currentChangeEffectList.size(); i++) {
                FilterEffectBean bean = (FilterEffectBean) this.currentChangeEffectList.get(i);
                if (bean.getItemSelect()) {
                    mList.add(bean);
                }
            }
        }
        if (!(currentEffectType == EffectType.NONE || listAContainItem(mList, currentEffectType))) {
            mList.add(getDefaultBean(currentEffectType));
        }
        if (!listAContainItem(mList, EffectType.STRENGTH)) {
            mList.add(getDefaultBean(EffectType.STRENGTH));
        }
        return mList;
    }

    public boolean hasDateEffect(EffectType type) {
        List<FilterEffectBean> mList = getShowFilterEffectList(type);
        Collections.sort(mList, new FilterEffectCompare());
        for (FilterEffectBean bean : mList) {
            if (EffectType.DATE == bean.effectType) {
                return bean.getCurrentTypeSelected(bean.effectType, bean.value, bean.selectPosition);
            }
        }
        return false;
    }

    public FilterEffectBean getAssignedBeanByType(EffectType effectType) {
        List<FilterEffectBean> beanList = getShowFilterEffectList(EffectType.NONE);
        int size = beanList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                FilterEffectBean bean = (FilterEffectBean) beanList.get(i);
                if (bean.effectType == effectType) {
                    return bean;
                }
            }
        }
        beanList.clear();
        return getDefaultBean(effectType);
    }

    public boolean listAContainItem(List<FilterEffectBean> mList, FilterEffectBean bean) {
        if (mList == null || mList.size() == 0) {
            return false;
        }
        int size = mList.size();
        for (int i = 0; i < size; i++) {
            if (bean.effectType == ((FilterEffectBean) mList.get(i)).effectType) {
                return true;
            }
        }
        return false;
    }

    public boolean listAcontainTransform(List<FilterEffectBean> mList) {
        if (mList == null || mList.size() == 0) {
            return false;
        }
        int i = 0;
        int size = mList.size();
        while (i < size) {
            if (EffectType.HORIZONTAL == ((FilterEffectBean) mList.get(i)).effectType || EffectType.VERTICAL == ((FilterEffectBean) mList.get(i)).effectType || EffectType.ROTATE == ((FilterEffectBean) mList.get(i)).effectType) {
                return true;
            }
            i++;
        }
        return false;
    }

    public boolean listAContainItem(List<FilterEffectBean> mList, EffectType type) {
        if (mList == null || mList.size() == 0) {
            return false;
        }
        int size = mList.size();
        for (int i = 0; i < size; i++) {
            if (type == ((FilterEffectBean) mList.get(i)).effectType) {
                return true;
            }
        }
        return false;
    }

    public void clearCurrentFilterEffect() {
        FilterEffectBean beautifyEffectValues = getSelectEffectValues(EffectType.BEAUTIFY);
        FilterEffectBean cropEffectValues = getSelectEffectValues(EffectType.CROP);
        FilterEffectBean rotateEffectValues = getSelectEffectValues(EffectType.ROTATE);
        FilterEffectBean verticalEffectValues = getSelectEffectValues(EffectType.VERTICAL);
        FilterEffectBean horizontalEffectValues = getSelectEffectValues(EffectType.HORIZONTAL);
        FilterEffectBean mirrorEffectValues = getSelectEffectValues(EffectType.MIRROR);
        this.currentChangeEffectList.clear();
        if (beautifyEffectValues.getItemSelect()) {
            this.currentChangeEffectList.add(beautifyEffectValues);
        }
        if (cropEffectValues.getItemSelect()) {
            this.currentChangeEffectList.add(cropEffectValues);
        }
        if (rotateEffectValues.getItemSelect()) {
            this.currentChangeEffectList.add(rotateEffectValues);
        }
        if (verticalEffectValues.getItemSelect()) {
            this.currentChangeEffectList.add(verticalEffectValues);
        }
        if (horizontalEffectValues.getItemSelect()) {
            this.currentChangeEffectList.add(horizontalEffectValues);
        }
        if (mirrorEffectValues.getItemSelect()) {
            this.currentChangeEffectList.add(mirrorEffectValues);
        }
    }

    public void masterCancelFilterEffect() {
        this.currentChangeEffectList.clear();
    }

    public FilterEffectBean getStrengthBean() {
        return getSelectEffectValues(EffectType.STRENGTH);
    }

    public FilterEffectBean getFilterEffectBeanById(int viewId) {
        switch (viewId) {
            case 2131691101:
                return getSelectEffectValues(EffectType.BEAUTIFY);
            case 2131691102:
                return getSelectEffectValues(EffectType.EXPOSURE);
            case 2131691103:
                return getSelectEffectValues(EffectType.CONTRAST);
            case 2131691104:
                return getSelectEffectValues(EffectType.SATURATION);
            case 2131691105:
                return getSelectEffectValues(EffectType.TEMPERATURE);
            case 2131691106:
                return getSelectEffectValues(EffectType.TINGE);
            case 2131691107:
                return getSelectEffectValues(EffectType.GRAIN);
            case 2131691108:
                return getSelectEffectValues(EffectType.DUST);
            case 2131691109:
                return getSelectEffectValues(EffectType.VIGNETTE);
            case 2131691110:
                return getSelectEffectValues(EffectType.LEAK);
            case 2131691111:
                return getSelectEffectValues(EffectType.DATE);
            case 2131691112:
                return getSelectEffectValues(EffectType.SHARPEN);
            case 2131691113:
                return getSelectEffectValues(EffectType.TILT);
            case 2131691114:
                return getSelectEffectValues(EffectType.PRISM);
            case 2131691115:
                return getSelectEffectValues(EffectType.FADED);
            case 2131691116:
                return getSelectEffectValues(EffectType.HIGHLIGHT);
            case 2131691117:
                return getSelectEffectValues(EffectType.SHADOW);
            case 2131691118:
                return getSelectEffectValues(EffectType.SKY);
            case 2131691119:
                return getSelectEffectValues(EffectType.CROP);
            case 2131691120:
                return getSelectEffectValues(EffectType.ROTATE);
            case 2131691121:
                return getSelectEffectValues(EffectType.VERTICAL);
            case 2131691122:
                return getSelectEffectValues(EffectType.HORIZONTAL);
            case 2131691123:
                return getSelectEffectValues(EffectType.MIRROR);
            default:
                return null;
        }
    }

    public List<FilterEffectBean> getCurrentSelectEffectTypeList() {
        if (this.currentChangeEffectList == null) {
            this.currentChangeEffectList = new ArrayList();
        }
        return this.currentChangeEffectList;
    }

    public FilterEffectBean getFrontBeautyBean(boolean isFront) {
        float defaultValue = isFront ? 5.0f : StaticLayoutUtil.DefaultSpacingadd;
        EffectType effectType = EffectType.BEAUTIFY;
        float[] fArr = new float[3];
        fArr[0] = StaticLayoutUtil.DefaultSpacingadd;
        fArr[1] = defaultValue;
        fArr[2] = 10.0f;
        float[] fArr2 = new float[1];
        fArr2[0] = StaticLayoutUtil.DefaultSpacingadd;
        return new FilterEffectBean(effectType, fArr, fArr2);
    }

    public FilterEffectBean getDefaultBean(EffectType effectType) {
        float[] fArr;
        float[] fArr2;
        switch (AnonymousClass2.$SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[effectType.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 10:
            case 12:
            case 13:
            case 15:
            case 16:
                fArr = new float[]{-5.0f, StaticLayoutUtil.DefaultSpacingadd, 5.0f};
                fArr2 = new float[1];
                fArr2[0] = StaticLayoutUtil.DefaultSpacingadd;
                return new FilterEffectBean(effectType, fArr, fArr2);
            case 4:
                fArr = new float[]{StaticLayoutUtil.DefaultSpacingadd, 10.0f, 10.0f};
                fArr2 = new float[1];
                fArr2[0] = 50.0f;
                return new FilterEffectBean(effectType, fArr, fArr2);
            case 5:
                fArr = new float[]{StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 10.0f};
                fArr2 = new float[1];
                fArr2[0] = StaticLayoutUtil.DefaultSpacingadd;
                return new FilterEffectBean(effectType, fArr, fArr2);
            case 6:
                fArr = new float[]{StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 10.0f};
                fArr2 = new float[1];
                fArr2[0] = StaticLayoutUtil.DefaultSpacingadd;
                return new FilterEffectBean(effectType, fArr, fArr2);
            case 7:
            case 24:
                return new FilterEffectBean(effectType, new float[]{StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 10.0f}, new float[]{StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd});
            case 8:
            case 11:
            case 14:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
                fArr = new float[]{StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 10.0f};
                fArr2 = new float[1];
                fArr2[0] = StaticLayoutUtil.DefaultSpacingadd;
                return new FilterEffectBean(effectType, fArr, fArr2);
            case 9:
                fArr = new float[]{StaticLayoutUtil.DefaultSpacingadd, 10.0f, 10.0f};
                fArr2 = new float[1];
                fArr2[0] = StaticLayoutUtil.DefaultSpacingadd;
                return new FilterEffectBean(effectType, fArr, fArr2);
            case 23:
                fArr = new float[]{StaticLayoutUtil.DefaultSpacingadd, StaticLayoutUtil.DefaultSpacingadd, 10.0f};
                fArr2 = new float[1];
                fArr2[0] = 1.0f;
                return new FilterEffectBean(effectType, fArr, fArr2);
            default:
                return null;
        }
    }
}