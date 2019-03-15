package cn.nineton.onetake.media.audiotool;

import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class AudioEncoderAAC {
    private int mBitRate;
    private Callbacks mCallbacks;
    private boolean mHaveSubmittedInput;
    private long mInputCount = 0;
    private int mNumChannels;
    private int mSampleRate;
    private MediaCodec mediaCodec;
    private String mediaType = "audio/mp4a-latm";
    private ByteArrayOutputStream outputStream;

    public interface Callbacks {
        void onAudioBufferAvailable(BufferInfo bufferInfo, ByteBuffer byteBuffer);

        void onAudioFormatChanged(MediaFormat mediaFormat);
    }

    public AudioEncoderAAC(int sampleRate, int bitRate, int numChannels) {
        this.mSampleRate = sampleRate;
        this.mBitRate = bitRate;
        this.mNumChannels = numChannels;
    }

    public void prepareEncoder(Callbacks callbacks) {
        if (callbacks != null) {
            this.mCallbacks = callbacks;
        } else {
            this.outputStream = new ByteArrayOutputStream();
        }
        try {
            this.mediaCodec = MediaCodec.createEncoderByType(this.mediaType);
            MediaFormat mediaFormat = MediaFormat.createAudioFormat(this.mediaType, this.mSampleRate, this.mNumChannels);
            mediaFormat.setInteger("aac-profile", 2);
            mediaFormat.setInteger("bitrate", this.mBitRate);
            this.mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            this.mediaCodec.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MediaFormat getOutputFormat() {
        return this.mediaCodec.getOutputFormat();
    }

    public byte[] getEncodedBytes() {
        return this.outputStream.toByteArray();
    }

    public void close() {
        try {
            this.mediaCodec.stop();
            this.mediaCodec.release();
            if (this.mCallbacks == null) {
                this.outputStream.flush();
                this.outputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void flush() {
        offerEncoder(null, true);
    }

    /* JADX WARNING: Missing block: B:10:0x0028, code:
            if (r17 >= r31.length) goto L_0x002a;
     */
    public synchronized void offerEncoder(byte[] r31, boolean r32) {
        /*
        r30 = this;
        monitor-enter(r30);
        r17 = 0;
        r11 = new android.media.MediaCodec$BufferInfo;	 Catch:{ all -> 0x0185 }
        r11.<init>();	 Catch:{ all -> 0x0185 }
        r12 = 0;
        r13 = 0;
        r4 = android.os.Build.VERSION.SDK_INT;	 Catch:{ all -> 0x0185 }
        r6 = 21;
        if (r4 >= r6) goto L_0x0020;
    L_0x0010:
        r0 = r30;
        r4 = r0.mediaCodec;	 Catch:{ all -> 0x0185 }
        r12 = r4.getInputBuffers();	 Catch:{ all -> 0x0185 }
        r0 = r30;
        r4 = r0.mediaCodec;	 Catch:{ all -> 0x0185 }
        r13 = r4.getOutputBuffers();	 Catch:{ all -> 0x0185 }
    L_0x0020:
        r14 = 0;
    L_0x0021:
        if (r31 == 0) goto L_0x002a;
    L_0x0023:
        r0 = r31;
        r4 = r0.length;	 Catch:{ Throwable -> 0x0108 }
        r0 = r17;
        if (r0 < r4) goto L_0x002e;
    L_0x002a:
        if (r32 == 0) goto L_0x010c;
    L_0x002c:
        if (r14 != 0) goto L_0x010c;
    L_0x002e:
        r0 = r30;
        r4 = r0.mediaCodec;	 Catch:{ Throwable -> 0x0108 }
        r26 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        r0 = r26;
        r5 = r4.dequeueInputBuffer(r0);	 Catch:{ Throwable -> 0x0108 }
        if (r5 < 0) goto L_0x00a9;
    L_0x003c:
        r4 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Throwable -> 0x0108 }
        r6 = 21;
        if (r4 < r6) goto L_0x010e;
    L_0x0042:
        r0 = r30;
        r4 = r0.mediaCodec;	 Catch:{ Throwable -> 0x0108 }
        r16 = r4.getInputBuffer(r5);	 Catch:{ Throwable -> 0x0108 }
    L_0x004a:
        r7 = 0;
        if (r31 == 0) goto L_0x0065;
    L_0x004d:
        r0 = r31;
        r4 = r0.length;	 Catch:{ Throwable -> 0x0108 }
        r23 = r4 - r17;
        r4 = r16.limit();	 Catch:{ Throwable -> 0x0108 }
        r0 = r23;
        r7 = java.lang.Math.min(r4, r0);	 Catch:{ Throwable -> 0x0108 }
        r0 = r16;
        r1 = r31;
        r2 = r17;
        r0.put(r1, r2, r7);	 Catch:{ Throwable -> 0x0108 }
    L_0x0065:
        r26 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        r0 = r30;
        r0 = r0.mInputCount;	 Catch:{ Throwable -> 0x0108 }
        r28 = r0;
        r26 = r26 * r28;
        r0 = r30;
        r4 = r0.mSampleRate;	 Catch:{ Throwable -> 0x0108 }
        r0 = (long) r4;	 Catch:{ Throwable -> 0x0108 }
        r28 = r0;
        r8 = r26 / r28;
        r0 = r30;
        r0 = r0.mInputCount;	 Catch:{ Throwable -> 0x0108 }
        r26 = r0;
        r4 = r7 / 4;
        r0 = (long) r4;	 Catch:{ Throwable -> 0x0108 }
        r28 = r0;
        r26 = r26 + r28;
        r0 = r26;
        r2 = r30;
        r2.mInputCount = r0;	 Catch:{ Throwable -> 0x0108 }
        r10 = 0;
        r17 = r17 + r7;
        if (r32 == 0) goto L_0x009c;
    L_0x0091:
        if (r31 == 0) goto L_0x009a;
    L_0x0093:
        r0 = r31;
        r4 = r0.length;	 Catch:{ Throwable -> 0x0108 }
        r0 = r17;
        if (r0 != r4) goto L_0x009c;
    L_0x009a:
        r10 = r10 | 4;
    L_0x009c:
        r0 = r30;
        r4 = r0.mediaCodec;	 Catch:{ Throwable -> 0x0108 }
        r6 = 0;
        r4.queueInputBuffer(r5, r6, r7, r8, r10);	 Catch:{ Throwable -> 0x0108 }
        r4 = 1;
        r0 = r30;
        r0.mHaveSubmittedInput = r4;	 Catch:{ Throwable -> 0x0108 }
    L_0x00a9:
        r0 = r30;
        r4 = r0.mHaveSubmittedInput;	 Catch:{ Throwable -> 0x0108 }
        if (r4 == 0) goto L_0x0021;
    L_0x00af:
        r0 = r30;
        r4 = r0.mediaCodec;	 Catch:{ Throwable -> 0x0108 }
        r26 = 0;
        r0 = r26;
        r22 = r4.dequeueOutputBuffer(r11, r0);	 Catch:{ Throwable -> 0x0108 }
        r4 = -2;
        r0 = r22;
        if (r0 != r4) goto L_0x00d5;
    L_0x00c0:
        r0 = r30;
        r4 = r0.mediaCodec;	 Catch:{ Throwable -> 0x0108 }
        r15 = r4.getOutputFormat();	 Catch:{ Throwable -> 0x0108 }
        r0 = r30;
        r4 = r0.mCallbacks;	 Catch:{ Throwable -> 0x0108 }
        if (r4 == 0) goto L_0x00d5;
    L_0x00ce:
        r0 = r30;
        r4 = r0.mCallbacks;	 Catch:{ Throwable -> 0x0108 }
        r4.onAudioFormatChanged(r15);	 Catch:{ Throwable -> 0x0108 }
    L_0x00d5:
        if (r22 < 0) goto L_0x0021;
    L_0x00d7:
        r4 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Throwable -> 0x0108 }
        r6 = 21;
        if (r4 < r6) goto L_0x0112;
    L_0x00dd:
        r0 = r30;
        r4 = r0.mediaCodec;	 Catch:{ Throwable -> 0x0108 }
        r0 = r22;
        r21 = r4.getOutputBuffer(r0);	 Catch:{ Throwable -> 0x0108 }
    L_0x00e7:
        r0 = r30;
        r4 = r0.mCallbacks;	 Catch:{ Throwable -> 0x0108 }
        if (r4 == 0) goto L_0x0115;
    L_0x00ed:
        r0 = r30;
        r4 = r0.mCallbacks;	 Catch:{ Throwable -> 0x0108 }
        r0 = r21;
        r4.onAudioBufferAvailable(r11, r0);	 Catch:{ Throwable -> 0x0108 }
    L_0x00f6:
        r4 = r11.flags;	 Catch:{ Throwable -> 0x0108 }
        r4 = r4 & 4;
        if (r4 == 0) goto L_0x00fd;
    L_0x00fc:
        r14 = 1;
    L_0x00fd:
        r0 = r30;
        r4 = r0.mediaCodec;	 Catch:{ Throwable -> 0x0108 }
        r6 = 0;
        r0 = r22;
        r4.releaseOutputBuffer(r0, r6);	 Catch:{ Throwable -> 0x0108 }
        goto L_0x00af;
    L_0x0108:
        r24 = move-exception;
        r24.printStackTrace();	 Catch:{ all -> 0x0185 }
    L_0x010c:
        monitor-exit(r30);
        return;
    L_0x010e:
        r16 = r12[r5];	 Catch:{ Throwable -> 0x0108 }
        goto L_0x004a;
    L_0x0112:
        r21 = r13[r22];	 Catch:{ Throwable -> 0x0108 }
        goto L_0x00e7;
    L_0x0115:
        r4 = r11.flags;	 Catch:{ Throwable -> 0x0108 }
        r4 = r4 & 2;
        if (r4 != 0) goto L_0x00f6;
    L_0x011b:
        r0 = r11.size;	 Catch:{ Throwable -> 0x0108 }
        r18 = r0;
        r20 = r18 + 7;
        r4 = r11.offset;	 Catch:{ Throwable -> 0x0108 }
        r0 = r21;
        r0.position(r4);	 Catch:{ Throwable -> 0x0108 }
        r4 = r11.offset;	 Catch:{ Throwable -> 0x0108 }
        r4 = r4 + r18;
        r0 = r21;
        r0.limit(r4);	 Catch:{ Throwable -> 0x0108 }
        r0 = r20;
        r0 = new byte[r0];	 Catch:{ Throwable -> 0x0108 }
        r19 = r0;
        r0 = r30;
        r1 = r19;
        r2 = r20;
        r0.addADTStoPacket(r1, r2);	 Catch:{ Throwable -> 0x0108 }
        r4 = 7;
        r0 = r21;
        r1 = r19;
        r2 = r18;
        r0.get(r1, r4, r2);	 Catch:{ Throwable -> 0x0108 }
        r4 = r11.offset;	 Catch:{ Throwable -> 0x0108 }
        r0 = r21;
        r0.position(r4);	 Catch:{ Throwable -> 0x0108 }
        r0 = r30;
        r4 = r0.outputStream;	 Catch:{ Throwable -> 0x0108 }
        r6 = 0;
        r0 = r19;
        r0 = r0.length;	 Catch:{ Throwable -> 0x0108 }
        r25 = r0;
        r0 = r19;
        r1 = r25;
        r4.write(r0, r6, r1);	 Catch:{ Throwable -> 0x0108 }
        r4 = "AudioEncoder";
        r6 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0108 }
        r6.<init>();	 Catch:{ Throwable -> 0x0108 }
        r0 = r19;
        r0 = r0.length;	 Catch:{ Throwable -> 0x0108 }
        r25 = r0;
        r0 = r25;
        r6 = r6.append(r0);	 Catch:{ Throwable -> 0x0108 }
        r25 = " bytes written";
        r0 = r25;
        r6 = r6.append(r0);	 Catch:{ Throwable -> 0x0108 }
        r6 = r6.toString();	 Catch:{ Throwable -> 0x0108 }
        android.util.Log.e(r4, r6);	 Catch:{ Throwable -> 0x0108 }
        goto L_0x00f6;
    L_0x0185:
        r4 = move-exception;
        monitor-exit(r30);
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.VideoTools.AudioEncoderAAC.offerEncoder(byte[], boolean):void");
    }

    private void addADTStoPacket(byte[] packet, int packetLen) {
        packet[0] = (byte) -1;
        packet[1] = (byte) -7;
        packet[2] = (byte) 92;
        packet[3] = (byte) ((packetLen >> 11) + 64);
        packet[4] = (byte) ((packetLen & 2047) >> 3);
        packet[5] = (byte) (((packetLen & 7) << 5) + 31);
        packet[6] = (byte) -4;
    }
}