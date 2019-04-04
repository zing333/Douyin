package cn.nineton.onetake.util;

import org.joda.time.DateTime;

import cn.nineton.onetake.App;
import cn.nineton.onetake.R;

public class DateUtil {
    public static final long DayLong = 7;
    public static final long HourLong = 24;
    public static final long HrAgoLong = 86400;
    public static final long MinuteAgoLong = 3600;
    public static final long MinuteLong = 60;
    public static final long SecondAgoLong = 300;
    public static final long SecondLong = 60;
    public static final String day_ago = App.getResource().getString(R.string.TEXT_TIME_DAYS_AGO);
    public static final String hr_ago = App.getResource().getString(R.string.TEXT_TIME_HOURS_AGO);
    public static final String minute_ago = App.getResource().getString(R.string.TEXT_TIME_MINUTES_AGO);
    public static final String second_ago = App.getResource().getString(R.string.TEXT_TIME_JUST_NOW);

    public static String parseDates(String dateStr) {
        if (TextUtil.isNull(dateStr)) {
            return "";
        }
        try {
            long millonPeriod = Math.abs(new DateTime().toDate().getTime() - new DateTime(dateStr).toDate().getTime()) / 1000;
            if (millonPeriod < 300) {
                return second_ago;
            }
            if (millonPeriod < MinuteAgoLong) {
                return String.format(minute_ago, new Object[]{Integer.valueOf((int) Math.floor((double) (millonPeriod / 60)))});
            } else if (millonPeriod < HrAgoLong) {
                return String.format(hr_ago, new Object[]{Integer.valueOf((int) Math.floor((double) (millonPeriod / MinuteAgoLong)))});
            } else {
                return String.format(day_ago, new Object[]{Integer.valueOf((int) Math.floor((double) (millonPeriod / HrAgoLong)))});
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static String parseDateWithLong(long dateStr) {
        try {
            long millonPeriod = Math.abs(new DateTime().toDate().getTime() - dateStr) / 1000;
            if (millonPeriod < 300) {
                return second_ago;
            }
            if (millonPeriod < MinuteAgoLong) {
                return String.format(minute_ago, new Object[]{Integer.valueOf((int) Math.floor((double) (millonPeriod / 60)))});
            } else if (millonPeriod < HrAgoLong) {
                return String.format(hr_ago, new Object[]{Integer.valueOf((int) Math.floor((double) (millonPeriod / MinuteAgoLong)))});
            } else {
                return String.format(day_ago, new Object[]{Integer.valueOf((int) Math.floor((double) (millonPeriod / HrAgoLong)))});
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static long getTimestampByDatetime(String dateStr) {
        return new DateTime(dateStr).toDate().getTime();
    }

    public static String dealTime(String duration) {
        int minute;
        int time_int = (int) Float.parseFloat(duration);
        int second = time_int % 60;
        int tmp = time_int / 60;
        if (tmp >= 60) {
            int hour = tmp / 60;
            minute = tmp % 60;
        } else {
            minute = tmp;
        }
        return String.format("%s:%s", new Object[]{String.valueOf(minute), getSecondTimeString(second)});
    }

    private static String getSecondTimeString(int second) {
        if (second <= 0) {
            return "00";
        }
        if (second < 10) {
            return "0" + second;
        }
        return String.valueOf(second);
    }
}