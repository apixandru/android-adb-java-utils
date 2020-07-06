package com.apixandru;

import java.util.Arrays;
import java.util.List;

class IoUtils {

    static List<String> extractLines(String input) {
        String[] lines = input.replace("\r\n", "\n")
                .replace("\r", "\n")
                .split("\n");
        return Arrays.asList(lines);
    }


}
