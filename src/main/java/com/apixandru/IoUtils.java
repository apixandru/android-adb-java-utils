package com.apixandru;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class IoUtils {

    private static final int EOF = -1;

    static List<String> extractLines(String input) {
        String[] lines = input.replace("\r\n", "\n")
                .replace("\r", "\n")
                .split("\n");
        return Arrays.asList(lines);
    }

    /**
     * commons-io doesn't work in this scenario, neither does br.ready() and br.readLine()
     * i don't want to debug this right now
     */
    public static String readStream(BufferedReader br) throws IOException {
        StringBuilder fullBuffer = new StringBuilder();
        while (true) {
            int read = br.read();
            if (read == EOF) {
                break;
            }
            fullBuffer.append((char) read);
        }
        return fullBuffer.toString();
    }

}
