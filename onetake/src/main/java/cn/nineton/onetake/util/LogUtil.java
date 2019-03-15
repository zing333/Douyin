package cn.nineton.onetake.util;

import android.util.Log;
import com.socks.library.Constant;
import com.socks.library.klog.BaseLog;
import com.socks.library.klog.FileLog;
import com.socks.library.klog.JsonLog;
import com.socks.library.klog.XmlLog;
import java.io.File;

public class LogUtil implements Constant {
    private static boolean IS_SHOW_LOG = true;
    private static LogUtil sLogUtil;

    public static void init(boolean isShowLog) {
        IS_SHOW_LOG = isShowLog;
    }

    private LogUtil() {
    }

    public static LogUtil getInstance() {
        if (sLogUtil == null) {
            synchronized (LogUtil.class) {
                if (sLogUtil == null) {
                    sLogUtil = new LogUtil();
                }
            }
        }
        return sLogUtil;
    }

    public static void v() {
        printLog(1, null, Constant.DEFAULT_MESSAGE);
    }

    public static void v(Object msg) {
        printLog(1, null, msg);
    }

    public static void v(String tag, String msg) {
        printLog(1, tag, msg);
    }

    public static void d() {
        printLog(2, null, Constant.DEFAULT_MESSAGE);
    }

    public static void d(Object msg) {
        printLog(2, null, msg);
    }

    public static void d(String tag, Object msg) {
        printLog(2, tag, msg);
    }

    public static void i() {
        printLog(3, null, Constant.DEFAULT_MESSAGE);
    }

    public static void i(Object msg) {
        printLog(3, null, msg);
    }

    public static void i(String tag, Object msg) {
        printLog(3, tag, msg);
    }

    public static void w() {
        printLog(4, null, Constant.DEFAULT_MESSAGE);
    }

    public static void w(Object msg) {
        printLog(4, null, msg);
    }

    public static void w(String tag, Object msg) {
        printLog(4, tag, msg);
    }

    public static void e() {
        printLog(5, null, Constant.DEFAULT_MESSAGE);
    }

    public static void e(Object msg) {
        printLog(5, null, msg);
    }

    public static void e(String tag, Object msg) {
        printLog(5, tag, msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        Log.e(tag, msg, tr);
    }

    public static void a() {
        printLog(6, null, Constant.DEFAULT_MESSAGE);
    }

    public static void a(Object msg) {
        printLog(6, null, msg);
    }

    public static void a(String tag, Object msg) {
        printLog(6, tag, msg);
    }

    public static void json(String jsonFormat) {
        printLog(7, null, jsonFormat);
    }

    public static void json(String tag, String jsonFormat) {
        printLog(7, tag, jsonFormat);
    }

    public static void xml(String xml) {
        printLog(8, null, xml);
    }

    public static void xml(String tag, String xml) {
        printLog(8, tag, xml);
    }

    public static void file(File targetDirectory, Object msg) {
        printFile(null, targetDirectory, null, msg);
    }

    public static void file(String tag, File targetDirectory, Object msg) {
        printFile(tag, targetDirectory, null, msg);
    }

    public static void file(String tag, File targetDirectory, String fileName, Object msg) {
        printFile(tag, targetDirectory, fileName, msg);
    }

    private static void printLog(int type, String tagStr, Object objectMsg) {
        if (IS_SHOW_LOG) {
            String[] contents = wrapperContent(tagStr, objectMsg);
            String tag = contents[0];
            String msg = contents[1];
            String headString = contents[2];
            switch (type) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    BaseLog.printDefault(type, tag, headString + msg);
                    return;
                case 7:
                    JsonLog.printJson(tag, msg, headString);
                    return;
                case 8:
                    XmlLog.printXml(tag, msg, headString);
                    return;
                default:
                    return;
            }
        }
    }

    private static void printFile(String tagStr, File targetDirectory, String fileName, Object objectMsg) {
        if (IS_SHOW_LOG) {
            String[] contents = wrapperContent(tagStr, objectMsg);
            FileLog.printFile(contents[0], targetDirectory, fileName, contents[2], contents[1]);
        }
    }

    private static String[] wrapperContent(String tagStr, Object objectMsg) {
        String tag;
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String className = stackTrace[5].getFileName();
        String methodName = stackTrace[5].getMethodName();
        int lineNumber = stackTrace[5].getLineNumber();
        String methodNameShort = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
        if (tagStr == null) {
            tag = className;
        } else {
            tag = tagStr;
        }
        String msg = objectMsg == null ? Constant.NULL_TIPS : objectMsg.toString();
        String headString = String.format("[ # (%1$s:%2$s) # %3$s ] ", new Object[]{className, Integer.valueOf(lineNumber), methodNameShort});
        return new String[]{tag, msg, headString};
    }
}