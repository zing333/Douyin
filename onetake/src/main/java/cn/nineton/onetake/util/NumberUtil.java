package cn.nineton.onetake.util;

public class NumberUtil {
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

    public static String removeTheDecimalPointOfZeroWithFloat(float number) {
        if (((double) number) - Math.floor((double) number) < 0.1d) {
            return String.format("%.0f", new Object[]{Double.valueOf(Math.floor((double) number))});
        }
        return String.format("%.1f", new Object[]{Float.valueOf(number)}).replace(".0", "");
    }

    public static long discardTheLastTwoDigits(long value) {
        return (value / 100) * 100;
    }
}