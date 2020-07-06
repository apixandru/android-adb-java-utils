package com.apixandru;

import java.util.List;

import static com.apixandru.IoUtils.extractLines;

public interface Shell {

    String execute(String command);

    default List<String> executeGetList(String command) {
        return extractLines(execute(command));
    }

    default List<String> ls(String where) {
        return executeGetList("ls -1 " + where);
    }

    default boolean isDirectory(String who) {
        String execute = execute("file -L " + who);
        return execute.equals(who + ": directory\n");
    }

}
