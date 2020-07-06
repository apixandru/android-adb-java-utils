package com.apixandru;

import java.util.List;
import java.util.stream.Collectors;

import static com.apixandru.IoUtils.extractLines;

public interface Shell {

    String execute(String command);

    default List<String> executeGetList(String command) {
        return extractLines(execute(command));
    }

    default List<AdbFile> ls(String where) {
        return executeGetList("ls -1 " + where)
                .stream()
                .map(fileName -> new AdbFile(fileName, where))
                .collect(Collectors.toList());
    }

    default boolean isDirectory(String who) {
        String execute = execute("file -L " + who);
        return execute.equals(who + ": directory\n");
    }

    default boolean isDirectory(AdbFile file) {
        return isDirectory(file.getAbsolutePath());
    }

}
