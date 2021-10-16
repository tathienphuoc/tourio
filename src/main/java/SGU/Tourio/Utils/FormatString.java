package SGU.Tourio.Utils;

import org.apache.commons.lang3.StringUtils;

public class FormatString {
    public static String TitleCase(String str) {
        return StringUtils.capitalize(str.trim().replaceAll("(?U)\\s+", ""));
    }
}
