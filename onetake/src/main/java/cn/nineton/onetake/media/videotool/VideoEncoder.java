package cn.nineton.onetake.media.videotool;

import cn.nineton.onetake.media.FrameRenderer;

public interface VideoEncoder {

    public enum Quality {
        LOW,
        HIGH,
        DRAFT
    }

    void putFrames(String str, int i, int i2, FrameRenderer frameRenderer, boolean z, Quality quality);
}