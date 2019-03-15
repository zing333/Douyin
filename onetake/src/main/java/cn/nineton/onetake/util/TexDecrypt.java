package cn.nineton.onetake.util;

import android.util.Log;

import java.nio.ByteBuffer;

public class TexDecrypt {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected TexDecrypt(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(TexDecrypt obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                //VidStab_WrapperJNI.delete_TexDecrypt(this.swigCPtr);
                Log.e("debug","删除:VidStab_WrapperJNI.delete_TexDecrypt:"+swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public static int createTexture(ByteBuffer start) {
        Log.e("debug","创建:createTexture:"+start.toString());
        return 0;//VidStab_WrapperJNI.TexDecrypt_createTexture(start);
    }

    public TexDecrypt() {
        //this(VidStab_WrapperJNI.new_TexDecrypt(), true);
        this(0,true);
    }
}