package cn.nineton.onetake.media.videotool;

import java.nio.ByteBuffer;

public class VidStab_WrapperJNI {
    static {
        System.loadLibrary("taker");
    }

    //    public static final native void GIFEncoder_choosePalette(long j, GIFEncoder gIFEncoder);
//
//    public static final native void GIFEncoder_dispose(long j, GIFEncoder gIFEncoder);
//
//    public static final native void GIFEncoder_palettize(long j, GIFEncoder gIFEncoder);
//
//    public static final native void GIFEncoder_setFrameData(long j, GIFEncoder gIFEncoder, int i, ByteBuffer byteBuffer);
//
//    public static final native boolean GIFEncoder_writeGIF(long j, GIFEncoder gIFEncoder, String str, boolean z);
//
//    public static final native int PhaseVocoderAndroid_fill(long j, PhaseVocoderAndroid phaseVocoderAndroid, int i, ByteBuffer byteBuffer, int i2, int i3);
//
//    public static final native int PhaseVocoderAndroid_getAnalysisSize(long j, PhaseVocoderAndroid phaseVocoderAndroid);
//
//    public static final native int PhaseVocoderAndroid_getSynthesisSize(long j, PhaseVocoderAndroid phaseVocoderAndroid);
//
//    public static final native boolean PhaseVocoderAndroid_needNewAnalysis(long j, PhaseVocoderAndroid phaseVocoderAndroid);
//
//    public static final native void PhaseVocoderAndroid_setSpeed(long j, PhaseVocoderAndroid phaseVocoderAndroid, int i, int i2);
//
//    public static final native void PhaseVocoderAndroid_synthesis(long j, PhaseVocoderAndroid phaseVocoderAndroid, int i, ByteBuffer byteBuffer);
//
//    public static final native int SLAudioRecorder_getBuffer(long j, SLAudioRecorder sLAudioRecorder, ByteBuffer byteBuffer);
//
//    public static final native int SLAudioRecorder_getBufferSizeInBytes(long j, SLAudioRecorder sLAudioRecorder);
//
//    public static final native long SLAudioRecorder_getBufferTime(long j, SLAudioRecorder sLAudioRecorder);
//
//    public static final native int SLAudioRecorder_getSampleRate(long j, SLAudioRecorder sLAudioRecorder);
//
//    public static final native boolean SLAudioRecorder_initialize(long j, SLAudioRecorder sLAudioRecorder, int i);
//
//    public static final native void SLAudioRecorder_shutdown(long j, SLAudioRecorder sLAudioRecorder);
//
//    public static final native void SLAudioRecorder_startRecording(long j, SLAudioRecorder sLAudioRecorder);
//
//    public static final native void SLAudioRecorder_stopRecording(long j, SLAudioRecorder sLAudioRecorder);
//
//    public static final native int TexDecrypt_createTexture(ByteBuffer byteBuffer);
//
//    public static final native void VidStabAnalyzer_addFramebuffer(long j, VidStabAnalyzer vidStabAnalyzer, int i, ByteBuffer byteBuffer);
    public static final  void VidStabAnalyzer_addFramebuffer(long j, VidStabAnalyzer vidStabAnalyzer, int i, ByteBuffer byteBuffer){};
//
//    public static final native void VidStabAnalyzer_destroy(long j, VidStabAnalyzer vidStabAnalyzer);
    public static final  void VidStabAnalyzer_destroy(long j, VidStabAnalyzer vidStabAnalyzer){};
//
//    public static final native boolean VidStabAnalyzer_initialize(long j, VidStabAnalyzer vidStabAnalyzer, int i, int i2);
    public static final  boolean VidStabAnalyzer_initialize(long j, VidStabAnalyzer vidStabAnalyzer, int i, int i2){
        return false;
    };
//
//    public static final native void VidStabLuminance_convert(long j, VidStabLuminance vidStabLuminance, int i);
    public static final void VidStabLuminance_convert(long j, VidStabLuminance vidStabLuminance, int i){};
//
//    public static final native void VidStabLuminance_setSource(long j, VidStabLuminance vidStabLuminance, ByteBuffer byteBuffer);
    public static final void VidStabLuminance_setSource(long j, VidStabLuminance vidStabLuminance, ByteBuffer byteBuffer){

    };
//
//    public static final native void VidStabLuminance_setTarget(long j, VidStabLuminance vidStabLuminance, ByteBuffer byteBuffer);
    public static final void VidStabLuminance_setTarget(long j, VidStabLuminance vidStabLuminance, ByteBuffer byteBuffer){};
//
//    public static final native void VidStab_createTransforms(long j, VidStab vidStab, int i, int[] iArr, boolean z);
    public static final void VidStab_createTransforms(long j, VidStab vidStab, int i, int[] iArr, boolean z) {
    }

