package cn.nineton.onetake.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.LeadingMarginSpan.Standard;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
//import com.blink.academy.onetake.App;
//import com.blink.academy.onetake.bean.album.AlbumInfoBean;
//import com.blink.academy.onetake.bean.notification.NotificationBean;
//import com.blink.academy.onetake.bean.timeline.CommentBean;
//import com.blink.academy.onetake.custom.CustomDynamicDrawableSpan;
//import com.blink.academy.onetake.custom.DetailDrawableSpan;
//import com.blink.academy.onetake.custom.LinkSpan;
//import com.blink.academy.onetake.custom.LinkUnderlineSpan;
//import com.blink.academy.onetake.custom.YoutubeDrawableSpan;
//import com.blink.academy.onetake.support.debug.LogUtil;
//import com.blink.academy.onetake.support.helper.GlobalHelper;
//import com.blink.academy.onetake.support.twitter.Extractor;
//import com.blink.academy.onetake.support.twitter.Extractor.Entity;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.1;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.10;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.11;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.12;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.13;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.14;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.15;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.16;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.17;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.18;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.19;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.2;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.20;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.21;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.22;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.23;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.24;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.25;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.26;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.27;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.28;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.29;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.3;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.30;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.31;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.32;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.33;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.34;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.35;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.36;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.37;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.38;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.39;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.4;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.40;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.41;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.42;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.43;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.44;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.45;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.46;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.47;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.48;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.49;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.5;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.50;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.51;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.52;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.53;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.54;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.55;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.56;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.57;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.58;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.6;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.7;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.8;
//import com.blink.academy.onetake.support.utils.SpannedUtil$.Lambda.9;
//import com.blink.academy.onetake.ui.adapter.entities.CommentEntity;
//import com.blink.academy.onetake.ui.adapter.entities.LikeEntity;
//import com.blink.academy.onetake.ui.adapter.entities.TimeLineCardEntity;
//import com.blink.academy.onetake.ui.adapter.entities.UserEntity;
//import com.blink.academy.onetake.widgets.VideoView.LongVideo.JZVideoPlayer;
//import com.blink.academy.onetake.widgets.VideoView.LongVideo.JZVideoPlayerManager;
//import com.facebook.internal.AnalyticsEvents;
import java.util.ArrayList;
import java.util.List;

import cn.nineton.onetake.App;
import cn.nineton.onetake.bean.AlbumInfoBean;
import cn.nineton.onetake.bean.NotificationBean;
import cn.nineton.onetake.widget.LinkSpan;
import cn.nineton.onetake.widget.LinkUnderlineSpan;

public class SpannedUtil {
    public static String Hashtag = "#";
    public static final String IMG_REPLACE_STR = "-";
    public static String Plus = "+";
    public static String at = "@";
    public static String colon = ":";
    public static String comma = ",";
    public static String empty = " ";
    public static int flags = 33;
    public static String leftQuotes = " ";
    private static final Context mContext = App.getContext();
    public static Resources mResource = mContext.getResources();
    public static String semicolon = ";";

//    public static Spannable CommonString(String username, OnClickListener usernameListener, String status) {
//        if (TextUtil.isNull(username)) {
//            return new SpannableString("");
//        }
//        SpannableString SS = new SpannableString(username + empty + status);
//        int end = username.length() + empty.length();
//        setLinkSpan(SS, usernameListener, 0, username.length());
//        setDefaultDarkgraySpan(SS, end, status.length() + end);
//        return SS;
//    }

//    public static Spannable dealContentForBold(String content) {
//        if (TextUtil.isNull(content)) {
//            return new SpannableString("");
//        }
//        return dealContentForBold(content, 0, content.length());
//    }
//
//    public static Spannable dealContentForBold(String content, int startPos, int endPos) {
//        if (TextUtil.isNull(content)) {
//            return new SpannableString("");
//        }
//        Spannable spannableString = new SpannableString(content);
//        setBoldLinkSpan(spannableString, startPos, endPos);
//        return spannableString;
//    }

//    public static Spannable dealContentForBoldAndBlack(String content, int startPos, int endPos) {
//        if (TextUtil.isNull(content)) {
//            return new SpannableString("");
//        }
//        SpannableString spannableString = new SpannableString(content);
//        setBoldLinkSpan(spannableString, startPos, endPos);
//        setBlackDarkgraySpan(spannableString, startPos, endPos);
//        return spannableString;
//    }
//
//    public static Spannable dealContentForBlack(String content, int startPos, int endPos) {
//        if (TextUtil.isNull(content)) {
//            return new SpannableString("");
//        }
//        SpannableString spannableString = new SpannableString(content);
//        setBlackDarkgraySpan(spannableString, startPos, endPos);
//        return spannableString;
//    }
//
//    public static Spannable BoldString(String username, OnClickListener usernameListener, String status) {
//        if (TextUtil.isNull(username)) {
//            return new SpannableString("");
//        }
//        SpannableString SS = new SpannableString(username + empty + status);
//        int end = username.length() + empty.length();
//        setBoldLinkSpan(SS, usernameListener, 0, username.length());
//        setBlackDarkgraySpan(SS, end, status.length() + end);
//        return SS;
//    }
//
//    public static Spannable BoldStringWithCount(NotificationBean bean, OnClickListener usernameListener, String status, int lineCount) {
//        if (TextUtil.isNull(bean) || TextUtil.isNull(bean.friend_screen_name)) {
//            return new SpannableString("");
//        }
//        SpannableString SS;
//        if (1 == lineCount) {
//            SS = new SpannableString(bean.friend_screen_name + empty + status + "\n" + DateUtil.parseDates(bean.created_at));
//        } else {
//            SS = new SpannableString(bean.friend_screen_name + empty + status + empty + empty + empty + DateUtil.parseDates(bean.created_at));
//        }
//        int end = bean.friend_screen_name.length() + empty.length();
//        setBoldLinkSpan(SS, usernameListener, 0, bean.friend_screen_name.length());
//        setBlackDarkgraySpan(SS, end, status.length() + end);
//        setNotifyTimeSpan(SS, (bean.friend_screen_name + empty + status + "\n").length(), SS.length());
//        return SS;
//    }
//
//    public static Spannable InviteString(String username, OnClickListener usernameListener, String status) {
//        if (TextUtil.isNull(username)) {
//            return new SpannableString("");
//        }
//        SpannableString SS = new SpannableString(username + empty + status);
//        int end = username.length() + empty.length();
//        setWhiteLinkSpan(SS, usernameListener, 0, username.length());
//        setDefaultDarkgraySpan(SS, end, status.length() + end);
//        return SS;
//    }

//    public static Spannable CommonDialogString(Context context, String username, OnClickListener usernameListener, String dialogType, String content) {
//        SpannableStringBuilder SSB = new SpannableStringBuilder(username + empty + dialogType + leftQuotes);
//        int end = username.length();
//        setLinkSpan(SSB, usernameListener, 0, end);
//        setDefaultDarkgraySpan(SSB, end, ((empty.length() + end) + dialogType.length()) + leftQuotes.length());
//        SSB.append(dealContent(context, content));
//        return SSB;
//    }

//    public static Spannable CommonDialogBoldBlackString(Context context, String username, OnClickListener usernameListener, String dialogType, String content) {
//        SpannableString SSB = new SpannableString(username + empty + dialogType + leftQuotes + content + empty);
//        int end = username.length();
//        setBoldLinkSpan(SSB, usernameListener, 0, end);
//        setBlackDarkgraySpan(SSB, end, ((empty.length() + end) + dialogType.length()) + leftQuotes.length());
//        return dealContentForColorGold(context, SSB);
//    }

//    public static Spannable CommonDialogBoldBlackStringWithCount(Context context, NotificationBean bean, OnClickListener usernameListener, String dialogType, String content, int lineCount) {
//        if (bean == null) {
//            return new SpannableString("");
//        }
//        SpannableStringBuilder SSB = new SpannableStringBuilder(bean.friend_screen_name + empty + dialogType + leftQuotes);
//        int end = bean.friend_screen_name.length();
//        setBoldLinkSpan(SSB, usernameListener, 0, end);
//        int end1 = ((empty.length() + end) + dialogType.length()) + leftQuotes.length();
//        setBlackDarkgraySpan(SSB, end, end1);
//        SSB.append(dealContentForColorGold(context, content));
//        int timeStart = SSB.length() + 1;
//        if (1 == lineCount) {
//            SSB.append("\n" + DateUtil.parseDates(bean.created_at));
//        } else {
//            SSB.append(empty + empty + empty + DateUtil.parseDates(bean.created_at));
//        }
//        setNotifyTimeSpan(SSB, timeStart, SSB.length());
//        SpannableString spannableString = new SpannableString(SSB.toString());
//        setBoldLinkSpan(spannableString, usernameListener, 0, end);
//        setBlackDarkgraySpan(SSB, end, end1);
//        spannableString = dealContentForColorGold(context, spannableString);
//        setNotifyTimeSpan(spannableString, timeStart, SSB.length());
//        return spannableString;
//    }
//
//    public static Spannable CommentDialogString(final Context context, final String screenName, String dialogType, String replySreenName, String content, boolean isReply) {
//        SpannableStringBuilder SSB;
//        if (!isReply || screenName.equals(replySreenName)) {
//            SSB = new SpannableStringBuilder(screenName + colon + empty);
////            setLinkSpan(SSB, 3.lambdaFactory$(context, screenName), 0, screenName.length());
//            setLinkSpan(SSB, new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context,"CommentDialogString onclick:"+screenName,Toast.LENGTH_LONG).show();
//                }
//            },0,screenName.length());
//            SSB.append(dealContent(context, content));
//            return SSB;
//        }
//        SSB = new SpannableStringBuilder(screenName + empty + dialogType + empty + replySreenName + colon + empty);
//        int end = screenName.length();
//        //setLinkSpan(SSB, 1.lambdaFactory$(context, screenName), 0, end);
//        setLinkSpan(SSB, new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context,"CommentDialogString onclick:"+screenName,Toast.LENGTH_LONG).show();
//            }
//        },0,end);
//        int dialogEnd = ((empty.length() + end) + dialogType.length()) + empty.length();
//        setDefaultDarkgraySpan(SSB, end, dialogEnd);
//        //setLinkSpan(SSB, 2.lambdaFactory$(context, replySreenName), dialogEnd, replySreenName.length() + dialogEnd);
//        setLinkSpan(SSB, new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context,"CommentDialogString onclick:"+screenName,Toast.LENGTH_LONG).show();
//            }
//        },dialogEnd,replySreenName.length()+dialogEnd);
//        SSB.append(dealContent(context, content));
//        return SSB;
//    }

