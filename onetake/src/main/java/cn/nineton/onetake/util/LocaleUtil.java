package cn.nineton.onetake.util;

import android.os.Build.VERSION;

import java.util.Locale;

public class LocaleUtil {
    public static final String AreaCode = "86";
    public static final String CNCode = "CN";
    public static final int CNS_CODE = 1;
    public static final int CNT_CODE = 2;
    public static final String Chinese = "cn";
    public static final String HKCode = "HK";
    public static final String TWCode = "TW";

    public static final String[] CountryCodes = new String[]{"93", "AF", "355", "AL", "213", "DZ", "1", "AS",
            "376", "AD", "244", "AO", "1", "AI", "1", "AG", "54", "AR", "374", "AM", "297", "AW", "247", "AC",
            "61", "AU", "672", "AX", "43", "AT", "994", "AZ", "1", "BS", "973", "BH", "880", "BD", "1", "BB",
            "375", "BY", "32", "BE", "501", "BZ", "229", "BJ", "1", "BM", "975", "BT", "591", "BO", "387", "BA",
            "267", "BW", "55", "BR", "1", "VG", "673", "BN", "359", "BG", "226", "BF", "257", "BI", "855", "KH",
            "237", "CM", "1", "CA", "238", "CV", "1", "KY", "236", "CF", "235", "TD", "56", "CL", AreaCode, "CN",
            "57", "CO", "269", "KM", "242", "CG", "682", "CK", "506", "CR", "225", "CI", "385", "HR", "53", "CU",
            "357", "CY", "420", "CZ", "243", "CD", "45", "DK", "246", "DG", "253", "DJ", "1", "DM", "1", "DO",
            "670", "TL", "593", "EC", "20", "EG", "503", "SV", "240", "GQ", "291", "ER", "372", "EE", "251",
            "ET", "500", "FK", "298", "FO", "679", "FJ", "358", "FI", "33", "FR", "594", "GF", "689", "PF",
            "241", "GA", "220", "GM", "995", "GE", "49", "DE", "233", "GH", "350", "GI", "30", "GR", "299",
            "GL", "1", "GD", "590", "GP", "1", "GU", "502", "GT", "224", "GN", "245", "GW", "592", "GY",
            "509", "HT", "504", "HN", "852", HKCode, "36", "HU", "354", "IS", "91", "IN",
            "62", "ID", "98", "IR", "964", "IQ", "353", "IE", "972", "IL", "39", "IT",
            "1", "JM", "81", "JP", "962", "JO", "7", "KZ", "254", "KE", "686", "KI",
            "965", "KW", "996", "KG", "856", "LA", "371", "LV", "961", "LB", "266",
            "LS", "231", "LR", "218", "LY", "423", "LI", "370", "LT", "352", "LU",
            "853", "MO", "389", "MK", "261", "MG", "265", "MW", "60", "MY", "960",
            "MV", "223", "ML", "356", "MT", "692", "MH", "596", "MQ", "222", "MR",
            "230", "MU", "52", "MX", "691", "FM", "373", "MD", "377", "MC", "976",
            "MN", "382", "ME", "1", "MS", "212", "MA", "258", "MZ", "95", "MM", "264",
            "NA", "674", "NR", "977", "NP", "31", "NL", "599", "AN", "687", "NC", "64",
            "NZ", "505", "NI", "227", "NE", "234", "NG", "683", "NU", "850", "KP", "1",
            "MP", "47", "NO", "968", "OM", "92", "PK", "680", "PW", "970", "PS", "507",
            "PA", "675", "PG", "595", "PY", "51", "PE", "63", "PH", "48", "PL", "351",
            "PT", "1", "PR", "974", "QA", "262", "RE", "40", "RO", "7", "RU", "250",
            "RW", "290", "SH", "1", "KN", "1", "LC", "508", "PM", "1", "VC", "685",
            "WS", "378", "SM", "239", "ST", "966", "SA", "221", "SN", "381", "RS",
            "248", "SC", "232", "SL", "65", "SG", "421", "SK", "386", "SI", "677",
            "SB", "252", "SO", "27", "ZA", "82", "KR", "34", "ES", "94", "LK", "249",
            "SD", "597", "SR", "268", "SZ", "46", "SE", "41", "CH", "963", "SY", "886",
            TWCode, "992", "TJ", "255", "TZ", "66", "TH", "228", "TG", "690", "TK", "676",
            "TO", "1", "TT", "216", "TN", "90", "TR", "993", "TM", "1", "TC", "688", "TV",
            "1", "VI", "256", "UG", "380", "UA", "971", "AE", "44", "GB", "1", "US", "598",
            "UY", "998", "UZ", "678", "VU", "379", "VA", "58", "VE", "84", "VN", "681", "WF"
            , "967", "YE", "260", "ZM", "263", "ZW"};
    public static final int EN_CODE = 3;
    public static final String English = "en";
    public static final String JudgeChineseCode = "CN";

    public static String getCurrentCountryCode() {
        return Locale.getDefault().getCountry();
    }

    public static String getCurrentLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public static boolean isChinese() {
        return isChineseCode();
    }

    public static boolean isChineseCode() {
        return getCurrentCountryCode().equals("CN") || getCurrentCountryCode().equals(HKCode) || getCurrentCountryCode().equals(TWCode);
    }

    public static boolean isJudgeChineseCode() {
        return getCurrentCountryCode().equals("CN");
    }

    public static String getBcp47Language() {
        Locale loc = Locale.getDefault();
        if (VERSION.SDK_INT >= 21) {
            return loc.toLanguageTag();
        }
        String language = loc.getLanguage();
        String region = loc.getCountry();
        String variant = loc.getVariant();
        if (language.equals("no") && region.equals("NO") && variant.equals("NY")) {
            language = "nn";
            region = "NO";
            variant = "";
        }
        if (language.isEmpty() || !language.matches("\\p{Alpha}{2,8}")) {
            language = "und";
        } else if (language.equals("iw")) {
            language = "he";
        } else if (language.equals("in")) {
            language = "id";
        } else if (language.equals("ji")) {
            language = "yi";
        }
        if (!region.matches("\\p{Alpha}{2}|\\p{Digit}{3}")) {
            region = "";
        }
        if (!variant.matches("\\p{Alnum}{5,8}|\\p{Digit}\\p{Alnum}{3}")) {
            variant = "";
        }
        StringBuilder bcp47Tag = new StringBuilder(language);
        if (!region.isEmpty()) {
            bcp47Tag.append('-').append(region);
        }
        if (!variant.isEmpty()) {
            bcp47Tag.append('-').append(variant);
        }
        return bcp47Tag.toString();
    }

    public static int getLanguageCode() {
        if (getCurrentCountryCode().equals("CN")) {
            return 1;
        }
        if (getCurrentCountryCode().equals(HKCode) || getCurrentCountryCode().equals(TWCode)) {
            return 2;
        }
        return 3;
    }
}