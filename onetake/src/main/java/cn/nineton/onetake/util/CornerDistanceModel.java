package cn.nineton.onetake.util;


public class CornerDistanceModel {
    private static CornerDistanceModel cornerDistanceModel;
    public double bottomHeight;
    public double bottomMargin;
    public double cameraViewHeight;
    public int captureModel;
    public double coverHeight;
    public boolean isHorizontalAndSquare;
    public boolean isVertical;
    public float localLeftMargin;
    public float localMovedX;
    public float localMovedY;
    public float localOffsetPercent;
    public float localTopHeight;
    public float localTopMargin;
    public float localVideoHeight;
    public float localVideoPercent;
    public float localVideoWidth;
    public float localhorizontalPercent;
    public float localhorizontalVideoPercent;
    public double realWidth;
    public double screenHeight;
    public double size;
    public double topMargin;

    public static CornerDistanceModel getInstance() {
        if (cornerDistanceModel == null) {
            synchronized (FollowFilterModel.class) {
                if (cornerDistanceModel == null) {
                    cornerDistanceModel = new CornerDistanceModel();
                }
            }
        }
        return cornerDistanceModel;
    }

    public void calculatorSize() {
        this.size = ((this.coverHeight - this.topMargin) * 1.0d) / ((this.cameraViewHeight + this.topMargin) * 1.0d);
    }

    public double getVideoPercent() {
        LogUtil.d("wangchen54", "bottomMargin = " + this.bottomMargin);
        return (((this.screenHeight - this.coverHeight) - this.bottomHeight) * 1.0d) / (this.cameraViewHeight * 1.0d);
    }

    public void calculatorPercent() {
        this.localOffsetPercent = ((this.localTopMargin - this.localMovedY) * 1.0f) / this.localVideoHeight;
    }

    public void calculatorHorizontalPercent() {
        this.localhorizontalPercent = ((this.localLeftMargin + this.localMovedX) * 1.0f) / this.localVideoWidth;
    }
}