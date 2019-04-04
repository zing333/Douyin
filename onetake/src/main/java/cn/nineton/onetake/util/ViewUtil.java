package cn.nineton.onetake.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build.VERSION;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.bean.collection.CollectionBean;
//import com.blink.academy.onetake.bean.timeline.TimelineBean;
//import com.blink.academy.onetake.fresco.configs.imagepipeline.AppPhotoConfig;
//import com.blink.academy.onetake.fresco.view.AvatarFrameView;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.ui.adapter.tab.me.FrameImageView;
//import com.blink.academy.onetake.widgets.IOSDialog.IOSAlertDialog;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Timer;

import cn.nineton.onetake.App;
import cn.nineton.onetake.R;
import cn.nineton.onetake.bean.CollectionBean;
import cn.nineton.onetake.bean.TimelineBean;
import cn.nineton.onetake.widget.AvatarFrameView;
import cn.nineton.onetake.widget.FrameImageView;
import cn.nineton.onetake.widget.StaticLayoutUtil;

public class ViewUtil {
    public static int ScreenHight = DensityUtil.getMetricsHeight(App.getContext());
    public static int ScreenWidth = DensityUtil.getMetricsWidth(App.getContext());

    public static void setViewHeight(View view, int height) {
        MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    public static void setViewTopMargin(View view, int marginTop) {
        LayoutParams topMarginParams = (LayoutParams) view.getLayoutParams();
        topMarginParams.topMargin = marginTop;
        view.setLayoutParams(topMarginParams);
    }

    public static void setCursorVisible(EditText editText, boolean isCursorVisible) {
        editText.setCursorVisible(isCursorVisible);
        if (VERSION.SDK_INT >= 14) {
            Class<? extends EditText> editTextClass = editText.getClass();
            if (editTextClass != null) {
                Class<? extends TextView> textViewClass = (Class<? extends TextView>) editTextClass.getSuperclass();
                if (textViewClass != null) {
                    Field mCursorDrawableField = null;
                    try {
                        mCursorDrawableField = textViewClass.getDeclaredField("mCursorDrawableRes");
                    } catch (NoSuchFieldException e) {
                    }
                    if (mCursorDrawableField != null) {
                        mCursorDrawableField.setAccessible(isCursorVisible);
                        try {
                            mCursorDrawableField.set(editText, Integer.valueOf(0));
                        } catch (IllegalArgumentException e2) {
                        } catch (IllegalAccessException e3) {
                        } catch (NotFoundException e4) {
                        }
                    }
                }
            }
        }
    }

    public static void setSquareImageView(ImageView imageView) {
        MarginLayoutParams layoutParams = (MarginLayoutParams) imageView.getLayoutParams();
        layoutParams.width = ScreenWidth;
        layoutParams.height = ScreenWidth;
        imageView.setLayoutParams(layoutParams);
    }

    public static int getColorByString(String colorInfo) {
        if (!TextUtil.isValidate(colorInfo) || colorInfo.length() < 3) {
            colorInfo = "000000";
        } else {
            colorInfo = colorInfo.substring(2, colorInfo.length());
            int colorLength = colorInfo.length();
            if (colorLength < 6) {
                for (int i = 0; i < 6 - colorLength; i++) {
                    colorInfo = colorInfo + "F";
                }
            }
        }
        return Color.parseColor(String.format("#%1$s", new Object[]{colorInfo}));
    }

    public static void setViewBgColor(View view, String colorInfo) {
        if (!TextUtil.isValidate(colorInfo) || colorInfo.length() < 3) {
            colorInfo = "FFFFFF";
        } else {
            colorInfo = colorInfo.substring(2, colorInfo.length());
            int colorLength = colorInfo.length();
            if (colorLength < 6) {
                for (int i = 0; i < 6 - colorLength; i++) {
                    colorInfo = colorInfo + "F";
                }
            }
        }
        view.setBackgroundColor(Color.parseColor(String.format("#%1$s", new Object[]{colorInfo})));
    }

    public static void setFrescoImageBgColor(SimpleDraweeView simpleDraweeView, String colorInfo) {
        if (simpleDraweeView != null) {
            if (!TextUtil.isValidate(colorInfo) || colorInfo.length() < 3) {
                colorInfo = "FFFFFF";
            } else {
                colorInfo = colorInfo.substring(2, colorInfo.length());
                int colorLength = colorInfo.length();
                if (colorLength < 6) {
                    for (int i = 0; i < 6 - colorLength; i++) {
                        colorInfo = colorInfo + "F";
                    }
                }
            }
            GenericDraweeHierarchy hierarchy = (GenericDraweeHierarchy) simpleDraweeView.getHierarchy();
            hierarchy.setPlaceholderImage(new ColorDrawable(Color.parseColor(String.format("#%1$s", new Object[]{colorInfo}))));
            simpleDraweeView.setHierarchy(hierarchy);
        }
    }

    public static void setFrescoImageBgRes(SimpleDraweeView simpleDraweeView, int resId) {
        if (simpleDraweeView != null) {
            GenericDraweeHierarchy hierarchy = (GenericDraweeHierarchy) simpleDraweeView.getHierarchy();
            if (TextUtil.isValidate(resId)) {
                hierarchy.setPlaceholderImage(resId);
            } else {
                hierarchy.setPlaceholderImage(new ColorDrawable(Color.parseColor(String.format("#%1$s", new Object[]{"FFFFFF"}))));
            }
            simpleDraweeView.setHierarchy(hierarchy);
        }
    }

    public static void setFrameImageViewUrl(FrameImageView frameImageView, String longThumbnailUrl, float itemWidth) {
        if (frameImageView != null && !TextUtil.isNull(longThumbnailUrl) && itemWidth != StaticLayoutUtil.DefaultSpacingadd) {
            frameImageView.setUrl(ImageUtil.getLongThumbnailUrl(longThumbnailUrl), itemWidth);
        }
    }

    public static void setBigFrameImageViewUrl(FrameImageView frameImageView, String longThumbnailUrl, float itemWidth) {
        if (frameImageView != null && !TextUtil.isNull(longThumbnailUrl) && itemWidth != StaticLayoutUtil.DefaultSpacingadd) {
            frameImageView.setUrl(ImageUtil.getCollectionBigThumbnailUrl(longThumbnailUrl), itemWidth, 800.0f, true);
        }
    }

    public static void setAvatarFrameViewUrl(AvatarFrameView avatarFrameView, String user_avatar, int gender, float itemSize) {
        if (avatarFrameView != null && !TextUtil.isNull(user_avatar) && itemSize != StaticLayoutUtil.DefaultSpacingadd) {
            avatarFrameView.setImageUrl(ImageUtil.getAvatorUrl(user_avatar), gender, itemSize);
        }
    }

    public static void setCollectionCoverUrl(SimpleDraweeView simpleDraweeView, String cover) {
        if (simpleDraweeView != null && !TextUtil.isNull(cover)) {
            simpleDraweeView.setController(AppPhotoConfig.getMainPhotoConfig(ImageUtil.getCollectionCoverUrl(cover), simpleDraweeView, new BaseControllerListener()));
        }
    }

    public static void setDiscoverCollectionViewContent(TextView collection_title, TextView collection_s_title, TextView collection_unread_count_tv, FrameImageView item_collection_frameimage, AvatarFrameView avatarFrameView, TextView header_user_name, View back1, View back2, CollectionBean bean, Timer timer, int number) {
        collection_title.setText(bean.getTitle_s());
        collection_s_title.setText(bean.getNote_s());
        if (bean.getUnread_count() > 0) {
            if (bean.getUnread_count() >= 100) {
                collection_unread_count_tv.setText("99+");
            } else {
                collection_unread_count_tv.setText(String.valueOf(bean.getUnread_count()));
            }
            collection_unread_count_tv.setVisibility(View.VISIBLE);
        } else {
            collection_unread_count_tv.setVisibility(View.GONE);
        }
        if (item_collection_frameimage.getTimer() == null) {
            item_collection_frameimage.setTimer(timer);
        } else {
            item_collection_frameimage.stop();
        }
        List<TimelineBean> recent_photos = bean.getRecent_photos();
        TimelineBean timelineBean;
        int colorByString;
        if (recent_photos == null || recent_photos.size() <= 0) {
            List<TimelineBean> photos = bean.getPhotos();
            if (photos != null && photos.size() > 0) {
                if (number >= photos.size()) {
                    number = 0;
                }
                timelineBean = (TimelineBean) photos.get(number);
                colorByString = getColorByString(timelineBean.preview_ave_info);
                setViewCornerBackGround(back1, (float) DensityUtil.dip2px(5.0f), colorByString);
                setViewCornerBackGround(back2, (float) DensityUtil.dip2px(5.0f), colorByString);
                setViewBgColor(item_collection_frameimage, timelineBean.preview_ave_info);
                if (TextUtil.isValidate(timelineBean.long_thumbnail_url)) {
                    setFrameImageViewUrl(item_collection_frameimage, timelineBean.long_thumbnail_url, App.getResource().getDimension(R.dimen.discover_collection_image_length));
                } else {
                    item_collection_frameimage.stop();
                }
                setAvatarFrameViewUrl(avatarFrameView, timelineBean.user_avatar, timelineBean.user_gender, App.getResource().getDimension(R.dimen.user_head_avater_20));
                LogUtil.d("preDownloadBitmap", "getPhotos timelineBean.user_avatar : " + timelineBean.user_avatar);
                LogUtil.d("preDownloadBitmap", "getPhotos timelineBean.long_thumbnail_url : " + timelineBean.long_thumbnail_url);
                if (TextUtil.isValidate(timelineBean.user_screen_name)) {
                    header_user_name.setText(timelineBean.user_screen_name);
                    return;
                }
                return;
            }
            return;
        }
        if (number >= recent_photos.size()) {
            number = 0;
        }
        timelineBean = (TimelineBean) recent_photos.get(number);
        colorByString = getColorByString(timelineBean.preview_ave_info);
        setViewCornerBackGround(back1, (float) DensityUtil.dip2px(5.0f), colorByString);
        setViewCornerBackGround(back2, (float) DensityUtil.dip2px(5.0f), colorByString);
        setViewBgColor(item_collection_frameimage, timelineBean.preview_ave_info);
        if (TextUtil.isValidate(timelineBean.long_thumbnail_url)) {
            setFrameImageViewUrl(item_collection_frameimage, timelineBean.long_thumbnail_url, App.getResource().getDimension(R.dimen.discover_collection_image_length));
        } else {
            item_collection_frameimage.stop();
        }
        setAvatarFrameViewUrl(avatarFrameView, timelineBean.user_avatar, timelineBean.user_gender, App.getResource().getDimension(R.dimen.user_head_avater_20));
        LogUtil.d("preDownloadBitmap", "timelineBean.user_avatar : " + timelineBean.user_avatar);
        LogUtil.d("preDownloadBitmap", "timelineBean.long_thumbnail_url : " + timelineBean.long_thumbnail_url);
        if (TextUtil.isValidate(timelineBean.user_screen_name)) {
            header_user_name.setText(timelineBean.user_screen_name);
        }
    }

    public static void setViewCornerBackGround(View view, float radius, int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(color);
        gradientDrawable.setCornerRadius(radius);
        view.setBackground(gradientDrawable);
    }

    public static void onToInstagramButtonClick(Activity activity, final Runnable runnable) {
        if (activity != null && !activity.isDestroyed()) {
//            new IOSAlertDialog(activity).builder().setCancelable(false).setTitle(String.format(activity.getString(R.string.POPUP_TITLE_OPEN_IN_3RD_PARTY), new Object[]{activity.getString(2131296796)})).setPositiveButton(new OnClickListener() {
//                public void onClick(View v) {
//                    runnable.run();
//                }
//            }).setNegativeButtonForRequest().show();
            new AlertDialog.Builder(activity).setCancelable(false)
                    .setTitle(String.format(activity.getString(R.string.POPUP_TITLE_OPEN_IN_3RD_PARTY), activity.getString(R.string.SOCIAL_INSTAGRAM)))
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            runnable.run();
                        }
                    }).show();
        }
    }

    public static boolean judgeViewVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    public static void setViewWidth(View view, int width) {
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(-2, -2);
            }
            layoutParams.width = width;
            view.setLayoutParams(layoutParams);
        }
    }

    public static void setViewWidthAndHeight(View view, int width, int height) {
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(-2, -2);
            }
            if (width >= 0) {
                layoutParams.width = width;
            }
            if (height >= 0) {
                layoutParams.height = height;
            }
            view.setLayoutParams(layoutParams);
        }
    }

    public static void resetViewHeight(View view, int height) {
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(-2, -2);
            }
            layoutParams.height = height;
            view.setLayoutParams(layoutParams);
        }
    }

    public static Bitmap getMagicDrawingCache(View view) {
        if (view.getWidth() + view.getHeight() == 0) {
            view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        }
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }
}