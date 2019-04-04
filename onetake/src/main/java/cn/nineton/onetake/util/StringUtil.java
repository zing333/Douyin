package cn.nineton.onetake.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.Normalizer.Form;

public class StringUtil {
    @SuppressLint({"DefaultLocale"})
    public static String formatNumber(float count) {
        if (LocaleUtil.isChineseCode()) {
            float number_yi = (float) Math.pow(10.0d, 8.0d);
            float number_wan = (float) Math.pow(10.0f,4.0f);
            if (count >= ((float) Math.pow(10.0d, 4.0d)) && count < number_yi) {
                return String.format("%s万", new Object[]{removeTheDecimalPointOfZeroWithFloat(count / number_wan)});
            } else if (count >= number_yi) {
                return String.format("%s亿", new Object[]{removeTheDecimalPointOfZeroWithFloat(count / number_yi)});
            }
        }
        float number_million = (float) Math.pow(10.0d, 6.0d);
        float number_billion = (float) Math.pow(10.0d, 9.0d);
        float number_thounsand = (float) Math.pow(10.0f,3.0f);
        if (count > ((float) Math.pow(10.0d, 3.0d)) && count < number_million) {
            return String.format("%sk", new Object[]{removeTheDecimalPointOfZeroWithFloat(count / number_thounsand)});
        } else if (count > number_million && count < number_billion) {
            return String.format("%sm", new Object[]{removeTheDecimalPointOfZeroWithFloat(count / number_million)});
        } else if (count > number_billion) {
            return String.format("%sb", new Object[]{removeTheDecimalPointOfZeroWithFloat(count / number_billion)});
        }
        return String.format("%.0f", new Object[]{Float.valueOf(count)});
    }

    @SuppressLint({"DefaultLocale"})
    public static String removeTheDecimalPointOfZeroWithFloat(float number) {
        if (((double) number) - Math.floor((double) number) < 0.1d) {
            return String.format("%.0f", new Object[]{Double.valueOf(Math.floor((double) number))});
        }
        return String.format("%.1f", new Object[]{Float.valueOf(number)}).replace(".0", "");
    }

    public static String dealMoneyDisplay(int money) {
        return "" + new DecimalFormat("#,###").format((long) money);
    }

    public static String dealColorString(String colorString) {
        int length = colorString.length();
        String returnString = colorString;
        if (length < 6) {
            for (int i = 0; i < 6 - length; i++) {
                returnString = returnString + "F";
            }
        }
        return returnString;
    }

    public static String replaceDoubleSpaceToSpace(String content) {
        return TextUtils.isEmpty(content) ? content : content.replace("　", " ");
    }

    public static String clearContentSpace(String content) {
        return TextUtils.isEmpty(content) ? content : replaceDoubleSpaceToSpace(content).replace(" ", "");
    }

    public static String clearContentSpaceAndEnter(String content) {
        return TextUtils.isEmpty(content) ? content : clearContentEnter(clearContentSpace(content));
    }

    public static String clearContentEnter(String content) {
        if (TextUtils.isEmpty(content)) {
            return content;
        }
        return content.replace("\r\n", "").replace("\n", "");
    }

    public static String clearContentSpcaeLineSpecialSymbols(String content) {
        return TextUtils.isEmpty(content) ? content : clearContentSpaceAndEnter(Normalizer.normalize(content, Form.NFD).replaceAll("\\p{M}", "").replaceAll("Æ", "AE").replaceAll("Ð", "D").replaceAll("Ø", "O").replaceAll("Þ", "Th").replaceAll("ß", "ss").replaceAll("æ", "ae").replaceAll("ð", "d").replaceAll("ø", "o").replaceAll("×", "x").replaceAll("þ", "th").replaceAll("Đ", "D").replaceAll("đ", "d").replaceAll("Ħ", "H").replaceAll("ħ", "h").replaceAll("ı", "i").replaceAll("Ĳ", "IJ").replaceAll("ĳ", "ij").replaceAll("ĸ", "k").replaceAll("Ŀ", "L").replaceAll("ŀ", "l").replaceAll("Ł", "L").replaceAll("ł", "l").replaceAll("ŉ", "n").replaceAll("Ŋ", "NG").replaceAll("ŋ", "ng").replaceAll("Œ", "OE").replaceAll("œ", "oe").replaceAll("Ŧ", "T").replaceAll("ŧ", "t")).replaceAll("[^0-9a-zA-Z]", "");
    }

//    public static void main(String[] argv) {
//        System.out.println(clearContentSpcaeLineSpecialSymbols("Tĥïŝ ĩš â fůňķŷ Šťŕĭńġ"));
//    }
}