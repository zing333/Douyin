package cn.nineton.onetake.util;

import android.graphics.BitmapFactory.Options;

public class OptionSizeUtil {
    public static int computeSampleSize(Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        if (initialSize > 8) {
            return ((initialSize + 7) / 8) * 8;
        }
        int roundedSize = 1;
        while (roundedSize < initialSize) {
            roundedSize <<= 1;
        }
        return roundedSize;
    }

    public static int computeInitialSampleSize(Options options, int minSideLength, int maxNumOfPixels) {
        int lowerBound;
        int upperBound;
        double w = (double) options.outWidth;
        double h = (double) options.outHeight;
        if (maxNumOfPixels == -1) {
            lowerBound = 1;
        } else {
            lowerBound = (int) Math.ceil(Math.sqrt((w * h) / ((double) maxNumOfPixels)));
        }
        if (minSideLength == -1) {
            upperBound = 128;
        } else {
            upperBound = (int) Math.min(Math.floor(w / ((double) minSideLength)), Math.floor(h / ((double) minSideLength)));
        }
        if (upperBound < lowerBound) {
            return lowerBound;
        }
        if (maxNumOfPixels == -1 && minSideLength == -1) {
            return 1;
        }
        if (minSideLength != -1) {
            return upperBound;
        }
        return lowerBound;
    }
}