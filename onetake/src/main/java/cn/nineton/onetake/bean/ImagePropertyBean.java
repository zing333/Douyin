package cn.nineton.onetake.bean;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.hardware.camera2.CaptureResult.Key;
import android.location.Location;
import android.os.Build;
import android.support.media.ExifInterface;
//import ExifInterfaceExifInterface;
//import com.blink.academy.onetake.bean.CameraEXIFBean;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.utils.CheckUtil;
//import com.blink.academy.onetake.support.utils.JsonUtil;
//import com.facebook.appevents.AppEventsConstants;
//import com.jaredrummler.android.device.DeviceName;
//import com.xiaomi.mipush.sdk.MiPushClient;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import org.json.JSONObject;

import cn.nineton.onetake.util.CheckUtil;
import cn.nineton.onetake.util.JsonUtil;
import cn.nineton.onetake.util.LogUtil;

public class ImagePropertyBean {
    public static final String[] EXIF_TAG_ALL_KEY = new String[]{ExifInterface.TAG_BITS_PER_SAMPLE, ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT, ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, ExifInterface.TAG_PLANAR_CONFIGURATION, ExifInterface.TAG_REFERENCE_BLACK_WHITE, ExifInterface.TAG_ROWS_PER_STRIP, ExifInterface.TAG_SAMPLES_PER_PIXEL, ExifInterface.TAG_STRIP_BYTE_COUNTS, ExifInterface.TAG_STRIP_OFFSETS, ExifInterface.TAG_Y_CB_CR_COEFFICIENTS, ExifInterface.TAG_Y_CB_CR_POSITIONING, ExifInterface.TAG_Y_CB_CR_SUB_SAMPLING, ExifInterface.TAG_F_NUMBER, ExifInterface.TAG_NEW_SUBFILE_TYPE, ExifInterface.TAG_OECF, ExifInterface.TAG_SUBFILE_TYPE, ExifInterface.TAG_INTEROPERABILITY_INDEX, ExifInterface.TAG_THUMBNAIL_IMAGE_LENGTH, ExifInterface.TAG_THUMBNAIL_IMAGE_WIDTH, ExifInterface.TAG_DNG_VERSION, ExifInterface.TAG_DEFAULT_CROP_SIZE, ExifInterface.TAG_ORF_THUMBNAIL_IMAGE, ExifInterface.TAG_ORF_PREVIEW_IMAGE_START, ExifInterface.TAG_ORF_PREVIEW_IMAGE_LENGTH, ExifInterface.TAG_ORF_ASPECT_FRAME, ExifInterface.TAG_RW2_SENSOR_BOTTOM_BORDER, ExifInterface.TAG_RW2_SENSOR_LEFT_BORDER, ExifInterface.TAG_RW2_SENSOR_RIGHT_BORDER, ExifInterface.TAG_RW2_SENSOR_TOP_BORDER, ExifInterface.TAG_RW2_ISO, ExifInterface.TAG_RW2_JPG_FROM_RAW, ExifInterface.TAG_COMPRESSION, ExifInterface.TAG_PHOTOMETRIC_INTERPRETATION, ExifInterface.TAG_IMAGE_DESCRIPTION, ExifInterface.TAG_MAKE, ExifInterface.TAG_MODEL, ExifInterface.TAG_ORIENTATION, ExifInterface.TAG_X_RESOLUTION, ExifInterface.TAG_Y_RESOLUTION, ExifInterface.TAG_RESOLUTION_UNIT, ExifInterface.TAG_SOFTWARE, ExifInterface.TAG_TRANSFER_FUNCTION, ExifInterface.TAG_DATETIME, ExifInterface.TAG_ARTIST, ExifInterface.TAG_COPYRIGHT, ExifInterface.TAG_WHITE_POINT, ExifInterface.TAG_PRIMARY_CHROMATICITIES, ExifInterface.TAG_GPS_ALTITUDE, ExifInterface.TAG_GPS_ALTITUDE_REF, ExifInterface.TAG_GPS_AREA_INFORMATION, ExifInterface.TAG_GPS_DOP, ExifInterface.TAG_GPS_DATESTAMP, ExifInterface.TAG_GPS_DEST_BEARING, ExifInterface.TAG_GPS_DEST_BEARING_REF, ExifInterface.TAG_GPS_DEST_DISTANCE, ExifInterface.TAG_GPS_DEST_DISTANCE_REF, ExifInterface.TAG_GPS_DEST_LATITUDE, ExifInterface.TAG_GPS_DEST_LATITUDE_REF, ExifInterface.TAG_GPS_DEST_LONGITUDE, ExifInterface.TAG_GPS_DEST_LONGITUDE_REF, ExifInterface.TAG_GPS_DIFFERENTIAL, ExifInterface.TAG_GPS_IMG_DIRECTION, ExifInterface.TAG_GPS_IMG_DIRECTION_REF, ExifInterface.TAG_GPS_LATITUDE, ExifInterface.TAG_GPS_LATITUDE_REF, ExifInterface.TAG_GPS_LONGITUDE, ExifInterface.TAG_GPS_LONGITUDE_REF, ExifInterface.TAG_GPS_MAP_DATUM, ExifInterface.TAG_GPS_MEASURE_MODE, ExifInterface.TAG_GPS_PROCESSING_METHOD, ExifInterface.TAG_GPS_SATELLITES, ExifInterface.TAG_GPS_SPEED, ExifInterface.TAG_GPS_SPEED_REF, ExifInterface.TAG_GPS_STATUS, ExifInterface.TAG_GPS_TIMESTAMP, ExifInterface.TAG_GPS_TRACK, ExifInterface.TAG_GPS_TRACK_REF, ExifInterface.TAG_GPS_VERSION_ID, ExifInterface.TAG_EXPOSURE_TIME, ExifInterface.TAG_EXPOSURE_PROGRAM, ExifInterface.TAG_SPECTRAL_SENSITIVITY, ExifInterface.TAG_ISO_SPEED_RATINGS, ExifInterface.TAG_EXIF_VERSION, ExifInterface.TAG_DATETIME_ORIGINAL, ExifInterface.TAG_DATETIME_DIGITIZED, ExifInterface.TAG_COMPONENTS_CONFIGURATION, ExifInterface.TAG_COMPRESSED_BITS_PER_PIXEL, ExifInterface.TAG_SHUTTER_SPEED_VALUE, ExifInterface.TAG_APERTURE_VALUE, ExifInterface.TAG_BRIGHTNESS_VALUE, ExifInterface.TAG_EXPOSURE_BIAS_VALUE, ExifInterface.TAG_MAX_APERTURE_VALUE, ExifInterface.TAG_SUBJECT_DISTANCE, ExifInterface.TAG_METERING_MODE, ExifInterface.TAG_LIGHT_SOURCE, ExifInterface.TAG_FLASH, ExifInterface.TAG_FOCAL_LENGTH, ExifInterface.TAG_SUBJECT_AREA, ExifInterface.TAG_MAKER_NOTE, ExifInterface.TAG_USER_COMMENT, ExifInterface.TAG_SUBSEC_TIME, ExifInterface.TAG_SUBSEC_TIME_ORIGINAL, ExifInterface.TAG_SUBSEC_TIME_DIGITIZED, ExifInterface.TAG_FLASHPIX_VERSION, ExifInterface.TAG_COLOR_SPACE, ExifInterface.TAG_PIXEL_X_DIMENSION, ExifInterface.TAG_PIXEL_Y_DIMENSION, ExifInterface.TAG_RELATED_SOUND_FILE, ExifInterface.TAG_FLASH_ENERGY, ExifInterface.TAG_SPATIAL_FREQUENCY_RESPONSE, ExifInterface.TAG_FOCAL_PLANE_X_RESOLUTION, ExifInterface.TAG_FOCAL_PLANE_Y_RESOLUTION, ExifInterface.TAG_FOCAL_PLANE_RESOLUTION_UNIT, ExifInterface.TAG_SUBJECT_LOCATION, ExifInterface.TAG_EXPOSURE_INDEX, ExifInterface.TAG_SENSING_METHOD, ExifInterface.TAG_FILE_SOURCE, ExifInterface.TAG_SCENE_TYPE, ExifInterface.TAG_CFA_PATTERN, ExifInterface.TAG_CUSTOM_RENDERED, ExifInterface.TAG_EXPOSURE_MODE, "WhiteBalance", ExifInterface.TAG_DIGITAL_ZOOM_RATIO, ExifInterface.TAG_FOCAL_LENGTH_IN_35MM_FILM, ExifInterface.TAG_SCENE_CAPTURE_TYPE, ExifInterface.TAG_GAIN_CONTROL, "Contrast", "Saturation", ExifInterface.TAG_SHARPNESS, ExifInterface.TAG_DEVICE_SETTING_DESCRIPTION, ExifInterface.TAG_SUBJECT_DISTANCE_RANGE, ExifInterface.TAG_IMAGE_UNIQUE_ID};
    public static final String[] EXIF_TAG_COMMON_KEY = new String[]{ExifInterface.TAG_BITS_PER_SAMPLE, ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT, ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, ExifInterface.TAG_PLANAR_CONFIGURATION, ExifInterface.TAG_REFERENCE_BLACK_WHITE, ExifInterface.TAG_ROWS_PER_STRIP, ExifInterface.TAG_SAMPLES_PER_PIXEL, ExifInterface.TAG_STRIP_BYTE_COUNTS, ExifInterface.TAG_STRIP_OFFSETS, ExifInterface.TAG_Y_CB_CR_COEFFICIENTS, ExifInterface.TAG_Y_CB_CR_POSITIONING, ExifInterface.TAG_Y_CB_CR_SUB_SAMPLING, ExifInterface.TAG_F_NUMBER, ExifInterface.TAG_NEW_SUBFILE_TYPE, ExifInterface.TAG_OECF, ExifInterface.TAG_SUBFILE_TYPE, ExifInterface.TAG_INTEROPERABILITY_INDEX, ExifInterface.TAG_THUMBNAIL_IMAGE_LENGTH, ExifInterface.TAG_THUMBNAIL_IMAGE_WIDTH, ExifInterface.TAG_DNG_VERSION, ExifInterface.TAG_DEFAULT_CROP_SIZE, ExifInterface.TAG_ORF_THUMBNAIL_IMAGE, ExifInterface.TAG_ORF_PREVIEW_IMAGE_START, ExifInterface.TAG_ORF_PREVIEW_IMAGE_LENGTH, ExifInterface.TAG_ORF_ASPECT_FRAME, ExifInterface.TAG_RW2_SENSOR_BOTTOM_BORDER, ExifInterface.TAG_RW2_SENSOR_LEFT_BORDER, ExifInterface.TAG_RW2_SENSOR_RIGHT_BORDER, ExifInterface.TAG_RW2_SENSOR_TOP_BORDER, ExifInterface.TAG_RW2_ISO, ExifInterface.TAG_RW2_JPG_FROM_RAW};
    public static final String[] EXIF_TAG_EXIF_KEY = new String[]{ExifInterface.TAG_EXPOSURE_TIME, ExifInterface.TAG_EXPOSURE_PROGRAM, ExifInterface.TAG_SPECTRAL_SENSITIVITY, ExifInterface.TAG_ISO_SPEED_RATINGS, ExifInterface.TAG_EXIF_VERSION, ExifInterface.TAG_DATETIME_ORIGINAL, ExifInterface.TAG_DATETIME_DIGITIZED, ExifInterface.TAG_COMPONENTS_CONFIGURATION, ExifInterface.TAG_COMPRESSED_BITS_PER_PIXEL, ExifInterface.TAG_SHUTTER_SPEED_VALUE, ExifInterface.TAG_APERTURE_VALUE, ExifInterface.TAG_BRIGHTNESS_VALUE, ExifInterface.TAG_EXPOSURE_BIAS_VALUE, ExifInterface.TAG_MAX_APERTURE_VALUE, ExifInterface.TAG_SUBJECT_DISTANCE, ExifInterface.TAG_METERING_MODE, ExifInterface.TAG_LIGHT_SOURCE, ExifInterface.TAG_FLASH, ExifInterface.TAG_FOCAL_LENGTH, ExifInterface.TAG_SUBJECT_AREA, ExifInterface.TAG_MAKER_NOTE, ExifInterface.TAG_USER_COMMENT, ExifInterface.TAG_SUBSEC_TIME, ExifInterface.TAG_SUBSEC_TIME_ORIGINAL, ExifInterface.TAG_SUBSEC_TIME_DIGITIZED, ExifInterface.TAG_FLASHPIX_VERSION, ExifInterface.TAG_COLOR_SPACE, ExifInterface.TAG_PIXEL_X_DIMENSION, ExifInterface.TAG_PIXEL_Y_DIMENSION, ExifInterface.TAG_RELATED_SOUND_FILE, ExifInterface.TAG_FLASH_ENERGY, ExifInterface.TAG_SPATIAL_FREQUENCY_RESPONSE, ExifInterface.TAG_FOCAL_PLANE_X_RESOLUTION, ExifInterface.TAG_FOCAL_PLANE_Y_RESOLUTION, ExifInterface.TAG_FOCAL_PLANE_RESOLUTION_UNIT, ExifInterface.TAG_SUBJECT_LOCATION, ExifInterface.TAG_EXPOSURE_INDEX, ExifInterface.TAG_SENSING_METHOD, ExifInterface.TAG_FILE_SOURCE, ExifInterface.TAG_SCENE_TYPE, ExifInterface.TAG_CFA_PATTERN, ExifInterface.TAG_CUSTOM_RENDERED, ExifInterface.TAG_EXPOSURE_MODE, "WhiteBalance", ExifInterface.TAG_DIGITAL_ZOOM_RATIO, ExifInterface.TAG_FOCAL_LENGTH_IN_35MM_FILM, ExifInterface.TAG_SCENE_CAPTURE_TYPE, ExifInterface.TAG_GAIN_CONTROL, "Contrast", "Saturation", ExifInterface.TAG_SHARPNESS, ExifInterface.TAG_DEVICE_SETTING_DESCRIPTION, ExifInterface.TAG_SUBJECT_DISTANCE_RANGE, ExifInterface.TAG_IMAGE_UNIQUE_ID};
    public static final String[] EXIF_TAG_GPS_KEY = new String[]{ExifInterface.TAG_GPS_ALTITUDE, ExifInterface.TAG_GPS_ALTITUDE_REF, ExifInterface.TAG_GPS_AREA_INFORMATION, ExifInterface.TAG_GPS_DOP, ExifInterface.TAG_GPS_DATESTAMP, ExifInterface.TAG_GPS_DEST_BEARING, ExifInterface.TAG_GPS_DEST_BEARING_REF, ExifInterface.TAG_GPS_DEST_DISTANCE, ExifInterface.TAG_GPS_DEST_DISTANCE_REF, ExifInterface.TAG_GPS_DEST_LATITUDE, ExifInterface.TAG_GPS_DEST_LATITUDE_REF, ExifInterface.TAG_GPS_DEST_LONGITUDE, ExifInterface.TAG_GPS_DEST_LONGITUDE_REF, ExifInterface.TAG_GPS_DIFFERENTIAL, ExifInterface.TAG_GPS_IMG_DIRECTION, ExifInterface.TAG_GPS_IMG_DIRECTION_REF, ExifInterface.TAG_GPS_LATITUDE, ExifInterface.TAG_GPS_LATITUDE_REF, ExifInterface.TAG_GPS_LONGITUDE, ExifInterface.TAG_GPS_LONGITUDE_REF, ExifInterface.TAG_GPS_MAP_DATUM, ExifInterface.TAG_GPS_MEASURE_MODE, ExifInterface.TAG_GPS_PROCESSING_METHOD, ExifInterface.TAG_GPS_SATELLITES, ExifInterface.TAG_GPS_SPEED, ExifInterface.TAG_GPS_SPEED_REF, ExifInterface.TAG_GPS_STATUS, ExifInterface.TAG_GPS_TIMESTAMP, ExifInterface.TAG_GPS_TRACK, ExifInterface.TAG_GPS_TRACK_REF, ExifInterface.TAG_GPS_VERSION_ID};
    public static final String[] EXIF_TAG_IPTC_KEY = new String[]{ExifInterface.TAG_COPYRIGHT, ExifInterface.TAG_SUBSEC_TIME_DIGITIZED, ExifInterface.TAG_SUBSEC_TIME_ORIGINAL, ExifInterface.TAG_IMAGE_DESCRIPTION};
    public static final String[] EXIF_TAG_TIFF_KEY = new String[]{ExifInterface.TAG_COMPRESSION, ExifInterface.TAG_PHOTOMETRIC_INTERPRETATION, ExifInterface.TAG_IMAGE_DESCRIPTION, ExifInterface.TAG_MAKE, ExifInterface.TAG_MODEL, ExifInterface.TAG_ORIENTATION, ExifInterface.TAG_X_RESOLUTION, ExifInterface.TAG_Y_RESOLUTION, ExifInterface.TAG_RESOLUTION_UNIT, ExifInterface.TAG_SOFTWARE, ExifInterface.TAG_TRANSFER_FUNCTION, ExifInterface.TAG_DATETIME, ExifInterface.TAG_ARTIST, ExifInterface.TAG_COPYRIGHT, ExifInterface.TAG_WHITE_POINT, ExifInterface.TAG_PRIMARY_CHROMATICITIES};
    private static ImagePropertyBean ImagePropertyBean;
    private static final Pattern sNonZeroTimePattern = Pattern.compile(".*[1-9].*");
    String cameraPictureTime = "";
    private byte[] dataBytes;
    private String filterName = "NONE";
    private String imagePath;
    private Map<String, String> imagePropertyAllMap = new HashMap();
    private Map<String, String> imagePropertyCommonMap = new HashMap();
    private Map<String, String> imagePropertyEXIFMap = new HashMap();
    private Map<String, String> imagePropertyGPSMap = new HashMap();
    private Map<String, String> imagePropertyIPTCMap = new HashMap();
    private HashMap<String, String> imagePropertyTIFFMap = new HashMap();
    private int mOrientation;
    private SimpleDateFormat sFormatter = null;

