package com.apixandru;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import static com.apixandru.IoUtils.extractLines;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public interface Shell extends Closeable {

    String execute(String command);

    default List<String> executeGetList(String command) {
        String execute = execute(command);
        if (execute.isEmpty()) {
            return emptyList();
        }
        return extractLines(execute);
    }

    default List<AdbFile> ls(String where) {
        return executeGetList("ls -1 " + where)
                .stream()
                .map(fileName -> new AdbFile(fileName.replace("\\", ""), where))
                .collect(toList());
    }

    default List<AdbFile> ls(AdbFile adbFile) {
        return ls(adbFile.getAbsolutePath());
    }

    void pull(String remote, String local) throws IOException;

    default boolean isDirectory(String who) {
        String execute = execute("file -L " + who);
        return execute.equals(who + ": directory\n");
    }

    default boolean isDirectory(AdbFile file) {
        return isDirectory(file.getAbsolutePath());
    }

}
