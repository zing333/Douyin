package cn.nineton.onetake.util;

import java.util.regex.Pattern;

public class EmojiUtil {
    public static final String EMOJI_RANGE_REGEX = "[🌀-🗿]|[😀-🙏]|[🚀-🛿]|[☀-⛿]|[✀-➿]";
    public static final Pattern PATTERN = Pattern.compile(EMOJI_RANGE_REGEX);
}