    private ImagePropertyBean() {
    }

    public static ImagePropertyBean getInstance() {
        if (ImagePropertyBean == null) {
            ImagePropertyBean = new ImagePropertyBean();
        }
        return ImagePropertyBean;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath) throws IOException {
        this.imagePath = imagePath;
        readGropTag();
    }

    public void setImagePropertyTIFFMap(HashMap<String, String> imagePropertyTIFFMap) {
        this.imagePropertyTIFFMap = imagePropertyTIFFMap;
    }

    public void setImagePropertyIPTCMap(HashMap<String, String> imagePropertyIPTCMap) {
        this.imagePropertyIPTCMap = imagePropertyIPTCMap;
    }

    public void setImagePropertyEXIFMap(HashMap<String, String> imagePropertyEXIFMap) {
        this.imagePropertyEXIFMap = imagePropertyEXIFMap;
    }

    public void setImagePropertyCommonMap(HashMap<String, String> imagePropertyCommonMap) {
        this.imagePropertyCommonMap = imagePropertyCommonMap;
    }

    public void setImagePropertyGPSMap(HashMap<String, String> imagePropertyGPSMap) {
        this.imagePropertyGPSMap = imagePropertyGPSMap;
    }

    public Map<String, String> getImagePropertyAllMap() {
        return this.imagePropertyAllMap;
    }

