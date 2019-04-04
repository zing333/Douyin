package cn.nineton.onetake.bean;

//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.R;
//import com.blink.academy.onetake.support.manager.FilterEffectManager.EffectType;
import java.util.Arrays;

import cn.nineton.onetake.App;
import cn.nineton.onetake.R;

public class FilterEffectBean {
    public static final int SHOW_TYPE_BUTTON = 1;
    public static final int SHOW_TYPE_NONE = 3;
    public static final int SHOW_TYPE_SEEK = 0;
    public FilterEffectManager.EffectType effectType;
    public Integer filterLocation;
    private boolean isItemSelect;
    public float[] selectPosition;
    public float[] value;

    /* renamed from: com.blink.academy.onetake.bean.filterview.FilterEffectBean$1 */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType = new int[FilterEffectManager.EffectType.values().length];

        static {
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[FilterEffectManager.EffectType.STRENGTH.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[FilterEffectManager.EffectType.BEAUTIFY.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[FilterEffectManager.EffectType.EXPOSURE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[FilterEffectManager.EffectType.CONTRAST.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[FilterEffectManager.EffectType.SATURATION.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[FilterEffectManager.EffectType.TEMPERATURE.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[FilterEffectManager.EffectType.TINGE.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[FilterEffectManager.EffectType.HORIZONTAL.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[FilterEffectManager.EffectType.VERTICAL.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[FilterEffectManager.EffectType.ROTATE.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[FilterEffectManager.EffectType.SKY.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[FilterEffectManager.EffectType.HIGHLIGHT.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[FilterEffectManager.EffectType.SHADOW.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[FilterEffectManager.EffectType.FADED.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[FilterEffectManager.EffectType.GRAIN.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[FilterEffectManager.EffectType.DATE.ordinal()] = 16;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[FilterEffectManager.EffectType.DUST.ordinal()] = 17;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[FilterEffectManager.EffectType.PRISM.ordinal()] = 18;
            } catch (NoSuchFieldError e18) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[FilterEffectManager.EffectType.TILT.ordinal()] = 19;
            } catch (NoSuchFieldError e19) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[FilterEffectManager.EffectType.SHARPEN.ordinal()] = 20;
            } catch (NoSuchFieldError e20) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[FilterEffectManager.EffectType.VIGNETTE.ordinal()] = 21;
            } catch (NoSuchFieldError e21) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[FilterEffectManager.EffectType.LEAK.ordinal()] = 22;
            } catch (NoSuchFieldError e22) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[FilterEffectManager.EffectType.CROP.ordinal()] = 23;
            } catch (NoSuchFieldError e23) {
            }
            try {
                $SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[FilterEffectManager.EffectType.MIRROR.ordinal()] = 24;
            } catch (NoSuchFieldError e24) {
            }
        }
    }

    FilterEffectBean() {
    }

    public FilterEffectBean clone() {
        FilterEffectBean bean = new FilterEffectBean();
        bean.effectType = this.effectType;
        bean.isItemSelect = this.isItemSelect;
        bean.value = this.value;
        bean.selectPosition = (float[]) this.selectPosition.clone();
        bean.filterLocation = this.filterLocation;
        return bean;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FilterEffectBean that = (FilterEffectBean) o;
        if (this.isItemSelect != that.isItemSelect || this.effectType != that.effectType || !Arrays.equals(this.value, that.value) || !Arrays.equals(this.selectPosition, that.selectPosition)) {
            return false;
        }
        if (this.filterLocation != null) {
            z = this.filterLocation.equals(that.filterLocation);
        } else if (that.filterLocation != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2 = 0;
        if (this.effectType != null) {
            result = this.effectType.hashCode();
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (this.isItemSelect) {
            i = 1;
        } else {
            i = 0;
        }
        i = (((((i3 + i) * 31) + Arrays.hashCode(this.value)) * 31) + Arrays.hashCode(this.selectPosition)) * 31;
        if (this.filterLocation != null) {
            i2 = this.filterLocation.hashCode();
        }
        return i + i2;
    }

    public FilterEffectBean(FilterEffectManager.EffectType effectType, boolean isItemSelect, float[] value, float[] selectPosition, Integer filterLocation) {
        this.effectType = effectType;
        this.isItemSelect = isItemSelect;
        this.value = value;
        this.selectPosition = selectPosition;
        this.filterLocation = filterLocation;
    }

    public FilterEffectBean cloneBean() {
        return new FilterEffectBean(this.effectType, this.isItemSelect, this.value, this.selectPosition, this.filterLocation);
    }

    public String toString() {
        return "FilterEffectBean{effectType=" + this.effectType + ", isItemSelect=" + this.isItemSelect + ", value=" + Arrays.toString(this.value) + ", selectPosition=" + Arrays.toString(this.selectPosition) + '}';
    }

    public FilterEffectBean(FilterEffectManager.EffectType effectType, float[] value, float[] selectPosition) {
        this.effectType = effectType;
        this.value = value;
        this.selectPosition = selectPosition;
        this.isItemSelect = getCurrentTypeSelected(effectType, value, selectPosition);
        this.filterLocation = getFilterLocation(effectType);
    }

    private Integer getFilterLocation(FilterEffectManager.EffectType effectType) {
        int filterLocation = -1;
        switch (AnonymousClass1.$SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[effectType.ordinal()]) {
            case 1:
                filterLocation = 23;
                break;
            case 2:
                filterLocation = 0;
                break;
            case 3:
                filterLocation = 16;
                break;
            case 4:
                filterLocation = 15;
                break;
            case 5:
                filterLocation = 18;
                break;
            case 6:
                filterLocation = 17;
                break;
            case 7:
                filterLocation = 22;
                break;
            case 8:
                filterLocation = 3;
                break;
            case 9:
                filterLocation = 4;
                break;
            case 10:
                filterLocation = 2;
                break;
            case 11:
                filterLocation = 6;
                break;
            case 12:
                filterLocation = 20;
                break;
            case 13:
                filterLocation = 19;
                break;
            case 14:
                filterLocation = 21;
                break;
            case 15:
                filterLocation = 11;
                break;
            case 16:
                filterLocation = 12;
                break;
            case 17:
                filterLocation = 13;
                break;
            case 18:
                filterLocation = 8;
                break;
            case 19:
                filterLocation = 7;
                break;
            case 20:
                filterLocation = 14;
                break;
            case 21:
                filterLocation = 9;
                break;
            case 22:
                filterLocation = 10;
                break;
            case 23:
                filterLocation = 1;
                break;
            case 24:
                filterLocation = 5;
                break;
        }
        return Integer.valueOf(filterLocation);
    }

    public boolean getItemSelect() {
        return getCurrentTypeSelected(this.effectType, this.value, this.selectPosition);
    }

    public String getFilterEffectBeanString() {
        return getCurrentTypeString(this.effectType);
    }

    public boolean isCenterAdsorb() {
        return getIsAdsorb(this.effectType);
    }

    public int getShowType() {
        return getEffectShowType(this.effectType);
    }

    private int getEffectShowType(FilterEffectManager.EffectType effectType) {
        switch (AnonymousClass1.$SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[effectType.ordinal()]) {
            case 16:
                return 3;
            case 23:
            case 24:
                return 1;
            default:
                return 0;
        }
    }

    private boolean getIsAdsorb(FilterEffectManager.EffectType effectType) {
        switch (AnonymousClass1.$SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[effectType.ordinal()]) {
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                return true;
            default:
                return false;
        }
    }

    private String getCurrentTypeString(FilterEffectManager.EffectType effectType) {
        String filterEffectTypeString = "";
        switch (AnonymousClass1.$SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[effectType.ordinal()]) {
            case 1:
                return App.getContext().getString(R.string.LABEL_EFFECT_STRENGTH);
            case 2:
                return App.getContext().getString(R.string.BUTTON_EFFECT_BEAUTY);
            case 3:
                return App.getContext().getString(R.string.LABEL_EFFECT_EXPOSURE);
            case 4:
                return App.getContext().getString(R.string.LABEL_EFFECT_CONTRAST);
            case 5:
                return App.getContext().getString(R.string.LABEL_EFFECT_SATURATION);
            case 6:
                return App.getContext().getString(R.string.LABEL_EFFECT_WHITE_BALANCE);
            case 7:
                return App.getContext().getString(R.string.LABEL_EFFECT_TINT);
            case 8:
                return App.getContext().getString(R.string.LABEL_EFFECT_HORIZONTAL_PERSPECTIVE);
            case 9:
                return App.getContext().getString(R.string.LABEL_EFFECT_VERTITAL_PERSPECTIVE);
            case 10:
                return App.getContext().getString(R.string.LABEL_EFFECT_ROTATE);
            case 11:
                return App.getContext().getString(R.string.LABEL_EFFECT_SKY);
            case 12:
                return App.getContext().getString(R.string.LABEL_EFFECT_HIGHTLIGHTS_SAVE);
            case 13:
                return App.getContext().getString(R.string.LABEL_EFFECT_SHADOWS_SAVE);
            case 14:
                return App.getContext().getString(R.string.LABEL_EFFECT_FADE);
            case 15:
                return App.getContext().getString(R.string.LABEL_EFFECT_GRAINS);
            case 16:
                return App.getContext().getString(R.string.LABEL_EFFECT_DATE);
            case 17:
                return App.getContext().getString(R.string.LABEL_EFFECT_DUST);
            case 18:
                return App.getContext().getString(R.string.LABEL_EFFECT_PRISM);
            case 19:
                return App.getContext().getString(R.string.LABEL_EFFECT_TILT);
            case 20:
                return App.getContext().getString(R.string.BUTTON_EFFECT_SHARPEN);
            case 21:
                return App.getContext().getString(R.string.LABEL_EFFECT_VIGNETTE);
            case 22:
                return App.getContext().getString(R.string.LABEL_EFFECT_LIGHT_LEAK);
            case 23:
                return App.getContext().getString(R.string.LABEL_EFFECT_CROP);
            case 24:
                return App.getContext().getString(R.string.LABEL_EFFECT_MIRROR);
            default:
                return filterEffectTypeString;
        }
    }

    public boolean getCurrentTypeSelected(FilterEffectManager.EffectType effectType, float[] value, float[] selectPosition) {
        switch (AnonymousClass1.$SwitchMap$com$blink$academy$onetake$support$manager$FilterEffectManager$EffectType[effectType.ordinal()]) {
            case 1:
                if (value[1] != 10.0f) {
                    return true;
                }
                return false;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 19:
            case 20:
            case 21:
                if (value[1] != 0.0f) {
                    return true;
                }
                return false;
            case 10:
                if (value[1] == 0.0f && selectPosition[0] == 0.0f) {
                    return false;
                }
                return true;
            case 16:
                return value[1] != 0.0f;
            case 17:
                if (value[1] != 0.0f) {
                    return true;
                }
                return false;
            case 18:
                if (value[1] != 0.0f) {
                    return true;
                }
                return false;
            case 22:
                if (value[1] != 0.0f) {
                    return true;
                }
                return false;
            case 23:
            case 24:
                if (value[0] != 0.0f) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }
}