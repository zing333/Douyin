package cn.nineton.onetake.util;

public class CustomCropUtil {
    public static float[] getCropPercent(float mWidth, float mHeight, float marginTop, float marginLeft) {
        float cropWidth;
        float cropHeight;
        if (mWidth / mHeight < 1.0f) {
            cropWidth = mWidth;
            cropHeight = mWidth / 1.0f;
        } else {
            cropHeight = mHeight;
            cropWidth = mHeight * 1.0f;
        }
        float leftX = ((mWidth / 2.0f) - (cropWidth / 2.0f)) + marginLeft;
        float topY = ((mHeight / 2.0f) - (cropHeight / 2.0f)) + marginTop;
        float percentX = (leftX - marginLeft) / mWidth;
        float percentY = (topY - marginTop) / mHeight;
        float percentWidth = ((((mWidth / 2.0f) + (cropWidth / 2.0f)) + marginLeft) - leftX) / mWidth;
        float percentHeight = ((((mHeight / 2.0f) + (cropHeight / 2.0f)) + marginTop) - topY) / mHeight;
        return new float[]{leftX, topY, (((mWidth / 2.0f) + (cropWidth / 2.0f)) + marginLeft) - leftX, (((mHeight / 2.0f) + (cropHeight / 2.0f)) + marginTop) - topY, percentX, percentY, percentWidth, percentHeight};
    }
}