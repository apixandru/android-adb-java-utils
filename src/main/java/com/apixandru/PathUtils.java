package com.apixandru;

final class PathUtils {

    private PathUtils() {
    }

    static String joinPath(String part1, String part2) {
        String result = part1;
        if (!part1.endsWith("/")) {
            result += '/';
        }
        if (part2.startsWith("/")) {
            return result + part2.substring(1);
        }
        return result + part2;
    }


    static String getRelativePath(String parent, String child) {
        if (child.startsWith(parent)) {
            return child.substring(parent.length());
        }
        throw new IllegalStateException("This should never be the case.");
    }

    static String normalizeForWindows(String unsafeName) {
        return unsafeName.replace(":", "__");
    }

}
