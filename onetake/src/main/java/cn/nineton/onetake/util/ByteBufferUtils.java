package cn.nineton.onetake.util;

//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.ui.adapter.holder.reconstruction.BaseAdapter.Item;

import cn.nineton.onetake.adapter.BaseAdapter;

public class ByteBufferUtils {
    public static float[] getFloatByCaptureOrientationValue(float[] buffer, int orientation) {
        return getFloatByCaptureOrientationValue(buffer, orientation, false);
    }

    public static float[] getFloatByCaptureOrientationValue(float[] buffer, int orientation, boolean changed) {
        if (buffer == null) {
            throw new RuntimeException("buffer == null");
        } else if (buffer.length < 8) {
            throw new RuntimeException("buffer.length < 8");
        } else {
            switch (orientation) {
                case 0:
                case 90:
                    return new float[]{buffer[6], buffer[7], buffer[4], buffer[5], buffer[2], buffer[3], buffer[0], buffer[1]};
                case BaseAdapter.Item.VIDEO_MUSIC_DETAIL_HEAD_TYPE /*270*/:
                    LogUtil.d("refreshCropLeakFilterFirst", "getFloatByCaptureOrientationValue : changed : " + changed);
                    if (changed) {
                        return new float[]{buffer[2], buffer[3], buffer[6], buffer[7], buffer[0], buffer[1], buffer[4], buffer[5]};
                    }
                    break;
            }
            return buffer;
        }
    }

    private static float[] getFourPointByCoordinate(float a, float b, float w, float h) {
        return new float[]{a, b, a + w, b, a, b + h, a + w, b + h};
    }

    private static float[] getCoordinateByCaptureOrientation(float a, float b, float w, float h, int captureOrientation) {
        float[] newCoordinate = new float[4];
        switch (captureOrientation) {
            case 0:
                newCoordinate[0] = (1.0f - b) - h;
                newCoordinate[1] = a;
                newCoordinate[2] = h;
                newCoordinate[3] = w;
                break;
            case 90:
                newCoordinate[0] = (1.0f - a) - w;
                newCoordinate[1] = (1.0f - b) - h;
                newCoordinate[2] = w;
                newCoordinate[3] = h;
                break;
            case 180:
                newCoordinate[0] = b;
                newCoordinate[1] = (1.0f - a) - w;
                newCoordinate[2] = h;
                newCoordinate[3] = w;
                break;
            default:
                newCoordinate[0] = a;
                newCoordinate[1] = b;
                newCoordinate[2] = w;
                newCoordinate[3] = h;
                break;
        }
        return newCoordinate;
    }

    public static float[] changeBufferValueByRotateValue(float[] buffer, int degree) {
        switch (degree) {
            case 0:
                return buffer;
            case 90:
                return changeBufferValueAfterRotate(buffer);
            case 180:
                return changeBufferValueAfterRotate(changeBufferValueAfterRotate(buffer));
            case BaseAdapter.Item.VIDEO_MUSIC_DETAIL_HEAD_TYPE /*270*/:
                return changeBufferValueAfterRotate(changeBufferValueAfterRotate(changeBufferValueAfterRotate(buffer)));
            default:
                return buffer;
        }
    }

    public static float[] changeBufferValueAfterRotate(float[] buffer) {
        return new float[]{buffer[2], buffer[3], buffer[6], buffer[7], buffer[0], buffer[1], buffer[4], buffer[5]};
    }

    public static float[] changeBufferValueAfterCrop(float[] buffer, int captureOrientation) {
        float[] newBuffer = new float[8];
        switch (captureOrientation) {
            case 0:
            case 180:
                newBuffer[0] = buffer[4];
                newBuffer[1] = buffer[5];
                newBuffer[2] = buffer[0];
                newBuffer[3] = buffer[1];
                newBuffer[4] = buffer[6];
                newBuffer[5] = buffer[7];
                newBuffer[6] = buffer[2];
                newBuffer[7] = buffer[3];
                break;
            case 90:
            case BaseAdapter.Item.VIDEO_MUSIC_DETAIL_HEAD_TYPE /*270*/:
                newBuffer[0] = buffer[2];
                newBuffer[1] = buffer[3];
                newBuffer[2] = buffer[6];
                newBuffer[3] = buffer[7];
                newBuffer[4] = buffer[0];
                newBuffer[5] = buffer[1];
                newBuffer[6] = buffer[4];
                newBuffer[7] = buffer[5];
                break;
        }
        return newBuffer;
    }

    private static float[] changeBufferValue(float[] buffer) {
        return buffer;
    }

    public static float[] getBufferByDepth(float[] buffer, int depth, int captureOrientation) {
        float[] newBuffer = new float[8];
        switch (depth) {
            case 0:
                return buffer;
            case 1:
                if (captureOrientation == BaseAdapter.Item.VIDEO_MUSIC_DETAIL_HEAD_TYPE || captureOrientation == 90) {
                    newBuffer[0] = buffer[4];
                    newBuffer[1] = buffer[5];
                    newBuffer[2] = buffer[6];
                    newBuffer[3] = buffer[7];
                    newBuffer[4] = buffer[0];
                    newBuffer[5] = buffer[1];
                    newBuffer[6] = buffer[2];
                    newBuffer[7] = buffer[3];
                    return newBuffer;
                }
                newBuffer[0] = buffer[2];
                newBuffer[1] = buffer[3];
                newBuffer[2] = buffer[0];
                newBuffer[3] = buffer[1];
                newBuffer[4] = buffer[6];
                newBuffer[5] = buffer[7];
                newBuffer[6] = buffer[4];
                newBuffer[7] = buffer[5];
                return newBuffer;
            case 2:
                if (captureOrientation == BaseAdapter.Item.VIDEO_MUSIC_DETAIL_HEAD_TYPE || captureOrientation == 90) {
                    newBuffer[0] = buffer[2];
                    newBuffer[1] = buffer[3];
                    newBuffer[2] = buffer[0];
                    newBuffer[3] = buffer[1];
                    newBuffer[4] = buffer[6];
                    newBuffer[5] = buffer[7];
                    newBuffer[6] = buffer[4];
                    newBuffer[7] = buffer[5];
                    return newBuffer;
                }
                newBuffer[0] = buffer[4];
                newBuffer[1] = buffer[5];
                newBuffer[2] = buffer[6];
                newBuffer[3] = buffer[7];
                newBuffer[4] = buffer[0];
                newBuffer[5] = buffer[1];
                newBuffer[6] = buffer[2];
                newBuffer[7] = buffer[3];
                return newBuffer;
            case 3:
                newBuffer[0] = buffer[6];
                newBuffer[1] = buffer[7];
                newBuffer[2] = buffer[4];
                newBuffer[3] = buffer[5];
                newBuffer[4] = buffer[2];
                newBuffer[5] = buffer[3];
                newBuffer[6] = buffer[0];
                newBuffer[7] = buffer[1];
                return newBuffer;
            default:
                return newBuffer;
        }
    }
}