    public static Spannable CommonDialogBoldBlackString(Context context, String screenName, String dialogType, String replySreenName, String content, boolean isReply) {
//        SpannableString spannableString1;
//        SpannableStringBuilder SSB;
//        int end;
//        String str;
//        Extractor extractor;
//        List<Entity> atEntityList;
//        List<Entity> hashtagEntityList;
//        if (!isReply || screenName.equals(replySreenName)) {
//            SSB = new SpannableStringBuilder(screenName + colon + empty);
//            end = screenName.length();
//            SSB.append(dealContentForColorGold(context, content));
//            setBoldLinkSpan(SSB, 10.lambdaFactory$(context, screenName), 0, end);
//            str = SSB.toString();
//            spannableString1 = new SpannableString(str);
//            extractor = new Extractor();
//            atEntityList = extractor.extractMentionsOrListsWithIndices(str);
//            if (atEntityList.size() > 0) {
//                for (Entity entity : atEntityList) {
//                    setHashtagLinkSpanForGold(spannableString1, 11.lambdaFactory$(context, entity), entity.getStart().intValue(), entity.getEnd().intValue());
//                }
//            }
//            hashtagEntityList = extractor.extractHashtagsWithIndices(str);
//            if (hashtagEntityList.size() > 0) {
//                for (Entity entity2 : hashtagEntityList) {
//                    setHashtagLinkSpanForGold(spannableString1, 12.lambdaFactory$(context, entity2), entity2.getStart().intValue(), entity2.getEnd().intValue());
//                }
//            }
//            setBoldLinkSpan(spannableString1, 13.lambdaFactory$(context, screenName), 0, end);
//        } else {
//            SSB = new SpannableStringBuilder(screenName + empty + dialogType + empty + replySreenName + colon + empty);
//            end = screenName.length();
//            int dialogEnd = ((empty.length() + end) + dialogType.length()) + empty.length();
//            int end1 = end;
//            int dialogEnd1 = dialogEnd;
//            setBlackDarkgraySpan(SSB, end1, dialogEnd1);
//            SSB.append(dealContentForColorGold(context, content));
//            setBoldLinkSpan(SSB, 4.lambdaFactory$(context, replySreenName), dialogEnd, replySreenName.length() + dialogEnd);
//            setBoldLinkSpan(SSB, 5.lambdaFactory$(context, screenName), 0, end);
//            str = SSB.toString();
//            spannableString1 = new SpannableString(str);
//            setBlackDarkgraySpan(spannableString1, end1, dialogEnd1);
//            extractor = new Extractor();
//            atEntityList = extractor.extractMentionsOrListsWithIndices(str);
//            if (atEntityList.size() > 0) {
//                for (Entity entity22 : atEntityList) {
//                    setHashtagLinkSpanForGold(spannableString1, 6.lambdaFactory$(context, entity22), entity22.getStart().intValue(), entity22.getEnd().intValue());
//                }
//            }
//            hashtagEntityList = extractor.extractHashtagsWithIndices(str);
//            if (hashtagEntityList.size() > 0) {
//                for (Entity entity222 : hashtagEntityList) {
//                    setHashtagLinkSpanForGold(spannableString1, 7.lambdaFactory$(context, entity222), entity222.getStart().intValue(), entity222.getEnd().intValue());
//                }
//            }
//            setBoldLinkSpan(spannableString1, 8.lambdaFactory$(context, replySreenName), dialogEnd, replySreenName.length() + dialogEnd);
//            setBoldLinkSpan(spannableString1, 9.lambdaFactory$(context, screenName), 0, end);
//        }
//        return spannableString1;
        return null;
    }

//    public static Spannable getAlbumName(final Context context, List<AlbumInfoBean> infoBeanList) {
//        if (context == null) {
//            return new SpannableString("");
//        }
//        String preName = String.format("%1$s ", new Object[]{context.getResources().getString(R.string.TEXT_ALBUM)});
//        SpannableStringBuilder SSB = new SpannableStringBuilder();
//        SpannableString preSS = new SpannableString(preName);
//        setDefaultDarkgraySpan(preSS, 0, preName.length());
//        SSB.append(preSS);
//        for (int i = 0; i < infoBeanList.size(); i++) {
//            SpannableString SS;
//            AlbumInfoBean bean = (AlbumInfoBean) infoBeanList.get(i);
//            if (i == infoBeanList.size() - 1) {
//                SS = new SpannableString(bean.name + empty);
//            } else {
//                SS = new SpannableString(bean.name + comma + empty);
//            }
////            setLinkSpan(SS, 14.lambdaFactory$(context, bean), 0, bean.name.length());
//            setLinkSpan(SS, new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context,"getAlbumName onclick:",Toast.LENGTH_LONG).show();
//                }
//            },0,bean.name.length());
//            setDefaultDarkgraySpan(SS, bean.name.length(), SS.length());
//            SSB.append(SS);
//        }
//        return SSB;
//    }

//    public static Spannable getMoreLikeSpannable(Activity activity, TimeLineCardEntity timeLineCardEntity, LikeEntity likeEntity, boolean isEnoughTen) {
//        if (isEnoughTen) {
//            return getMoreLike(NumberUtil.formatNumber((float) likeEntity.getLikeNum()) + App.getResource().getString(2131296843), 15.lambdaFactory$(activity, timeLineCardEntity));
//        }
//        return LikeUtil.getLikeContent(activity, likeEntity.getUserEntityList());
//    }

//    public static Spannable getMoreLike(String content, OnClickListener listener) {
//        SpannableStringBuilder SSB = new SpannableStringBuilder();
//        SpannableString SS = new SpannableString(content);
//        setBoldLinkSpan(SS, listener, 0, content.length());
//        SSB.append(SS);
//        SSB.append(new SpannableString(empty));
//        return SSB;
//    }

//    public static ArrayList<Spannable> getLikeArrayList(Context context, List<UserEntity> userEntities) {
//        ArrayList<Spannable> spannables = new ArrayList();
//        if (TextUtil.isValidate(userEntities)) {
//            int size = userEntities.size();
//            if (size <= 4) {
//                for (int i = 0; i < size; i++) {
//                    SpannableString spannableString;
//                    String screenName = ((UserEntity) userEntities.get(i)).getSreenName();
//                    int length = screenName.length();
//                    if (i != size - 1) {
//                        spannableString = new SpannableString(screenName + comma + empty);
//                        setBoldLinkSpan(spannableString, 16.lambdaFactory$(context, screenName), 0, length);
//                    } else {
//                        spannableString = new SpannableString(screenName + empty);
//                        setBoldLinkSpan(spannableString, 17.lambdaFactory$(context, screenName), 0, length);
//                    }
//                    spannables.add(spannableString);
//                }
//            }
//        }
//        return spannables;
//    }

//    public static ArrayList<StaticLayout> getLikeArrayListStaticLayout(Context context, List<UserEntity> userEntities) {
//        ArrayList<Spannable> likeArrayList = getLikeArrayList(context, userEntities);
//        ArrayList<StaticLayout> staticLayouts = new ArrayList();
//        int size = likeArrayList.size();
//        for (int i = 0; i < size; i++) {
//            staticLayouts.add(StaticLayoutUtil.getDefault2StaticLayout((Spannable) likeArrayList.get(i), ((UserEntity) userEntities.get(i)).getSreenName()));
//        }
//        return staticLayouts;
//    }

//    public static Spannable getLikeList(Context context, List<UserEntity> userEntities) {
//        if (!TextUtil.isValidate(userEntities)) {
//            return new SpannableStringBuilder();
//        }
//        int i;
//        int size = userEntities.size();
//        String usersAll = "";
//        for (i = 0; i < size; i++) {
//            if (i != size - 1) {
//                usersAll = usersAll + ((UserEntity) userEntities.get(i)).getSreenName() + comma + empty;
//            } else {
//                usersAll = usersAll + ((UserEntity) userEntities.get(i)).getSreenName();
//            }
//        }
//        SpannableString spannableString = new SpannableString(usersAll);
//        int start = 0;
//        for (i = 0; i < size; i++) {
//            String sreenName = ((UserEntity) userEntities.get(i)).getSreenName();
//            int length = sreenName.length();
//            setBoldLinkSpan(spannableString, 18.lambdaFactory$(context, sreenName), start, start + length);
//            start += length + 2;
//        }
//        return spannableString;
//    }

//    public static List<CommentEntity> getCommentEntityList(Context context, List<CommentBean> commentBeanList, int commentCount, boolean isNeedParserEntity, boolean isGiphy) {
//        if (!TextUtil.isValidate(commentBeanList)) {
//            return null;
//        }
//        int size = commentBeanList.size();
//        List<CommentEntity> arrayList = new ArrayList();
//        int j = 0;
//        while (j < size) {
//            if (isNeedParserEntity && commentCount > 5 && j == 1) {
//                arrayList.add(new CommentEntity());
//            }
//            arrayList.add(getCommentEntity(context, (CommentBean) commentBeanList.get(j), isGiphy));
//            j++;
//        }
//        return arrayList;
//    }

//    public static CommentEntity getCommentEntity(Context context, CommentBean commentBean, boolean isGiphy) {
//        if (commentBean == null) {
//            return null;
//        }
//        boolean isReply = TextUtil.isValidate(commentBean.comment_to_screen_name);
//        String text = commentBean.text;
//        if (text == null) {
//            text = "";
//        }
//        commentBean.text = text.replaceAll("\n", empty).replace("　", " ").trim();
//        Spannable comment = CommonDialogBoldBlackString(context, commentBean.user_screen_name, App.getResource().getString(2131296956), commentBean.comment_to_screen_name, text, isReply);
//        return new CommentEntity(commentBean.user_screen_name, commentBean.comment_to_screen_name, text, commentBean.id, commentBean.user_id, isReply, comment, StaticLayoutUtil.getSpace2StaticLayout(comment, App.getHardCodeWidth(isGiphy)));
//    }

//    public static Spannable getLikesYourPicture(String username, OnClickListener usernameListener, int shareType) {
//        String content = "";
//        if (shareType == 0) {
//            content = String.format(mResource.getString(2131296916), new Object[]{mResource.getString(2131296705)});
//        } else if (shareType == 1) {
//            content = String.format(mResource.getString(2131296916), new Object[]{mResource.getString(2131296707)});
//        } else if (shareType == 2) {
//            content = String.format(mResource.getString(2131296916), new Object[]{mResource.getString(2131296711)});
//        }
//        return BoldString(username, usernameListener, content);
//    }

//    public static Spannable getLikesYourPictureWithTime(NotificationBean bean, OnClickListener usernameListener, int lineCount, int shareType) {
//        String content = "";
//        if (shareType == 0) {
//            content = String.format(mResource.getString(2131296916), new Object[]{mResource.getString(2131296705)});
//        } else if (shareType == 1) {
//            content = String.format(mResource.getString(2131296916), new Object[]{mResource.getString(2131296707)});
//        } else if (shareType == 2) {
//            content = String.format(mResource.getString(2131296916), new Object[]{mResource.getString(2131296711)});
//        }
//        return BoldStringWithCount(bean, usernameListener, content, lineCount);
//    }

//    public static Spannable getInvitedYou(String username, String mStatus, String mAlbumName, OnClickListener usernameListener, OnClickListener albumListener) {
//        if (TextUtil.isNull(username)) {
//            return new SpannableString("");
//        }
//        String albumName;
//        if (mAlbumName == null) {
//            albumName = "null";
//        } else {
//            albumName = mAlbumName;
//        }
//        String status = mStatus;
//        String source = username + empty + String.format(status, new Object[]{albumName}) + empty;
//        SpannableString SS = new SpannableString(source);
//        int end = username.length() + empty.length();
//        setBoldLinkSpan(SS, usernameListener, 0, username.length());
//        setBlackDarkgraySpan(SS, end, status.length() + end);
//        end = source.indexOf(albumName);
//        setBoldLinkSpan(SS, albumListener, end, albumName.length() + end);
//        return SS;
//    }
//
//    public static Spannable getInvitedYouWithTime(NotificationBean bean, String mStatus, String mAlbumName, OnClickListener usernameListener, OnClickListener albumListener, int lineCount) {
//        if (TextUtil.isNull(bean) || TextUtil.isNull(bean.friend_screen_name)) {
//            return new SpannableString("");
//        }
//        String albumName;
//        if (mAlbumName == null) {
//            albumName = "null";
//        } else {
//            albumName = mAlbumName;
//        }
//        String status = mStatus;
//        String source = bean.friend_screen_name + empty + String.format(status, new Object[]{albumName}) + empty;
//        int timeStart = source.length() + 1;
//        if (lineCount == 1) {
//            source = source + "\n" + DateUtil.parseDates(bean.created_at);
//        } else {
//            source = source + empty + empty + empty + DateUtil.parseDates(bean.created_at);
//        }
//        SpannableString SS = new SpannableString(source);
//        int end = bean.friend_screen_name.length() + empty.length();
//        setBoldLinkSpan(SS, usernameListener, 0, bean.friend_screen_name.length());
//        setBlackDarkgraySpan(SS, end, status.length() + end);
//        end = source.indexOf(albumName);
//        setBoldLinkSpan(SS, albumListener, end, albumName.length() + end);
//        setNotifyTimeSpan(SS, timeStart, SS.length());
//        return SS;
//    }

//    public static Spannable getCommentYourPicture(Context context, String username, String comment_to_screen_name, String content) {
//        String dialogType;
//        SpannableStringBuilder SSB;
//        int end;
//        int end1;
//        int start2;
//        int end2;
//        SpannableString spannableString;
//        if (TextUtil.isValidate(comment_to_screen_name)) {
//            int start1;
//            if (comment_to_screen_name.equals(GlobalHelper.getUserScreenName())) {
//                dialogType = context.getString(2131296920);
//                SSB = new SpannableStringBuilder(username + empty + dialogType + empty);
//                end = username.length();
//                start1 = 0;
//                end1 = end;
//                setBoldLinkSpan(SSB, 19.lambdaFactory$(context, username), start1, end1);
//                start2 = end;
//                end2 = end + ((empty.length() + dialogType.length()) + empty.length());
//                setBlackDarkgraySpan(SSB, start2, end2);
//                SSB.append(dealContentForColorGold(context, content));
//                spannableString = new SpannableString(SSB.toString());
//                setBoldLinkSpan(spannableString, 20.lambdaFactory$(context, username), start1, end1);
//                setBlackDarkgraySpan(spannableString, start2, end2);
//                return dealContentForColorGold(context, spannableString);
//            }
//            dialogType = context.getString(2131296956);
//            SSB = new SpannableStringBuilder(username + empty + dialogType + empty + comment_to_screen_name + colon + empty);
//            end = username.length();
//            start1 = 0;
//            end1 = end;
//            setBoldLinkSpan(SSB, 21.lambdaFactory$(context, username), start1, end1);
//            int start = end;
//            end += (empty.length() + dialogType.length()) + empty.length();
//            int start3 = start;
//            int end3 = end;
//            setBlackDarkgraySpan(SSB, start3, end3);
//            start2 = end;
//            end2 = end + comment_to_screen_name.length();
//            setBoldLinkSpan(SSB, 22.lambdaFactory$(context, comment_to_screen_name), start2, end2);
//            SSB.append(dealContentForColorGold(context, content));
//            spannableString = new SpannableString(SSB.toString());
//            setBoldLinkSpan(spannableString, 23.lambdaFactory$(context, username), start1, end1);
//            setBlackDarkgraySpan(spannableString, start3, end3);
//            setBoldLinkSpan(spannableString, 24.lambdaFactory$(context, comment_to_screen_name), start2, end2);
//            return dealContentForColorGold(context, spannableString);
//        }
//        dialogType = context.getString(2131296910);
//        SSB = new SpannableStringBuilder(username + empty + dialogType + empty);
//        end = username.length();
//        end1 = end;
//        setBoldLinkSpan(SSB, 25.lambdaFactory$(context, username), 0, end1);
//        start2 = end;
//        end2 = ((empty.length() + end) + dialogType.length()) + empty.length();
//        setBlackDarkgraySpan(SSB, start2, end2);
//        SSB.append(dealContentForColorGold(context, content));
//        spannableString = new SpannableString(SSB.toString());
//        setBoldLinkSpan(spannableString, 26.lambdaFactory$(context, username), 0, end1);
//        setBlackDarkgraySpan(spannableString, start2, end2);
//        return dealContentForColorGold(context, spannableString);
//    }

//    public static Spannable getCommentYourPictureWithCount(Context context, NotificationBean bean, String comment_to_screen_name, String content, int lineCount) {
//        if (bean == null) {
//            return new SpannableString("");
//        }
//        String dialogType;
//        SpannableStringBuilder SSB;
//        int end;
//        int end1;
//        int start2;
//        int end2;
//        int timeStart;
//        SpannableString spannableString;
//        if (TextUtil.isValidate(comment_to_screen_name)) {
//            int start1;
//            if (comment_to_screen_name.equals(GlobalHelper.getUserScreenName())) {
//                dialogType = context.getString(2131296920);
//                SSB = new SpannableStringBuilder(bean.friend_screen_name + empty + dialogType + empty);
//                end = bean.friend_screen_name.length();
//                start1 = 0;
//                end1 = end;
//                setBoldLinkSpan(SSB, 27.lambdaFactory$(context, bean), start1, end1);
//                start2 = end;
//                end2 = end + ((empty.length() + dialogType.length()) + empty.length());
//                setBlackDarkgraySpan(SSB, start2, end2);
//                SSB.append(dealContentForColorGold(context, content));
//                timeStart = SSB.length();
//                if (lineCount == 1) {
//                    SSB.append("\n" + DateUtil.parseDates(bean.created_at));
//                } else {
//                    SSB.append(empty + empty + empty + DateUtil.parseDates(bean.created_at));
//                }
//                setNotifyTimeSpan(SSB, timeStart, SSB.length());
//                spannableString = new SpannableString(SSB.toString());
//                setBoldLinkSpan(spannableString, 28.lambdaFactory$(context, bean), start1, end1);
//                setBlackDarkgraySpan(spannableString, start2, end2);
//                spannableString = dealContentForColorGold(context, spannableString);
//                setNotifyTimeSpan(spannableString, timeStart, SSB.length());
//                return spannableString;
//            }
//            dialogType = context.getString(2131296956);
//            SSB = new SpannableStringBuilder(bean.friend_screen_name + empty + dialogType + empty + comment_to_screen_name + colon + empty);
//            end = bean.friend_screen_name.length();
//            start1 = 0;
//            end1 = end;
//            setBoldLinkSpan(SSB, 29.lambdaFactory$(context, bean), start1, end1);
//            int start = end;
//            end += (empty.length() + dialogType.length()) + empty.length();
//            start2 = start;
//            end2 = end;
//            setBlackDarkgraySpan(SSB, start2, end2);
//            int start3 = end;
//            int end3 = end + comment_to_screen_name.length();
//            setBoldLinkSpan(SSB, 30.lambdaFactory$(context, comment_to_screen_name), start3, end3);
//            SSB.append(dealContentForColorGold(context, content));
//            timeStart = SSB.length();
//            if (lineCount == 1) {
//                SSB.append("\n" + DateUtil.parseDates(bean.created_at));
//            } else {
//                SSB.append(empty + empty + empty + DateUtil.parseDates(bean.created_at));
//            }
//            setNotifyTimeSpan(SSB, timeStart, SSB.length());
//            spannableString = new SpannableString(SSB.toString());
//            setBoldLinkSpan(spannableString, 31.lambdaFactory$(context, bean), start1, end1);
//            setBlackDarkgraySpan(spannableString, start2, end2);
//            setBoldLinkSpan(spannableString, 32.lambdaFactory$(context, comment_to_screen_name), start3, end3);
//            spannableString = dealContentForColorGold(context, spannableString);
//            setNotifyTimeSpan(spannableString, timeStart, SSB.length());
//            return spannableString;
//        }
//        dialogType = context.getString(2131296910);
//        SSB = new SpannableStringBuilder(bean.friend_screen_name + empty + dialogType + empty);
//        end = bean.friend_screen_name.length();
//        end1 = end;
//        setBoldLinkSpan(SSB, 33.lambdaFactory$(context, bean), 0, end1);
//        start2 = end;
//        end2 = ((empty.length() + end) + dialogType.length()) + empty.length();
//        setBlackDarkgraySpan(SSB, start2, end2);
//        SSB.append(dealContentForColorGold(context, content));
//        timeStart = SSB.length();
//        if (lineCount == 1) {
//            SSB.append("\n" + DateUtil.parseDates(bean.created_at));
//        } else {
//            SSB.append(empty + DateUtil.parseDates(bean.created_at));
//        }
//        setNotifyTimeSpan(SSB, timeStart, SSB.length());
//        spannableString = new SpannableString(SSB.toString());
//        setBoldLinkSpan(spannableString, 34.lambdaFactory$(context, bean), 0, end1);
//        setBlackDarkgraySpan(spannableString, start2, end2);
//        spannableString = dealContentForColorGold(context, spannableString);
//        setNotifyTimeSpan(spannableString, timeStart, SSB.length());
//        return spannableString;
//    }

//    public static Spannable dealContentForGold(Context context, String content) {
//        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(content);
//        Extractor extractor = new Extractor();
//        List<Entity> atEntityList = extractor.extractMentionsOrListsWithIndices(content);
//        if (atEntityList.size() > 0) {
//            for (Entity entity : atEntityList) {
//                setHashtagLinkSpanForGold(stringBuilder, 35.lambdaFactory$(context, entity), entity.getStart().intValue(), entity.getEnd().intValue());
//            }
//        }
//        List<Entity> hashtagEntityList = extractor.extractHashtagsWithIndices(content);
//        if (hashtagEntityList.size() > 0) {
//            for (Entity entity2 : hashtagEntityList) {
//                setHashtagLinkSpan(stringBuilder, 36.lambdaFactory$(context, entity2), entity2.getStart().intValue(), entity2.getEnd().intValue());
//            }
//        }
//        stringBuilder.append(new SpannableString(empty));
//        return stringBuilder;
//    }
//
//    public static Spannable dealContentForGoldNoAt(Context context, String content) {
//        Entity entity;
//        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(content);
//        Extractor extractor = new Extractor();
//        List<Entity> atEntityList = extractor.extractMentionsOrListsWithIndices(content);
//        if (atEntityList.size() > 0) {
//            for (Entity entity2 : atEntityList) {
//                setHashtagLinkSpanForGold(stringBuilder, 37.lambdaFactory$(context, entity2), entity2.getStart().intValue(), entity2.getEnd().intValue());
//            }
//        }
//        List<Entity> hashtagEntityList = extractor.extractHashtagsWithIndices(content);
//        if (hashtagEntityList.size() > 0) {
//            for (Entity entity22 : hashtagEntityList) {
//                setHashtagLinkSpanForGold(stringBuilder, 38.lambdaFactory$(context, entity22), entity22.getStart().intValue(), entity22.getEnd().intValue());
//            }
//        }
//        stringBuilder.append(new SpannableString(empty));
//        if (atEntityList.size() > 0) {
//            for (int index = atEntityList.size() - 1; index >= 0; index--) {
//                entity22 = (Entity) atEntityList.get(index);
//                stringBuilder.delete(entity22.getStart().intValue(), entity22.getStart().intValue() + 1);
//            }
//        }
//        return stringBuilder;
//    }

//    public static SpannableString dealContentForColorGold(Context context, SpannableString content) {
//        String string = content.toString();
//        Extractor extractor = new Extractor();
//        List<Entity> atEntityList = extractor.extractMentionsOrListsWithIndices(string);
//        if (atEntityList.size() > 0) {
//            for (Entity entity : atEntityList) {
//                setHashtagLinkSpanForGold(content, 39.lambdaFactory$(context, entity), entity.getStart().intValue(), entity.getEnd().intValue());
//            }
//        }
//        List<Entity> hashtagEntityList = extractor.extractHashtagsWithIndices(string);
//        if (hashtagEntityList.size() > 0) {
//            for (Entity entity2 : hashtagEntityList) {
//                setHashtagLinkSpanForGold(content, 40.lambdaFactory$(context, entity2), entity2.getStart().intValue(), entity2.getEnd().intValue());
//            }
//        }
//        return content;
//    }

//    public static Spannable dealContentForColorGold(Context context, String content) {
//        SpannableString stringBuilder = new SpannableString(content + empty);
//        Extractor extractor = new Extractor();
//        List<Entity> atEntityList = extractor.extractMentionsOrListsWithIndices(content);
//        if (atEntityList.size() > 0) {
//            for (Entity entity : atEntityList) {
//                setHashtagLinkSpanForGold(stringBuilder, 41.lambdaFactory$(context, entity), entity.getStart().intValue(), entity.getEnd().intValue());
//            }
//        }
//        List<Entity> hashtagEntityList = extractor.extractHashtagsWithIndices(content);
//        if (hashtagEntityList.size() > 0) {
//            for (Entity entity2 : hashtagEntityList) {
//                setHashtagLinkSpanForGold(stringBuilder, 42.lambdaFactory$(context, entity2), entity2.getStart().intValue(), entity2.getEnd().intValue());
//            }
//        }
//        return stringBuilder;
//    }

//    public static Spannable dealContentForYoutubeLink(Activity activity, String content, int index, YoutubeDrawableSpan imageSpan) {
//        SpannableString spannableString = new SpannableString(content);
//        spannableString.setSpan(imageSpan, index, content.length(), 33);
//        setBoldBlackSpan(spannableString, index, content.length());
//        setLinkSpan(spannableString, 43.lambdaFactory$(content, index, activity), index, content.length());
//        return spannableString;
//    }
//
//    private static /* synthetic */ void lambda$dealContentForYoutubeLink$42(String content, int index, Activity activity, View v) {
//        JZVideoPlayer jzvd = JZVideoPlayerManager.getCurrentJzvd();
//        if (jzvd != null && jzvd.currentState == 3) {
//            LogUtil.d("slim", "pause jz for youtube web");
//            jzvd.startButton.performClick();
//        }
//        IntentUtil.toYoutubeWebViewActivity(activity, content.substring(index).trim());
//    }

//    public static Spannable dealContentForDiscoverTitle(String content, DetailDrawableSpan imageSpan) {
//        SpannableString spannableString = new SpannableString(content + " ");
//        spannableString.setSpan(imageSpan, content.length(), content.length() + 1, 33);
//        spannableString.setSpan(new StyleSpan(1), 0, spannableString.length(), flags);
//        return spannableString;
//    }
//
//    public static Spannable dealContentForColorGoldAddGiphyKeyword(final Context context, String content, String keyword, boolean isNew, CustomDynamicDrawableSpan customDynamicDrawableSpan) {
//        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(content);
//        if (TextUtil.isValidate(content)) {
//            stringBuilder.append(empty);
//        }
//        String title_gif_library = context.getResources().getString(2131297064);
//        if (TextUtil.isValidate(keyword)) {
//            stringBuilder.append(IMG_REPLACE_STR).append(empty).append(title_gif_library);
//        }
//        SpannableString spannableString = new SpannableString(stringBuilder.toString() + empty);
//        Extractor extractor = new Extractor();
//        List<Entity> atEntityList = extractor.extractMentionsOrListsWithIndices(content);
//        if (atEntityList.size() > 0) {
//            for (Entity entity : atEntityList) {
//                setHashtagLinkSpanForGold(spannableString, 44.lambdaFactory$(context, entity), entity.getStart().intValue(), entity.getEnd().intValue());
//            }
//        }
//        List<Entity> hashtagEntityList = extractor.extractHashtagsWithIndices(content);
//        if (hashtagEntityList.size() > 0) {
//            for (Entity entity2 : hashtagEntityList) {
//                setHashtagLinkSpanForGold(spannableString, 45.lambdaFactory$(context, entity2), entity2.getStart().intValue(), entity2.getEnd().intValue());
//            }
//        }
//        if (TextUtil.isValidate(keyword)) {
//            int end = spannableString.length() - 1;
//            int start = end - title_gif_library.length();
//            spannableString.setSpan(customDynamicDrawableSpan, start - 2, start - 1, 33);
//            final String str = keyword;
//            final boolean z = isNew;
//            setHashtagLinkSpanForGold(spannableString, new OnClickListener() {
//                public void onClick(View v) {
//                    IntentUtil.toGiphyResultActivity(context, str, z);
//                }
//            }, start - 2, end);
//        }
//        return spannableString;
//    }

//    public static Spannable dealContent(Context context, String content) {
//        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(content);
//        Extractor extractor = new Extractor();
//        List<Entity> atEntityList = extractor.extractMentionsOrListsWithIndices(content);
//        if (atEntityList.size() > 0) {
//            for (Entity entity : atEntityList) {
//                setHashtagLinkSpan(stringBuilder, 46.lambdaFactory$(context, entity), entity.getStart().intValue(), entity.getEnd().intValue());
//            }
//        }
//        List<Entity> hashtagEntityList = extractor.extractHashtagsWithIndices(content);
//        if (hashtagEntityList.size() > 0) {
//            for (Entity entity2 : hashtagEntityList) {
//                setHashtagLinkSpan(stringBuilder, 47.lambdaFactory$(context, entity2), entity2.getStart().intValue(), entity2.getEnd().intValue());
//            }
//        }
//        stringBuilder.append(new SpannableString(empty));
//        return stringBuilder;
//    }
//
//    public static Spannable getPictureMentionYou(String username, OnClickListener usernameListener, int shareType) {
//        String content = "";
//        if (shareType == 0) {
//            content = String.format(mResource.getString(2131296918), new Object[]{mResource.getString(2131296705)});
//        } else if (shareType == 1) {
//            content = String.format(mResource.getString(2131296918), new Object[]{mResource.getString(2131296707)});
//        } else if (shareType == 2) {
//            content = String.format(mResource.getString(2131296918), new Object[]{mResource.getString(2131296711)});
//        }
//        return BoldString(username, usernameListener, content);
//    }

//    public static Spannable getPictureMentionYouWithTime(NotificationBean bean, OnClickListener usernameListener, int lineCount, int shareType) {
//        String content = "";
//        if (shareType == 0) {
//            content = String.format(mResource.getString(2131296918), new Object[]{mResource.getString(2131296705)});
//        } else if (shareType == 1) {
//            content = String.format(mResource.getString(2131296918), new Object[]{mResource.getString(2131296707)});
//        } else if (shareType == 2) {
//            content = String.format(mResource.getString(2131296918), new Object[]{mResource.getString(2131296711)});
//        }
//        return BoldStringWithCount(bean, usernameListener, content, lineCount);
//    }

//    public static Spannable getCommentMentionYou(Context context, String username, OnClickListener usernameListener, String content) {
//        return CommonDialogBoldBlackString(context, username, usernameListener, mResource.getString(2131296917), content);
//    }
//
//    public static Spannable getCommentMentionYouWithTime(Context context, NotificationBean bean, OnClickListener usernameListener, String content, int lineCount) {
//        return CommonDialogBoldBlackStringWithCount(context, bean, usernameListener, mResource.getString(2131296917), content, lineCount);
//    }

////    public static Spannable getYourFriendRegister(String socialType, String platform_screen_name, String username, String addressName, OnClickListener usernameListener) {
////        String friend;
////        String your = LocaleUtil.isChineseCode() ? mResource.getString(2131296914) : mResource.getString(2131296914) + " ";
////        if (LocaleUtil.isChineseCode()) {
////            friend = mResource.getString(2131296912);
////        } else {
////            friend = " " + mResource.getString(2131296912);
////        }
////        String createdAccount = mResource.getString(2131296911);
////        if (TextUtil.isNull(socialType)) {
////            socialType = "";
////        }
////        if (TextUtil.isNull(username)) {
////            username = "";
////        }
////        if (TextUtil.isNull(platform_screen_name)) {
////            platform_screen_name = "";
////        } else {
////            platform_screen_name = "(" + platform_screen_name + ")";
////        }
////        SpannableString SS = new SpannableString(your + socialType + friend + " " + username + " " + platform_screen_name + " " + createdAccount);
////        int end = (your.length() + socialType.length()) + friend.length();
////        setBoldLinkSpan(SS, usernameListener, end + 1, (username.length() + end) + 1);
////        return SS;
////    }
////
////    public static Spannable getYourFriendRegisterWithTime(String socialType, String platform_screen_name, String username, String addressName, OnClickListener usernameListener, int lineCount, NotificationBean bean) {
////        if (bean == null) {
////            return new SpannableString("");
////        }
////        String friend;
////        SpannableString SS;
////        String your = LocaleUtil.isChineseCode() ? mResource.getString(2131296914) : mResource.getString(2131296914) + " ";
////        if (LocaleUtil.isChineseCode()) {
////            friend = mResource.getString(2131296912);
////        } else {
////            friend = " " + mResource.getString(2131296912);
////        }
////        String createdAccount = mResource.getString(2131296911);
////        if (TextUtil.isNull(socialType)) {
////            socialType = "";
////        }
////        if (TextUtil.isNull(username)) {
////            username = "";
////        }
////        if (TextUtil.isNull(platform_screen_name)) {
////            platform_screen_name = "";
////        } else {
////            platform_screen_name = "(" + platform_screen_name + ")";
////        }
////        if (1 == lineCount) {
////            SS = new SpannableString(your + socialType + friend + " " + username + " " + createdAccount + "\n" + DateUtil.parseDates(bean.created_at));
////        } else {
////            SS = new SpannableString(your + socialType + friend + " " + username + " " + platform_screen_name + " " + createdAccount + empty + empty + empty + DateUtil.parseDates(bean.created_at));
////        }
////        int end = (your.length() + socialType.length()) + friend.length();
////        setBoldLinkSpan(SS, usernameListener, end + 1, (username.length() + end) + 1);
////        setNotifyTimeSpan(SS, (your + socialType + friend + " " + username + " " + platform_screen_name + " " + createdAccount + "\n").length(), SS.length());
////        return SS;
////    }
//
//    public static Spannable getStartFollowingYou(String username, OnClickListener usernameListener) {
//        return BoldString(username, usernameListener, mResource.getString(2131296913));
//    }
//
//    public static Spannable getStartFollowingYouWithTime(NotificationBean bean, OnClickListener usernameListener, int lineCount) {
//        return BoldStringWithCount(bean, usernameListener, mResource.getString(2131296913), lineCount);
//    }
//
//    public static Spannable inviteYouTakePartIn(String username, OnClickListener usernameListener) {
//        return InviteString(username, usernameListener, mResource.getString(2131296999));
//    }

//    public static Spannable getLightGrayTime(String time) {
//        SpannableString SS = new SpannableString(time);
//        setLightgraySpan(SS, 0, time.length());
//        return SS;
//    }

//    public static void setBoldLinkSpan(Spannable SS, int start, int end) {
//        if (start > end) {
//            start = end;
//        }
//        SS.setSpan(new StyleSpan(1), start, end, flags);
//    }

//    public static void setBoldBlackSpan(Spannable ss, int start, int end) {
//        if (start > end) {
//            start = end;
//        }
//        ss.setSpan(new StyleSpan(1), start, end, flags);
//        ss.setSpan(new ForegroundColorSpan(App.getResource().getColor(2131755050)), start, end, flags);
//    }

//    public static void setDefaultDarkgraySpan(SpannableString SS, int start, int end) {
//        if (start > end) {
//            start = end;
//        }
//        SS.setSpan(new ForegroundColorSpan(App.getResource().getColor(2131755040)), start, end, flags);
//        SS.setSpan(new BackgroundColorSpan(App.getResource().getColor(17170445)), start, end, flags);
//    }

//    public static void setDefaultDarkgraySpan(SpannableStringBuilder SS, int start, int end) {
//        if (start > end) {
//            start = end;
//        }
//        SS.setSpan(new ForegroundColorSpan(App.getResource().getColor(2131755040)), start, end, flags);
//        SS.setSpan(new BackgroundColorSpan(App.getResource().getColor(17170445)), start, end, flags);
//    }

//    public static void set92DarkgraySpan(SpannableStringBuilder SS, int start, int end) {
//        if (start > end) {
//            start = end;
//        }
//        SS.setSpan(new ForegroundColorSpan(App.getResource().getColor(2131755042)), start, end, flags);
//        SS.setSpan(new BackgroundColorSpan(App.getResource().getColor(17170445)), start, end, flags);
//    }
//
//    public static void set92DarkgraySpan(SpannableString SS, int start, int end) {
//        if (start > end) {
//            start = end;
//        }
//        SS.setSpan(new ForegroundColorSpan(App.getResource().getColor(2131755042)), start, end, flags);
//        SS.setSpan(new BackgroundColorSpan(App.getResource().getColor(17170445)), start, end, flags);
//    }

//    public static void setBlackDarkgraySpan(SpannableString SS, int start, int end) {
//        if (start > end) {
//            start = end;
//        }
//        SS.setSpan(new ForegroundColorSpan(App.getResource().getColor(2131755050)), start, end, flags);
//        SS.setSpan(new BackgroundColorSpan(App.getResource().getColor(17170445)), start, end, flags);
//    }
//
//    public static void setNotifyTimeSpan(SpannableString SS, int start, int end) {
//        if (start > end) {
//            start = end;
//        }
//        SS.setSpan(new ForegroundColorSpan(App.getResource().getColor(2131755042)), start, end, flags);
//        SS.setSpan(new BackgroundColorSpan(App.getResource().getColor(17170445)), start, end, flags);
//        SS.setSpan(new AbsoluteSizeSpan(12, true), start, end, flags);
//    }
//
//    public static void setNotifyTimeSpan(SpannableStringBuilder SS, int start, int end) {
//        if (start > end) {
//            start = end;
//        }
//        SS.setSpan(new ForegroundColorSpan(App.getResource().getColor(2131755042)), start, end, flags);
//        SS.setSpan(new BackgroundColorSpan(App.getResource().getColor(17170445)), start, end, flags);
//        SS.setSpan(new AbsoluteSizeSpan(12, true), start, end, flags);
//    }
//
//    public static void setBlackDarkgraySpan(SpannableStringBuilder SS, int start, int end) {
//        if (start > end) {
//            start = end;
//        }
//        SS.setSpan(new ForegroundColorSpan(App.getResource().getColor(2131755050)), start, end, flags);
//        SS.setSpan(new BackgroundColorSpan(App.getResource().getColor(17170445)), start, end, flags);
//    }

//    public static void setLightgraySpan(SpannableString SS, int start, int end) {
//        if (start > end) {
//            start = end;
//        }
//        SS.setSpan(new ForegroundColorSpan(App.getResource().getColor(2131755074)), start, end, flags);
//        SS.setSpan(new BackgroundColorSpan(App.getResource().getColor(17170445)), start, end, flags);
//    }

//    public static void setLinkSpan(SpannableString SS, OnClickListener userListener, int start, int end) {
//        if (start > end) {
//            start = end;
//        }
//        SS.setSpan(new ForegroundColorSpan(App.getResource().getColor(2131755050)), start, end, flags);
//        SS.setSpan(new LinkSpan(userListener), start, end, flags);
//        SS.setSpan(new BackgroundColorSpan(App.getResource().getColor(17170445)), start, end, flags);
//    }

//    public static void setBoldLinkSpan(SpannableString SS, OnClickListener userListener, int start, int end) {
//        if (start > end) {
//            start = end;
//        }
//        SS.setSpan(new ForegroundColorSpan(App.getResource().getColor(2131755050)), start, end, flags);
//        SS.setSpan(new LinkSpan(userListener), start, end, flags);
//        SS.setSpan(new BackgroundColorSpan(App.getResource().getColor(17170445)), start, end, flags);
//        SS.setSpan(new StyleSpan(1), start, end, flags);
//    }
//
//    public static void setLinkSpan(SpannableString SS, OnClickListener userListener, int start, int end, int colorId) {
//        SS.setSpan(new ForegroundColorSpan(App.getResource().getColor(2131755050)), start, end, flags);
//        SS.setSpan(new LinkSpan(userListener, App.getResource().getColor(colorId)), start, end, flags);
//        SS.setSpan(new BackgroundColorSpan(App.getResource().getColor(17170445)), start, end, flags);
//    }
//
//    public static void setBoldLinkSpan(SpannableString SS, OnClickListener userListener, int start, int end, int colorId) {
//        if (start > end) {
//            start = end;
//        }
//        SS.setSpan(new ForegroundColorSpan(App.getResource().getColor(2131755050)), start, end, flags);
//        SS.setSpan(new LinkSpan(userListener, App.getResource().getColor(colorId)), start, end, flags);
//        SS.setSpan(new BackgroundColorSpan(App.getResource().getColor(17170445)), start, end, flags);
//        SS.setSpan(new StyleSpan(1), start, end, flags);
//    }
//
//    public static void setWhiteLinkSpan(SpannableString SS, OnClickListener userListener, int start, int end) {
//        if (start > end) {
//            start = end;
//        }
//        SS.setSpan(new ForegroundColorSpan(App.getResource().getColor(R.color.colorWhite)), start, end, flags);
//        SS.setSpan(new LinkSpan(userListener, App.getResource().getColor(R.color.colorWhite)), start, end, flags);
//        SS.setSpan(new BackgroundColorSpan(App.getResource().getColor(R.color.colorAccent)), start, end, flags);
//    }
//
//    public static void setLinkSpan(SpannableStringBuilder SS, OnClickListener userListener, int start, int end) {
//        if (start > end) {
//            start = end;
//        }
//        SS.setSpan(new ForegroundColorSpan(App.getResource().getColor(R.color.colorBlack)), start, end, flags);
//        SS.setSpan(new LinkSpan(userListener), start, end, flags);
//        SS.setSpan(new BackgroundColorSpan(App.getResource().getColor(R.color.colorAccent)), start, end, flags);
//    }
//
//    public static void setBoldLinkSpan(SpannableStringBuilder SS, OnClickListener userListener, int start, int end) {
//        if (start > end) {
//            start = end;
//        }
//        SS.setSpan(new ForegroundColorSpan(App.getResource().getColor(R.color.colorBlack)), start, end, flags);
//        SS.setSpan(new BackgroundColorSpan(App.getResource().getColor(R.color.colorAccent)), start, end, flags);
//        SS.setSpan(new LinkSpan(userListener), start, end, flags);
//        SS.setSpan(new StyleSpan(1), start, end, flags);
//    }
//
//    public static void setHashtagLinkSpan(SpannableStringBuilder SS, OnClickListener userListener, int start, int end) {
//        if (start > end) {
//            start = end;
//        }
//        SS.setSpan(new ForegroundColorSpan(App.getResource().getColor(R.color.color66)), start, end, flags);
//        SS.setSpan(new LinkSpan(userListener, App.getResource().getColor(R.color.color66)), start, end, flags);
//        SS.setSpan(new BackgroundColorSpan(App.getResource().getColor(R.color.colorAccent)), start, end, flags);
//    }

//    public static void setHashtagLinkSpanForGold(SpannableStringBuilder SS, OnClickListener userListener, int start, int end) {
//        if (start > end) {
//            start = end;
//        }
//        SS.setSpan(new ForegroundColorSpan(App.getResource().getColor(R.color.colorGold)), start, end, flags);
//        SS.setSpan(new LinkSpan(userListener, App.getResource().getColor(R.color.colorGold)), start, end, flags);
//        SS.setSpan(new BackgroundColorSpan(App.getResource().getColor(R.color.colorAccent)), start, end, flags);
//    }

//    public static void setHashtagLinkSpanForGold(SpannableString SS, OnClickListener userListener, int start, int end) {
//        if (start > end) {
//            start = end;
//        }
//        SS.setSpan(new ForegroundColorSpan(App.getResource().getColor(R.color.colorGold)), start, end, flags);
//        SS.setSpan(new LinkSpan(userListener, App.getResource().getColor(R.color.colorGold)), start, end, flags);
//        SS.setSpan(new BackgroundColorSpan(App.getResource().getColor(R.color.colorAccent)), start, end, flags);
//    }