    public void setImagePropertyAllMap(HashMap<String, String> imagePropertyAllMap) {
        this.imagePropertyAllMap = imagePropertyAllMap;
    }

    public Map<String, String> getImagePropertyTIFFMap() {
        return this.imagePropertyTIFFMap;
    }

    public Map<String, String> getImagePropertyIPTCMap() {
        return this.imagePropertyIPTCMap;
    }

    public Map<String, String> getImagePropertyEXIFMap() {
        return this.imagePropertyEXIFMap;
    }

    public Map<String, String> getImagePropertyCommonMap() {
        return this.imagePropertyCommonMap;
    }

    public Map<String, String> getImagePropertyGPSMap() {
        return this.imagePropertyGPSMap;
    }

    public void clear() {
        clearMap(this.imagePropertyEXIFMap);
        clearMap(this.imagePropertyIPTCMap);
        clearMap(this.imagePropertyTIFFMap);
        clearMap(this.imagePropertyCommonMap);
        clearMap(this.imagePropertyGPSMap);
        clearBitmapArrayData();
        this.imagePath = "";
    }

    public void clearAllMap() {
        clearMap(this.imagePropertyAllMap);
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public String getFilterName() {
        return this.filterName;
    }

    public void readAllTag() {
        File file = new File(this.imagePath);
        if (file.exists()) {
            try {
                ExifInterface exifInterface = new ExifInterface(file.getAbsolutePath());
                HashMap<String, String> allMaps = new HashMap();
                for (int i = 0; i < EXIF_TAG_ALL_KEY.length; i++) {
                    String attribute = exifInterface.getAttribute(EXIF_TAG_ALL_KEY[i]);
                    if (attribute != null) {
                        allMaps.put(EXIF_TAG_ALL_KEY[i], attribute + "");
                    }
                }
                setImagePropertyAllMap(allMaps);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void readGropTag(String imagePath, boolean isFront) throws IOException {
        this.imagePath = imagePath;
        File file = new File(imagePath);
        if (file.exists()) {
            ExifInterface exifInterface = new ExifInterface(file.getAbsolutePath());
            LogUtil.d("mOrientation", "mOrientation : " + this.mOrientation);
            if (isFront) {
                if (this.mOrientation == 0) {
                    exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "8");
                } else if (this.mOrientation == 90) {
                    exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "3");
                } else if (this.mOrientation == 180) {
                    exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "6");
                } else if (this.mOrientation == 270) {
                    exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "1");
                }
            } else if (this.mOrientation == 0) {
                exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "6");
            } else if (this.mOrientation == 90) {
                exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "3");
            } else if (this.mOrientation == 180) {
                exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "8");
            } else if (this.mOrientation == 270) {
                exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "1");
            }
            if (this.imagePropertyAllMap != null && this.imagePropertyAllMap.size() > 0) {
                double latValue = 0.0d;
                double longValue = 0.0d;
                boolean hasValue = false;
                for (Entry<String, String> next : this.imagePropertyAllMap.entrySet()) {
                    String key = (String) next.getKey();
                    String value = (String) next.getValue();
                    if (key.equalsIgnoreCase(ExifInterface.TAG_GPS_LONGITUDE)) {
                        try {
                            longValue = Double.valueOf(value).doubleValue();
                            hasValue = true;
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    } else if (key.equalsIgnoreCase(ExifInterface.TAG_GPS_LATITUDE)) {
                        try {
                            latValue = Double.valueOf(value).doubleValue();
                            hasValue = true;
                        } catch (NumberFormatException e2) {
                            e2.printStackTrace();
                        }
                    } else if (key.equalsIgnoreCase(ExifInterface.TAG_GPS_DATESTAMP)) {
                        exifInterface.setAttribute(key, value);
                    } else if (key.equalsIgnoreCase(ExifInterface.TAG_GPS_TIMESTAMP)) {
                        exifInterface.setAttribute(key, value);
                    } else if (key.equalsIgnoreCase(ExifInterface.TAG_GPS_SPEED)) {
                        exifInterface.setAttribute(key, value);
                    } else if (key.equalsIgnoreCase(ExifInterface.TAG_GPS_ALTITUDE)) {
                        exifInterface.setAttribute(key, value);
                    } else if (key.equalsIgnoreCase(ExifInterface.TAG_GPS_SPEED_REF)) {
                        exifInterface.setAttribute(key, value);
                    }
                }
                this.imagePropertyAllMap.clear();
                if (hasValue) {
                    exifInterface.setLatLong(latValue, longValue);
                }
            }
            exifInterface.saveAttributes();
            readGropTag();
        }
    }

    public long getDateTime() {
        IOException e;
        File file = new File(this.imagePath);
        if (!file.exists()) {
            return -1;
        }
        try {
            ExifInterface exifInterface = new ExifInterface(this.imagePath);
            ExifInterface exifInterface2;
            @SuppressLint("RestrictedApi") long dateTime = exifInterface.getDateTime();
            if (dateTime == -1) {
                dateTime = file.lastModified();
                if (0 == dateTime) {
                    dateTime = System.currentTimeMillis();
                }
            }
            exifInterface2 = exifInterface;
            return dateTime;
        } catch (IOException e3) {
            e = e3;
            e.printStackTrace();
            return System.currentTimeMillis();
        }
    }

    public void readGropTag() throws IOException {
        File file = new File(this.imagePath);
        if (file.exists()) {
            int i;
            String attribute;
            ExifInterface exifInterface = new ExifInterface(file.getAbsolutePath());
            HashMap<String, String> commonMaps = new HashMap();
            for (i = 0; i < EXIF_TAG_COMMON_KEY.length; i++) {
                attribute = exifInterface.getAttribute(EXIF_TAG_COMMON_KEY[i]);
                if (attribute != null) {
                    commonMaps.put(EXIF_TAG_COMMON_KEY[i], attribute + "");
                }
            }
            setImagePropertyCommonMap(commonMaps);
            HashMap<String, String> tiffMaps = new HashMap();
            for (i = 0; i < EXIF_TAG_TIFF_KEY.length; i++) {
                attribute = exifInterface.getAttribute(EXIF_TAG_TIFF_KEY[i]);
                if (attribute != null) {
                    tiffMaps.put(EXIF_TAG_TIFF_KEY[i], attribute + "");
                }
            }
            setImagePropertyTIFFMap(tiffMaps);
            HashMap<String, String> iptcMaps = new HashMap();
            for (i = 0; i < EXIF_TAG_IPTC_KEY.length; i++) {
                attribute = exifInterface.getAttribute(EXIF_TAG_IPTC_KEY[i]);
                if (attribute != null) {
                    iptcMaps.put(EXIF_TAG_IPTC_KEY[i], attribute + "");
                }
            }
            setImagePropertyIPTCMap(iptcMaps);
            HashMap<String, String> exifMaps = new HashMap();
            for (i = 0; i < EXIF_TAG_EXIF_KEY.length; i++) {
                attribute = exifInterface.getAttribute(EXIF_TAG_EXIF_KEY[i]);
                if (attribute != null) {
                    exifMaps.put(EXIF_TAG_EXIF_KEY[i], attribute + "");
                }
            }
            setImagePropertyEXIFMap(exifMaps);
            HashMap<String, String> gpsMaps = new HashMap();
            for (i = 0; i < EXIF_TAG_GPS_KEY.length; i++) {
                attribute = exifInterface.getAttribute(EXIF_TAG_GPS_KEY[i]);
                if (attribute != null) {
                    double[] latLong;
                    if (EXIF_TAG_GPS_KEY[i].equalsIgnoreCase(ExifInterface.TAG_GPS_LATITUDE)) {
                        latLong = exifInterface.getLatLong();
                        if (latLong != null && latLong.length >= 2) {
                            gpsMaps.put(EXIF_TAG_GPS_KEY[i], String.valueOf(latLong[0]));
                        }
                    } else if (EXIF_TAG_GPS_KEY[i].equalsIgnoreCase(ExifInterface.TAG_GPS_LONGITUDE)) {
                        latLong = exifInterface.getLatLong();
                        if (latLong != null && latLong.length >= 2) {
                            gpsMaps.put(EXIF_TAG_GPS_KEY[i], String.valueOf(latLong[1]));
                        }
                    } else {
                        gpsMaps.put(EXIF_TAG_GPS_KEY[i], attribute + "");
                    }
                }
            }
            setImagePropertyGPSMap(gpsMaps);
            HashMap<String, String> allMaps = new HashMap();
            for (i = 0; i < EXIF_TAG_ALL_KEY.length; i++) {
                attribute = exifInterface.getAttribute(EXIF_TAG_ALL_KEY[i]);
                if (attribute != null) {
                    allMaps.put(EXIF_TAG_ALL_KEY[i], attribute + "");
                }
            }
            setImagePropertyAllMap(allMaps);
        }
    }

    public String getMapPragram() {
        HashMap<String, Object> imagePropertyParams = new HashMap();
        if (mapIsValidate(getImagePropertyCommonMap())) {
            for (String commonKey : getImagePropertyCommonMap().keySet()) {
                imagePropertyParams.put(commonKey, getImagePropertyCommonMap().get(commonKey));
            }
        }
        if (mapIsValidate(getImagePropertyEXIFMap())) {
            imagePropertyParams.put("Exif", getImagePropertyEXIFMap());
        }
        if (mapIsValidate(getImagePropertyGPSMap())) {
            imagePropertyParams.put("GPS", getImagePropertyGPSMap());
        }
        if (mapIsValidate(getImagePropertyIPTCMap())) {
            imagePropertyParams.put("IPTC", getImagePropertyIPTCMap());
        }
        if (mapIsValidate(getImagePropertyTIFFMap())) {
            imagePropertyParams.put("TIFF", getImagePropertyTIFFMap());
        }
        return JsonUtil.mapToJsonString(imagePropertyParams);
    }

    public JSONObject getImagePropertyObject() {
        HashMap<String, Object> imagePropertyParams = new HashMap();
        if (mapIsValidate(getImagePropertyCommonMap())) {
            for (String commonKey : getImagePropertyCommonMap().keySet()) {
                imagePropertyParams.put(commonKey, getImagePropertyCommonMap().get(commonKey));
            }
        }
        if (mapIsValidate(getImagePropertyEXIFMap())) {
            imagePropertyParams.put("Exif", getImagePropertyEXIFMap());
        }
        if (mapIsValidate(getImagePropertyGPSMap())) {
            imagePropertyParams.put("GPS", getImagePropertyGPSMap());
        }
        if (mapIsValidate(getImagePropertyIPTCMap())) {
            imagePropertyParams.put("IPTC", getImagePropertyIPTCMap());
        }
        if (mapIsValidate(getImagePropertyTIFFMap())) {
            imagePropertyParams.put("TIFF", getImagePropertyTIFFMap());
        }
        return new JSONObject(imagePropertyParams);
    }

    private boolean mapIsValidate(Map<String, String> map) {
        return (map == null || map.keySet().size() == 0) ? false : true;
    }

    private void clearMap(Map<String, String> map) {
        if (map != null) {
            map.clear();
        }
    }

    @TargetApi(21)
    public void setCameraInfoToEXIFmap(Key key, Object values) {
        String keyName = key.getName();
        String value = values.toString();
        if (keyName.contains("Aperture")) {
            value = Double.parseDouble(value) + "/100";
            this.imagePropertyAllMap.put(ExifInterface.TAG_MAX_APERTURE_VALUE, value);
            this.imagePropertyAllMap.put(ExifInterface.TAG_APERTURE_VALUE, value);
        } else if (keyName.contains(ExifInterface.TAG_FOCAL_LENGTH)) {
            this.imagePropertyAllMap.put(ExifInterface.TAG_FOCAL_LENGTH, Double.parseDouble(value) + "/100");
        } else if (keyName.contains("android.flash.mode")) {
            this.imagePropertyAllMap.put(ExifInterface.TAG_FLASH, value);
        } else if (keyName.contains("android.flash.state ")) {
            this.imagePropertyAllMap.put(ExifInterface.TAG_FLASH_ENERGY, value);
        } else if (keyName.contains("android.sensor.exposureTime")) {
            this.imagePropertyAllMap.put(ExifInterface.TAG_EXPOSURE_TIME, new DecimalFormat("0.00").format(Double.parseDouble(value) * Math.pow(10.0d, -10.0d)));
        } else if (keyName.contains("android.control.aeMode")) {
            this.imagePropertyAllMap.put(ExifInterface.TAG_EXPOSURE_MODE, value);
            this.imagePropertyAllMap.put(ExifInterface.TAG_EXPOSURE_PROGRAM, value);
        } else if (keyName.contains("android.control.aeExposureCompensation")) {
            this.imagePropertyAllMap.put(ExifInterface.TAG_EXPOSURE_BIAS_VALUE, "" + (Double.parseDouble(value) * 10.0d) + "/10");
        } else if (keyName.contains("android.control.awbMode")) {
            this.imagePropertyAllMap.put("WhiteBalance", value);
        } else if (keyName.contains("android.jpeg.thumbnailSize")) {
            String[] widthAndHeight = value.split("x");
            this.imagePropertyAllMap.put(ExifInterface.TAG_THUMBNAIL_IMAGE_WIDTH, widthAndHeight[0]);
            this.imagePropertyAllMap.put(ExifInterface.TAG_THUMBNAIL_IMAGE_LENGTH, widthAndHeight[1]);
        } else if (keyName.contains("android.sensor.sensitivity")) {
            this.imagePropertyAllMap.put(ExifInterface.TAG_ISO_SPEED_RATINGS, value);
        } else if (keyName.contains("android.lens.focalLength")) {
            this.imagePropertyAllMap.put(ExifInterface.TAG_FOCAL_LENGTH, "" + (Double.parseDouble(value) * 100.0d) + "/100");
        } else if (keyName.contains("android.lens.aperture")) {
            this.imagePropertyAllMap.put(ExifInterface.TAG_APERTURE_VALUE, "" + (Double.parseDouble(value) * 100.0d) + "/100");
        }
    }

    public void setNormalEXIFinfo(CameraEXIFBean bean) {
        if (this.imagePropertyAllMap == null) {
            this.imagePropertyAllMap = new HashMap();
        }
        this.imagePropertyAllMap.put(ExifInterface.TAG_COMPRESSION, "1");
        this.imagePropertyAllMap.put(ExifInterface.TAG_ORIENTATION, "1");
        this.imagePropertyAllMap.put(ExifInterface.TAG_COLOR_SPACE, "1");
        this.cameraPictureTime = getDateStringValue();
        this.imagePropertyAllMap.put(ExifInterface.TAG_DATETIME, this.cameraPictureTime);
        this.imagePropertyAllMap.put(ExifInterface.TAG_DATETIME_DIGITIZED, this.cameraPictureTime);
        this.imagePropertyAllMap.put(ExifInterface.TAG_DATETIME_ORIGINAL, this.cameraPictureTime);
        this.imagePropertyAllMap.put(ExifInterface.TAG_MAKE, "Onetake");
        this.imagePropertyAllMap.put(ExifInterface.TAG_SOFTWARE, "Onetake");
        this.imagePropertyAllMap.put(ExifInterface.TAG_MODEL, Build.DEVICE);//DeviceName.getDeviceName());
        setEXIFFlashMode(bean.flashIsOn);
        float[] floats = bean.maxApertureAndFNumber;
        if (!(floats == null || floats.length == 0)) {
            if (floats.length == 1) {
                setMaxApertureAndFNumber(floats[0]);
            } else {
                floats = CheckUtil.bubbleSort(floats);
                setMaxApertureAndFNumber(floats[floats.length - 1]);
            }
        }
        this.imagePropertyAllMap.put(ExifInterface.TAG_PIXEL_X_DIMENSION, String.valueOf(bean.pixelXDimension));
        this.imagePropertyAllMap.put(ExifInterface.TAG_PIXEL_Y_DIMENSION, String.valueOf(bean.pixelYDimension));
    }

    private void setMaxApertureAndFNumber(float value) {
        this.imagePropertyAllMap.put(ExifInterface.TAG_F_NUMBER, String.valueOf(value));
        this.imagePropertyAllMap.put(ExifInterface.TAG_MAX_APERTURE_VALUE, "" + (value * 100.0f) + "/100");
    }

    private void setEXIFFlashMode(boolean flashIsOn) {
        String flashmode;
        if (flashIsOn) {
            flashmode = "1";
        } else {
            flashmode = "0";//AppEventsConstants.EVENT_PARAM_VALUE_NO;
        }
        this.imagePropertyAllMap.put(ExifInterface.TAG_FLASH, flashmode);
    }

    public void setGPSInfo(Location location) {
        if (this.imagePropertyAllMap != null) {
            this.imagePropertyAllMap.clear();
        } else {
            this.imagePropertyAllMap = new HashMap();
        }
        if ("".equalsIgnoreCase(this.cameraPictureTime)) {
            this.cameraPictureTime = getDateStringValue();
        }
        this.imagePropertyAllMap.put(ExifInterface.TAG_GPS_LONGITUDE, String.valueOf(location.getLongitude()));
        this.imagePropertyAllMap.put(ExifInterface.TAG_GPS_LATITUDE, String.valueOf(location.getLatitude()));
        String[] split = this.cameraPictureTime.split(" ");
        this.imagePropertyAllMap.put(ExifInterface.TAG_GPS_DATESTAMP, split[0]);
        this.imagePropertyAllMap.put(ExifInterface.TAG_GPS_TIMESTAMP, split[1]);
        this.imagePropertyAllMap.put(ExifInterface.TAG_GPS_SPEED, String.valueOf(((int) ((3600.0f * location.getSpeed()) / 1000.0f)) + "/1"));
        this.imagePropertyAllMap.put(ExifInterface.TAG_GPS_ALTITUDE, String.valueOf(((int) (1790.0d * location.getAltitude())) + "/1790"));
        this.imagePropertyAllMap.put(ExifInterface.TAG_GPS_SPEED_REF, "K");
    }

    private String getDateStringValue() {
        return new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(new Date());
    }

    public void setImageDataBytes(byte[] dataBytes, int mOrientation) {
        this.dataBytes = dataBytes;
        this.mOrientation = mOrientation;
    }

    public byte[] getImageDataBytes() {
        return this.dataBytes;
    }

    public void clearBitmapArrayData() {
        this.dataBytes = null;
        System.gc();
    }

    private boolean contentNotRational(String value) {
        boolean isNull;
        if (value == null) {
            isNull = true;
        } else {
            isNull = false;
        }
        boolean isNoContent = "".equals(value);
        boolean isSpecial = ",".equals(value);
        if (isNull || isNoContent || isSpecial) {
            return true;
        }
        return false;
    }

    public void resetPictureExifInfo(String path) {
        try {
            ExifInterface exif = new ExifInterface(path);
            for (Entry<String, String> next : this.imagePropertyAllMap.entrySet()) {
                String key = (String) next.getKey();
                String value = (String) next.getValue();
                if (key.equalsIgnoreCase(ExifInterface.TAG_ORIENTATION)) {
                    value = "1";
                }
                if (!contentNotRational(value)) {
                    exif.setAttribute(key, value);
                }
            }
            String imageDescription = "";
            if ("NONE".equalsIgnoreCase(this.filterName)) {
                imageDescription = "Processed with Onetake.";
            } else {
                imageDescription = String.format("Processed with Onetake with %s preset.", new Object[]{this.filterName});
            }
            exif.setAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION, imageDescription);
            exif.setAttribute(ExifInterface.TAG_COPYRIGHT, "Copyright 2017. All rights reserved");
            exif.setAttribute(ExifInterface.TAG_SOFTWARE, "Onetake");
            exif.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}