    ;

    //
//    public static final native int VidStab_getNumReadyFrames(long j, VidStab vidStab);
    public static final int VidStab_getNumReadyFrames(long j, VidStab vidStab) {
        return -1;
    }

    //
//    public static final native void VidStab_getTransform(long j, VidStab vidStab, int i, double[] dArr);
    public static final void VidStab_getTransform(long j, VidStab vidStab, int i, double[] dArr) {
    }

    ;

    //
//    public static final native boolean VidStab_initialize(long j, VidStab vidStab, int i, int i2, int i3);
    public static final boolean VidStab_initialize(long j, VidStab vidStab, int i, int i2, int i3) {
        return false;
    }

    //
//    public static final native void VidStab_setFrameAnalysis(long j, VidStab vidStab, int i, long j2, VidStabAnalyzer vidStabAnalyzer);
    public static final void VidStab_setFrameAnalysis(long j, VidStab vidStab, int i, long j2, VidStabAnalyzer vidStabAnalyzer) {
    }

    ;
//
//    public static final native boolean X264Encoder_addFrame(long j, X264Encoder x264Encoder, long j2, ByteBuffer byteBuffer);
    public static final boolean X264Encoder_addFrame(long j, X264Encoder x264Encoder, long j2, ByteBuffer byteBuffer){
        return false;
    };
//
//    public static final native boolean X264Encoder_finish(long j, X264Encoder x264Encoder);
    public static final  boolean X264Encoder_finish(long j, X264Encoder x264Encoder){
        return false;
    };
//
//    public static final native boolean X264Encoder_start(long j, X264Encoder x264Encoder, ByteBuffer byteBuffer);
    public static final boolean X264Encoder_start(long j, X264Encoder x264Encoder, ByteBuffer byteBuffer){
        return false;
    };
//
//    public static final native int X264Encoder_totalBytes(long j, X264Encoder x264Encoder);
    public static final int X264Encoder_totalBytes(long j, X264Encoder x264Encoder){
        return -1;
    };
//
//    public static final native void delete_GIFEncoder(long j);
//
//    public static final native void delete_PhaseVocoderAndroid(long j);
//
//    public static final native void delete_SLAudioRecorder(long j);
//
//    public static final native void delete_TexDecrypt(long j);
//
//    public static final native void delete_VidStab(long j);
    public static final  void delete_VidStab(long j){};
//
//    public static final native void delete_VidStabAnalyzer(long j);
    public static final  void delete_VidStabAnalyzer(long j){};
//
//    public static final native void delete_VidStabLuminance(long j);
    public static final void delete_VidStabLuminance(long j){};
//
//    public static final native void delete_X264Encoder(long j);
    public static final  void delete_X264Encoder(long j){};
//
//    public static final native long new_GIFEncoder(int i, int i2, int i3, int i4);
//
//    public static final native long new_PhaseVocoderAndroid(int i);
//
//    public static final native long new_SLAudioRecorder();
//
//    public static final native long new_TexDecrypt();
//
//    public static final native long new_VidStab();
    public static final long new_VidStab(){
        return -1;
    };
//
//    public static final native long new_VidStabAnalyzer();
    public static final  long new_VidStabAnalyzer(){
        return -1;
    };
//
//    public static final native long new_VidStabLuminance();
    public static final long new_VidStabLuminance(){
        return -1;
    };
//
//    public static final native long new_X264Encoder(String str, int i, int i2, int i3, int i4, int i5, int i6, int i7);
    public static final long new_X264Encoder(String str, int i, int i2, int i3, int i4, int i5, int i6, int i7){
        return -1;
    };
}