    public static void setTextDpSizeSpan(SpannableStringBuilder SS, int textDpSize, int start, int end) {
        if (start > end) {
            start = end;
        }
        SS.setSpan(new AbsoluteSizeSpan(textDpSize, true), start, end, flags);
    }

    public static void setTextDpSizeSpan(SpannableString SS, int textDpSize, int start, int end) {
        if (start > end) {
            start = end;
        }
        SS.setSpan(new AbsoluteSizeSpan(textDpSize, true), start, end, flags);
    }

//    public static Spannable getPopupCommentNew(Context context, String content) {
//        if (TextUtil.isValidate(content)) {
//            return dealContent(context, content);
//        }
//        return new SpannableStringBuilder();
//    }

//    public static Spannable getGoldColorPopupCommentNew(Context context, String content) {
//        if (TextUtil.isValidate(content)) {
//            return dealContentForColorGold(context, content);
//        }
//        return new SpannableStringBuilder();
////    }
//
//    public static Spannable getIMChatListContent(Context context, String content, String type) {
//        if (TextUtil.isValidate(content)) {
//            return dealIMChatListContent(context, content, type);
//        }
//        return new SpannableString(content);
//    }

//    private static Spannable dealIMChatListContent(final Context context, String content, String type) {
//        int i = -1;
//        switch (type.hashCode()) {
//            case 112202875:
//                if (type.equals(AnalyticsEvents.PARAMETER_SHARE_DIALOG_CONTENT_VIDEO)) {
//                    i = 0;
//                    break;
//                }
//                break;
//        }
//        switch (i) {
//            case 0:
//                SpannableString spannableString = new SpannableString(IMG_REPLACE_STR + empty + content);
//                spannableString.setSpan(new DynamicDrawableSpan(1) {
//                    public Drawable getDrawable() {
//                        Drawable finalDrawable = TintColorUtil.tintDrawable(context, context.getResources().getDrawable(2130837709), 2131755040);
//                        finalDrawable.setBounds(5, 5, finalDrawable.getIntrinsicWidth(), finalDrawable.getIntrinsicHeight());
//                        return finalDrawable;
//                    }
//                }, 0, 1, flags);
//                return spannableString;
//            default:
//                return new SpannableString(content);
//        }
//    }

//    public static Spannable getPopupCommentNameDateStatus(Context context, String screenName, String replyScreenName, String date) {
//        SpannableStringBuilder SSB;
//        int end;
//        int start;
//        if (TextUtil.isValidate(screenName) && TextUtil.isValidate(replyScreenName) && !screenName.equals(replyScreenName)) {
//            String reply = App.getResource().getString(R.string.TEXT_REPLY_TO);
//            SSB = new SpannableStringBuilder(screenName + empty + reply + empty + replyScreenName + empty + empty + empty + date);
//            end = screenName.length();
//            setLinkSpan(SSB, 48.lambdaFactory$(context, screenName), 0, end);
//            start = end + empty.length();
//            end = start + reply.length();
//            setDefaultDarkgraySpan(SSB, start, end);
//            start = end + empty.length();
//            end = start + replyScreenName.length();
//            setLinkSpan(SSB, 49.lambdaFactory$(context, replyScreenName), start, end);
//            start = end;
//            end = SSB.length();
//            setTextDpSizeSpan(SSB, 12, start, end);
//            setDefaultDarkgraySpan(SSB, start, end);
//            return SSB;
//        } else if (!TextUtil.isValidate(screenName)) {
//            return new SpannableStringBuilder();
//        } else {
//            SSB = new SpannableStringBuilder(screenName + empty + empty + empty + date);
//            end = screenName.length();
//            setLinkSpan(SSB, 50.lambdaFactory$(context, screenName), 0, end);
//            start = end;
//            end = SSB.length();
//            setTextDpSizeSpan(SSB, 12, start, end);
//            setDefaultDarkgraySpan(SSB, start, end);
//            return SSB;
//        }
//    }

//    public static Spannable getBoldPopupCommentNameDateStatusUseSpannableString(Context context, String screenName, String replyScreenName, String date) {
//        SpannableString SSB;
//        int end;
//        int firstScreenNameEnd;
//        int start;
//        if (TextUtil.isValidate(screenName) && TextUtil.isValidate(replyScreenName) && !screenName.equals(replyScreenName)) {
//            String reply = App.getResource().getString(2131296956);
//            SSB = new SpannableString(screenName + empty + reply + empty + replyScreenName + empty + empty + empty + date);
//            end = screenName.length();
//            firstScreenNameEnd = end;
//            start = end + empty.length();
//            end = start + reply.length();
//            setBlackDarkgraySpan(SSB, start, end);
//            start = end + empty.length();
//            end = start + replyScreenName.length();
//            int secondScreenNameStart = start;
//            int secondScreenNameEnd = end;
//            start = end;
//            end = SSB.length();
//            setTextDpSizeSpan(SSB, 12, start, end);
//            set92DarkgraySpan(SSB, start, end);
//            setBoldLinkSpan(SSB, 51.lambdaFactory$(context, screenName), 0, firstScreenNameEnd);
//            setBoldLinkSpan(SSB, 52.lambdaFactory$(context, replyScreenName), secondScreenNameStart, secondScreenNameEnd);
//            return SSB;
//        } else if (!TextUtil.isValidate(screenName)) {
//            return new SpannableStringBuilder();
//        } else {
//            SSB = new SpannableString(screenName + empty + empty + empty + date);
//            end = screenName.length();
//            firstScreenNameEnd = end;
//            start = end;
//            end = SSB.length();
//            setTextDpSizeSpan(SSB, 12, start, end);
//            set92DarkgraySpan(SSB, start, end);
//            setBoldLinkSpan(SSB, 53.lambdaFactory$(context, screenName), 0, firstScreenNameEnd);
//            return SSB;
//        }
//    }

//    public static Spannable getBoldChatNameDateStatusUseSpannableString(Context context, String screenName, String date) {
//        if (!TextUtil.isValidate(screenName)) {
//            return new SpannableStringBuilder();
//        }
//        SpannableString SSB = new SpannableString(screenName + empty + empty + empty + date);
//        int end = screenName.length();
//        int firstScreenNameEnd = end;
//        int start = end;
//        end = SSB.length();
//        setTextDpSizeSpan(SSB, 12, start, end);
//        set92DarkgraySpan(SSB, start, end);
//        setBoldLinkSpan(SSB, 54.lambdaFactory$(context, screenName), 0, firstScreenNameEnd);
//        return SSB;
//    }

//    public static Spannable getChatNameDateStatusUseSpannableString(Context context, String screenName, String date) {
//        if (!TextUtil.isValidate(screenName)) {
//            return new SpannableStringBuilder();
//        }
//        SpannableString SSB = new SpannableString(screenName + empty + empty + empty + date);
//        int end = screenName.length();
//        int firstScreenNameEnd = end;
//        int start = end;
//        end = SSB.length();
//        setTextDpSizeSpan(SSB, 12, start, end);
//        set92DarkgraySpan(SSB, start, end);
//        setLinkSpan(SSB, 55.lambdaFactory$(context, screenName), 0, firstScreenNameEnd);
//        return SSB;
//    }

//    public static Spannable getBoldPopupCommentNameDateStatus(Context context, String screenName, String replyScreenName, String date) {
//        SpannableStringBuilder SSB;
//        int end;
//        int firstScreenNameEnd;
//        int start;
//        if (TextUtil.isValidate(screenName) && TextUtil.isValidate(replyScreenName) && !screenName.equals(replyScreenName)) {
//            String reply = App.getResource().getString(2131296956);
//            SSB = new SpannableStringBuilder(screenName + empty + reply + empty + replyScreenName + empty + empty + empty + date);
//            end = screenName.length();
//            firstScreenNameEnd = end;
//            start = end + empty.length();
//            end = start + reply.length();
//            setBlackDarkgraySpan(SSB, start, end);
//            start = end + empty.length();
//            end = start + replyScreenName.length();
//            int secondScreenNameStart = start;
//            int secondScreenNameEnd = end;
//            start = end;
//            end = SSB.length();
//            setTextDpSizeSpan(SSB, 12, start, end);
//            set92DarkgraySpan(SSB, start, end);
//            setBoldLinkSpan(SSB, 56.lambdaFactory$(context, screenName), 0, firstScreenNameEnd);
//            setBoldLinkSpan(SSB, 57.lambdaFactory$(context, replyScreenName), secondScreenNameStart, secondScreenNameEnd);
//            return SSB;
//        } else if (!TextUtil.isValidate(screenName)) {
//            return new SpannableStringBuilder();
//        } else {
//            SSB = new SpannableStringBuilder(screenName + empty + empty + empty + date);
//            end = screenName.length();
//            firstScreenNameEnd = end;
//            start = end;
//            end = SSB.length();
//            setTextDpSizeSpan(SSB, 12, start, end);
//            set92DarkgraySpan(SSB, start, end);
//            setBoldLinkSpan(SSB, 58.lambdaFactory$(context, screenName), 0, firstScreenNameEnd);
//            return SSB;
//        }
//    }

//    public static Spannable getBoldSpan(String content) {
//        if (TextUtils.isEmpty(content)) {
//            return null;
//        }
//        Spannable spannableString = new SpannableString(content);
//        spannableString.setSpan(new StyleSpan(1), 0, content.length(), flags);
//        return spannableString;
//    }
//
//    public static Spannable getBoldBlackSpan(String content) {
//        if (TextUtils.isEmpty(content)) {
//            return null;
//        }
//        Spannable spannableString = new SpannableString(content);
//        spannableString.setSpan(new ForegroundColorSpan(App.getResource().getColor(R.color.colorBlack)), 0, content.length(), flags);
//        spannableString.setSpan(new StyleSpan(1), 0, content.length(), flags);
//        return spannableString;
//    }
//
//    public static Spannable getAreYouSure(OnClickListener serviceTermListener, OnClickListener privacyPolicyListener) {
//        String creating = App.getResource().getString(R.string.TEXT_DESC_TERM_BY_CREATING);
//        String service = App.getResource().getString(R.string.TEXT_DESC_TERM_SERVICE);
//        String and = App.getResource().getString(R.string.TEXT_DESC_TERM_AND);
//        String privacy = App.getResource().getString(R.string.TEXT_DESC_TERM_PRIVACY);
//        String period = App.getResource().getString(R.string.TEXT_DESC_TERM_PERIOD);
//        SpannableString SS = new SpannableString(creating + empty + service + empty + and + empty + privacy + empty + period);
//        int end = creating.length() + empty.length();
//        setDefaultGraySpan(SS, 0, end);
//        int start = end;
//        end += service.length();
//        setLinkUnderlineSpan(SS, serviceTermListener, start, end);
//        start = end;
//        end = ((empty.length() + end) + and.length()) + empty.length();
//        setDefaultGraySpan(SS, start, end);
//        start = end;
//        end += privacy.length();
//        setLinkUnderlineSpan(SS, privacyPolicyListener, start, end);
//        setDefaultGraySpan(SS, end, (empty.length() + end) + period.length());
//        return SS;
//    }

//    public static void setDefaultGraySpan(SpannableString SS, int start, int end) {
//        SS.setSpan(new ForegroundColorSpan(App.getResource().getColor(R.color.color92)), start, end, flags);
//        SS.setSpan(new BackgroundColorSpan(App.getResource().getColor(R.color.colorAccent)), start, end, flags);
//    }
//
//    public static void setLinkUnderlineSpan(SpannableString SS, OnClickListener userListener, int start, int end) {
//        SS.setSpan(new ForegroundColorSpan(App.getResource().getColor(R.color.color92)), start, end, flags);
//        SS.setSpan(new LinkUnderlineSpan(App.getResource().getColor(R.color.color92), start, end, userListener), start, end, flags);
//        SS.setSpan(new BackgroundColorSpan(App.getResource().getColor(R.color.colorAccent)), start, end, flags);
//    }
//
//    public static Spannable getLeadingMarginSpan(String content, int space) {
//        if (TextUtil.isNull(content)) {
//            return new SpannableString("");
//        }
//        Spannable spannableString = new SpannableString(content);
//        setLeadingMarginSpan(spannableString, space);
//        return spannableString;
//    }

//    public static void setLeadingMarginSpan(Spannable span, int space) {
//        span.setSpan(new Standard(space, 0), 0, 1, 0);
//    }
}