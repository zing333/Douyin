package cn.nineton.onetake.util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import cn.nineton.onetake.App;
import cn.nineton.onetake.bean.VTFontDesBean;
import cn.nineton.onetake.widget.AvenirNextRegularButton;
import cn.nineton.onetake.widget.AvenirNextRegularTextView;

public class FontsUtil {
    public static Typeface setARegularTypeFace() {
        return App.getRegularTypeface();
    }

    public static Typeface setAMediumTypeFace() {
        return App.getMediumTypeface();
    }

    public static Typeface setRobotoMediumTypeFace() {
        Typeface typeface = Typeface.create("Roboto-Medium", Typeface.NORMAL);
        return typeface != null ? typeface : Typeface.DEFAULT;
    }

    public static Typeface setRobotoBoldTypeFace() {
        Typeface typeface = Typeface.create("Roboto-Medium", Typeface.BOLD);
        return typeface != null ? typeface : Typeface.DEFAULT_BOLD;
    }

    public static Typeface setARegularTypeFace(Context context) {
        return Typeface.DEFAULT;
    }

    public static Typeface setAMediumTypeFace(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/avenirnextregular.ttf");
    }

    public static Typeface setALCDDotTRTypeFace(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/lcddottr.ttf");
    }

    public static void applyFont(Context context, View root, Typeface typeface) {
        try {
            AvenirNextRegularTextView avenirNextRegularTextView;
            if (root instanceof TextView) {
                TextView textView = (TextView) root;
                textView.setPaintFlags((textView.getPaintFlags() | 128) | 1);
                textView.setTypeface(typeface);
            } else if (root instanceof AvenirNextRegularTextView) {
                avenirNextRegularTextView = (AvenirNextRegularTextView) root;
                avenirNextRegularTextView.setPaintFlags((avenirNextRegularTextView.getPaintFlags() | 128) | 1);
                avenirNextRegularTextView.setTypeface(setAMediumTypeFace());
            } else if (root instanceof AvenirNextRegularTextView) {
                avenirNextRegularTextView = (AvenirNextRegularTextView) root;
                avenirNextRegularTextView.setPaintFlags((avenirNextRegularTextView.getPaintFlags() | 128) | 1);
                avenirNextRegularTextView.setTypeface(setARegularTypeFace());
            } else if (root instanceof AvenirNextRegularButton) {
                AvenirNextRegularButton avenirNextRegularButton = (AvenirNextRegularButton) root;
                avenirNextRegularButton.setPaintFlags((avenirNextRegularButton.getPaintFlags() | 128) | 1);
                avenirNextRegularButton.setTypeface(setARegularTypeFace());
            } else if (root instanceof EditText) {
                EditText editText = (EditText) root;
                editText.setPaintFlags((editText.getPaintFlags() | 128) | 1);
                editText.setTypeface(typeface);
            } else if (root instanceof Button) {
                Button button = (Button) root;
                button.setPaintFlags((button.getPaintFlags() | 128) | 1);
                button.setTypeface(typeface);
            } else if (root instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) root;
                radioButton.setPaintFlags((radioButton.getPaintFlags() | 128) | 1);
                radioButton.setTypeface(typeface);
            } else if (root instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) root;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    applyFont(context, viewGroup.getChildAt(i), typeface);
                }
            }
        } catch (Exception e) {
        }
    }

    public static void applyARegularFont(Context context, View root) {
        applyFont(context, root, setARegularTypeFace());
    }

    public static void applyAMediumFont(Context context, View root) {
    }

    public static Typeface setDefaultTypeFace() {
        return Typeface.DEFAULT;
    }

    public static Typeface setAveNextCondensedRegularTypeFace() {
        return App.getAveNextCondensedRegularTypeFace();
    }

    public static Typeface setAvenirNextCondensedMediumTypeFace() {
        return App.getAvenirNextCondensedMediumTypeFace();
    }

    public static Typeface setAvenirNextUltralightTypeFace() {
        return App.getAvenirNextUltralightTypeFace();
    }

    public static Typeface setAvenirNextCondensedDemiBoldTypeFace() {
        return App.getAvenirNextCondensedDemiBoldTypeFace();
    }

    public static Typeface setDefaultTypeFace(Context context) {
        return Typeface.DEFAULT;
    }

    public static Typeface setFoundersGroteskXCondensedappMediumTypeFace(Context context) {
        return setFoundersGroutesqueXCondMedium(context);
    }

    public static Typeface setFoundersGroteskXCondensedappLightTypeFace(Context context) {
        return setFoundersGroutesqueXCondLight(context);
    }

    public static Typeface setAveNextCondensedRegularTypeFace(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/avenirnextcondensedregular.ttf");
    }

    public static Typeface setAvenirNextCondensedMediumTypeFace(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/avenirnextcondensedmedium.ttf");
    }

    public static Typeface setAvenirNextUltralightTypeFace(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/avenirnextultralight.ttf");
    }

    public static Typeface setAvenirNextCondensedDemiBoldTypeFace(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/avenirnextcondenseddemibold.ttf");
    }

    public static Typeface setFoundersGroutesqueXCondMedium(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/foundersgroteskxcondensedappmedium.ttf");
    }

    public static Typeface setFoundersGroutesqueXCondLight(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/foundersgroteskxcondensedapplight.ttf");
    }

    public static void applyFont(View root, Typeface typeface) {
        try {
            if (root instanceof TextView) {
                TextView textView = (TextView) root;
                textView.setPaintFlags((textView.getPaintFlags() | 128) | 1);
                textView.setTypeface(typeface);
            } else if (root instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) root;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    applyFont(viewGroup.getChildAt(i), typeface);
                }
            }
        } catch (Exception e) {
        }
    }

    public static void redownloadFont(VTFontDesBean vtFontDesBean) {
        //((GetRequest) ((GetRequest) OkHttpUtils.get(vtFontDesBean.url).tag(App.getContext())).cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)).execute(new 1(Config.getFontDestPath(vtFontDesBean.language), String.format("%1$s.%2$s", new Object[]{vtFontDesBean.filename, vtFontDesBean.fonttype}), vtFontDesBean));
    }
}