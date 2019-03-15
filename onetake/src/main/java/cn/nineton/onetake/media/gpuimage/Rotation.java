package cn.nineton.onetake.media.gpuimage;

public enum Rotation {
    NORMAL,
    ROTATION_90,
    ROTATION_180,
    ROTATION_270,
    NEGATIVE_NORMAL,
    NEGATIVE_ROTATION_90,
    NEGATIVE_ROTATION_180,
    NEGATIVE_ROTATION_270;

    public int asInt() {
        switch (this) {
            case NORMAL:
                return 0;
            case ROTATION_90:
                return 90;
            case ROTATION_180:
                return 180;
            case ROTATION_270:
                return 270;
            default:
                throw new IllegalStateException("Unknown Rotation!");
        }
    }

    public static Rotation fromInt(int rotation) {
        switch (rotation) {
            case -360:
                return NEGATIVE_NORMAL;
            case -270:
                return NEGATIVE_ROTATION_270;
            case -180:
                return NEGATIVE_ROTATION_180;
            case -90:
                return NEGATIVE_ROTATION_90;
            case -1:
                return NEGATIVE_NORMAL;
            case 0:
                return NORMAL;
            case 90:
                return ROTATION_90;
            case 180:
                return ROTATION_180;
            case 270:
                return ROTATION_270;
            case 360:
                return NORMAL;
            default:
                throw new IllegalStateException(rotation + " is an unknown rotation. Needs to be either 0, 90, 180 or 270!");
        }
    }
}