package com.apixandru;

public final class PathUtils {

    private PathUtils() {
    }

    public static String joinPath(String part1, String part2) {
        String result = part1;
        if (!part1.endsWith("/")) {
            result += '/';
        }
        if (part2.startsWith("/")) {
            return result + part2.substring(1);
        }
        return result + part2;
    }

}
