package cn.nineton.onetake.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.StatFs;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.nineton.onetake.App;

public class FileUtil {
    public static final CompressFormat BitmapCompressFormat = CompressFormat.JPEG;
    public static final boolean BitmapFilter = true;
    public static final int CacheFileMaxSize = 256;
    private static final long DEFAULT_MOST_FREE_SIZE = 10485760;
    public static final int GB = 1073741824;
    public static final int KB = 1024;
    private static final String LOCAL_FILE_END_KEY = "\"musicModels\":";
    private static final String LOCAL_FILE_START_KEY = "\"mUndoBean\":{";
    public static final int MB = 1048576;
    public static final int SIZETYPE_B = 1;
    public static final int SIZETYPE_GB = 4;
    public static final int SIZETYPE_KB = 2;
    public static final int SIZETYPE_MB = 3;
    private static final String TAG = "FileUtil";

    @SuppressLint({"SimpleDateFormat"})
    public static String getCurrentDateFileName() {
        return String.format("%1$s.jpg", new Object[]{new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())});
    }

    @SuppressLint({"SimpleDateFormat"})
    public static String getAndroidRenameCurrentDateFileName() {
        return String.format("IMG_%1$s.jpg", new Object[]{new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())});
    }

    @SuppressLint({"SimpleDateFormat"})
    public static String getAndroidRenameCurrentDateFileName(int index) {
        return String.format("IMG_%1$s_%2$d.jpg", new Object[]{new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()), Integer.valueOf(index)});
    }

    public static boolean writeAddonDescriptionsToFile(String path, String data) {
        try {
            FileWriter fw = new FileWriter(new File(path));
            fw.write(data);
            fw.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Bitmap handlerDropBitmap(Bitmap bitmap, int dstLength, float rotateDegrees) {
        if (bitmap == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(rotateDegrees);
        Bitmap finalBitmap = null;
        try {
            return Bitmap.createScaledBitmap(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true), dstLength, dstLength, true);
        } catch (OutOfMemoryError e) {
            return finalBitmap;
        } catch (RuntimeException e2) {
            return finalBitmap;
        }
    }


    public static byte[] File2byte(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            while (true) {
                int n = fis.read(b);
                if (n != -1) {
                    bos.write(b, 0, n);
                } else {
                    fis.close();
                    bos.close();
                    return bos.toByteArray();
                }
            }
        } catch (IOException e) {
            return null;
        }
    }



    public static String writeStoryOriginalJPGFile(Context context, File savePath, Bitmap originalBitmap) {
        if (savePath == null || originalBitmap == null) {
            return null;
        }
        try {
            FileOutputStream fos = new FileOutputStream(savePath);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            originalBitmap.compress(CompressFormat.JPEG, 80, bos);
            fos.flush();
            fos.close();
            bos.flush();
            bos.close();
        } catch (Exception e) {
            originalBitmap.recycle();
        }
        return savePath.getPath();
    }

    public static byte[] File2byte(String filePath) {
        if (filePath == null) {
            return null;
        }
        try {
            FileInputStream fis = new FileInputStream(filePath);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            while (true) {
                int n = fis.read(b);
                if (n != -1) {
                    bos.write(b, 0, n);
                } else {
                    fis.close();
                    bos.close();
                    return bos.toByteArray();
                }
            }
        } catch (IOException e) {
            return null;
        }
    }

    public static File getFirstFile(String path) {
        if (path != null && path.equals("")) {
            return null;
        }
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        if (!file.isDirectory()) {
            return file;
        }
        File[] list = file.listFiles();
        if (list.length == 0) {
            return null;
        }
        return list[0];
    }

    public static File getFirstFile(String path, String last) {
        if (path != null && path.equals("")) {
            return null;
        }
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        if (file.isDirectory()) {
            File[] list = file.listFiles();
            if (list.length == 0) {
                return null;
            }
            for (int i = 0; i < list.length; i++) {
                if (list[i].getName().contains(last)) {
                    return list[i];
                }
            }
            return null;
        }
        if (!file.getName().contains(last)) {
            file = null;
        }
        return file;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0028 A:{SYNTHETIC, Splitter: B:18:0x0028} */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0034 A:{SYNTHETIC, Splitter: B:24:0x0034} */
    public static boolean writeInfoToFile(java.lang.String r6, java.lang.String r7) {
        /*
        r4 = 0;
        r5 = com.blink.academy.onetake.support.utils.TextUtil.isNull(r7);
        if (r5 == 0) goto L_0x0008;
    L_0x0007:
        return r4;
    L_0x0008:
        r3 = new java.io.File;
        r3.<init>(r6);
        r1 = 0;
        r2 = new java.io.FileWriter;	 Catch:{ Exception -> 0x0022 }
        r2.<init>(r3);	 Catch:{ Exception -> 0x0022 }
        r2.write(r7);	 Catch:{ Exception -> 0x0040, all -> 0x003d }
        r4 = 1;
        if (r2 == 0) goto L_0x0007;
    L_0x0019:
        r2.close();	 Catch:{ IOException -> 0x001d }
        goto L_0x0007;
    L_0x001d:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0007;
    L_0x0022:
        r0 = move-exception;
    L_0x0023:
        r0.printStackTrace();	 Catch:{ all -> 0x0031 }
        if (r1 == 0) goto L_0x0007;
    L_0x0028:
        r1.close();	 Catch:{ IOException -> 0x002c }
        goto L_0x0007;
    L_0x002c:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0007;
    L_0x0031:
        r4 = move-exception;
    L_0x0032:
        if (r1 == 0) goto L_0x0037;
    L_0x0034:
        r1.close();	 Catch:{ IOException -> 0x0038 }
    L_0x0037:
        throw r4;
    L_0x0038:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0037;
    L_0x003d:
        r4 = move-exception;
        r1 = r2;
        goto L_0x0032;
    L_0x0040:
        r0 = move-exception;
        r1 = r2;
        goto L_0x0023;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.support.utils.FileUtil.writeInfoToFile(java.lang.String, java.lang.String):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x003c A:{SYNTHETIC, Splitter: B:23:0x003c} */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x002e A:{SYNTHETIC, Splitter: B:15:0x002e} */
    /* JADX WARNING: Removed duplicated region for block: B:35:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0033 A:{SYNTHETIC, Splitter: B:18:0x0033} */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0039 A:{PHI: r3 , Splitter: B:3:0x000e, ExcHandler: all (th java.lang.Throwable)} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:21:0x0039, code:
            r7 = th;
     */
    /* JADX WARNING: Missing block: B:24:?, code:
            r3.close();
     */
    public static java.lang.String readFile(java.lang.String r9, java.nio.charset.Charset r10) {
        /*
        r2 = new java.io.File;
        r2.<init>(r9);
        r5 = "";
        r7 = r2.exists();
        if (r7 == 0) goto L_0x0027;
    L_0x000d:
        r3 = 0;
        r4 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x002b, all -> 0x0039 }
        r4.<init>(r2);	 Catch:{ Exception -> 0x002b, all -> 0x0039 }
        r7 = r4.available();	 Catch:{ Exception -> 0x0047, all -> 0x0044 }
        r0 = new byte[r7];	 Catch:{ Exception -> 0x0047, all -> 0x0044 }
        r4.read(r0);	 Catch:{ Exception -> 0x0047, all -> 0x0044 }
        r6 = new java.lang.String;	 Catch:{ Exception -> 0x0047, all -> 0x0044 }
        r6.<init>(r0, r10);	 Catch:{ Exception -> 0x0047, all -> 0x0044 }
        if (r4 == 0) goto L_0x0026;
    L_0x0023:
        r4.close();	 Catch:{ IOException -> 0x0028 }
    L_0x0026:
        r5 = r6;
    L_0x0027:
        return r5;
    L_0x0028:
        r7 = move-exception;
        r5 = r6;
        goto L_0x0027;
    L_0x002b:
        r1 = move-exception;
    L_0x002c:
        if (r3 == 0) goto L_0x0031;
    L_0x002e:
        r3.close();	 Catch:{ IOException -> 0x0040, all -> 0x0039 }
    L_0x0031:
        if (r3 == 0) goto L_0x0027;
    L_0x0033:
        r3.close();	 Catch:{ IOException -> 0x0037 }
        goto L_0x0027;
    L_0x0037:
        r7 = move-exception;
        goto L_0x0027;
    L_0x0039:
        r7 = move-exception;
    L_0x003a:
        if (r3 == 0) goto L_0x003f;
    L_0x003c:
        r3.close();	 Catch:{ IOException -> 0x0042 }
    L_0x003f:
        throw r7;
    L_0x0040:
        r7 = move-exception;
        goto L_0x0031;
    L_0x0042:
        r8 = move-exception;
        goto L_0x003f;
    L_0x0044:
        r7 = move-exception;
        r3 = r4;
        goto L_0x003a;
    L_0x0047:
        r1 = move-exception;
        r3 = r4;
        goto L_0x002c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blink.academy.onetake.support.utils.FileUtil.readFile(java.lang.String, java.nio.charset.Charset):java.lang.String");
    }

    public static boolean deleteFile(String filePath) {
        if (TextUtil.isNull(filePath)) {
            return false;
        }
        File file = new File(filePath);
        if (file.exists() && file.delete()) {
            return true;
        }
        return false;
    }

    public static boolean deleteDirectory(String filePath) {
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();
        if (files == null) {
            return true;
        }
        for (File file : files) {
            if (!file.isFile()) {
                flag = deleteDirectory(file.getAbsolutePath());
                if (!flag) {
                    break;
                }
            } else {
                flag = deleteFile(file.getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }
        if (flag && dirFile.delete()) {
            return true;
        }
        return false;
    }

    public static void copyFile(String oldPath, String newPath) {
        if (new File(oldPath).exists()) {
            int bytesum = 0;
            try {
                if (new File(oldPath).exists()) {
                    InputStream inStream = new FileInputStream(oldPath);
                    FileOutputStream fs = new FileOutputStream(newPath);
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int byteread = inStream.read(buffer);
                        if (byteread != -1) {
                            bytesum += byteread;
                            fs.write(buffer, 0, byteread);
                        } else {
                            inStream.close();
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                Log.d("file", "复制单个文件操作出错");
            }
        }
    }

    public static void copyFile(File oldfile, String newPath) {
        if (oldfile.exists()) {
            int bytesum = 0;
            try {
                if (oldfile.exists()) {
                    InputStream inStream = new FileInputStream(oldfile.getPath());
                    FileOutputStream fs = new FileOutputStream(newPath);
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int byteread = inStream.read(buffer);
                        if (byteread != -1) {
                            bytesum += byteread;
                            fs.write(buffer, 0, byteread);
                        } else {
                            inStream.close();
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                Log.d("file", "复制单个文件操作出错");
            }
        }
    }


    public static void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            for (File file1 : files) {
                if (file1.isFile()) {
                    file1.delete();
                } else {
                    deleteFile(file1);
                }
            }
        }
        file.delete();
    }



    public static long getSDFreeSize(File file) {
        return getSDFreeSize(file.getPath());
    }

    public static long getSDFreeSize(String filePath) {
        StatFs statFs = new StatFs(filePath);
        return ((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize());
    }

    private static boolean isCanWrite(long writesize, File file) {
        long sdFreeSize = getSDFreeSize(file);
        LogUtil.d("isCanWrite", "sdFreeSize : " + sdFreeSize + "  writesize : " + writesize);
        return sdFreeSize > writesize;
    }

    private static boolean isCanWrite(long writesize, String filePath) {
        long sdFreeSize = getSDFreeSize(filePath);
        LogUtil.d("isCanWrite", "sdFreeSize : " + sdFreeSize + "  writesize : " + writesize);
        return sdFreeSize > writesize;
    }

    public static boolean isCanWirte(File file) {
        return isCanWrite((long) DEFAULT_MOST_FREE_SIZE, file);
    }

    public static boolean isCanWirte(String filePath) {
        return isCanWrite((long) DEFAULT_MOST_FREE_SIZE, filePath);
    }

    public static void deleteLongVideos(List<String> videoPaths) {
        if (videoPaths != null && videoPaths.size() > 0) {
            int size = videoPaths.size();
            for (int i = 0; i < size; i++) {
                LogUtil.d("copypath", String.format("path : %s", new Object[]{videoPaths.get(i)}));
                deleteLongVideo(Config.getLongVideoRecordPath() + ((String) videoPaths.get(i)) + ".mp4");
            }
            videoPaths.clear();
        }
    }

    private static void deleteLongVideo(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    public static String getPrivateAlbumFilePath(String fileName) {
        return Config.getLongVideoRecordPath() + "/" + fileName + ".mp4";
    }

    public static boolean judgeAndDeleteOldFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }


    public static void notifySystemFileFlesh(Activity activity, String filePath) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(new File(filePath)));
        activity.sendBroadcast(intent);
    }


    public static boolean fileIsExists(String filePath) {
        return filePath != null && new File(filePath).exists();
    }

    public static Bitmap getFilterPreview() {
        String filePath = Config.getPrivatePreviewFilterPath();
        if (new File(filePath).exists()) {
            return BitmapFactory.decodeFile(filePath);
        }
        return null;
    }

    public static String readLocalFileToStringByPath(String filePath) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
        StringBuffer buffer = new StringBuffer();
        String str = "";
        while (true) {
            str = in.readLine();
            if (str == null) {
                return buffer.toString();
            }
            buffer.append(str);
        }
    }

    public static boolean motifyLocalFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }
        String localFileString = readLocalFileToStringByPath(filePath);
        int startIndex = localFileString.indexOf(LOCAL_FILE_START_KEY);
        int endIndex = localFileString.indexOf(LOCAL_FILE_END_KEY);
        if (!(-1 == startIndex || -1 == endIndex)) {
            localFileString = localFileString.replace(localFileString.substring(startIndex, endIndex), "");
        }
        File tmpfile = new File(file.getParentFile().getAbsolutePath() + "/" + file.getName() + ".tmp.json");
        BufferedWriter writer = new BufferedWriter(new FileWriter(tmpfile));
        writer.write(localFileString, 0, localFileString.length());
        writer.flush();
        writer.close();
        file.delete();
        tmpfile.renameTo(new File(file.getAbsolutePath()));
        return true;
    }

    public static double getFileOrFilesSize(String filePath, int sizeType) {
        long blockSize = 0;
        try {
            File file = new File(filePath);
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormetFileSize(blockSize, sizeType);
    }

    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormetFileSize(blockSize);
    }

    private static long getFileSize(File file) throws Exception {
        if (!file.exists()) {
            return 0;
        }
        FileInputStream fis = new FileInputStream(file);
        long size = (long) fis.available();
        fis.close();
        return size;
    }

    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        for (File aFlist : f.listFiles()) {
            long fileSizes;
            if (aFlist.isDirectory()) {
                fileSizes = getFileSizes(aFlist);
            } else {
                fileSizes = getFileSize(aFlist);
            }
            size += fileSizes;
        }
        return size;
    }

    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        String fileSizeString;
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format(((double) fileS) / 1024.0d) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format(((double) fileS) / 1048576.0d) + "MB";
        } else {
            fileSizeString = df.format(((double) fileS) / 1.073741824E9d) + "GB";
        }
        return fileSizeString;
    }

    public static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        switch (sizeType) {
            case 1:
                return Double.valueOf(df.format((double) fileS)).doubleValue();
            case 2:
                return Double.valueOf(df.format(((double) fileS) / 1024.0d)).doubleValue();
            case 3:
                return Double.valueOf(df.format(((double) fileS) / 1048576.0d)).doubleValue();
            case 4:
                return Double.valueOf(df.format(((double) fileS) / 1.073741824E9d)).doubleValue();
            default:
                return 0.0d;
        }
    }
}