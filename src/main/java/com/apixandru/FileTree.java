package com.apixandru;

import java.util.LinkedHashMap;
import java.util.Map;

public class FileTree {

    private final Shell shell;

    private final Map<String, Boolean> files = new LinkedHashMap<>();

    private String lastValue = "";

    private FileTree(Shell shell, String root) {
        this.shell = shell;
        countVisits(root);
        clearLastLine();
    }

    public static Map<String, Boolean> loadTree(Shell shell, String root) {
        return new FileTree(shell, root)
                .getFiles();
    }

    private void countVisits(String remote) {
        boolean directory = shell.isDirectory(remote);
        trackFile(remote, directory);
        if (directory) {
            for (AdbFile remoteFile : shell.ls(remote)) {
                String remoteFileAbsolutePath = remoteFile.getAbsolutePath();
                if (shell.isDirectory(remoteFile)) {
                    countVisits(remoteFileAbsolutePath);
                } else {
                    trackFile(remoteFileAbsolutePath, false);
                }
            }
        }
    }

    private void trackFile(String remote, boolean directory) {
        files.put(remote, directory);
        logCountStatus();
    }

    private void logCountStatus() {
        clearLastLine();
        lastValue = String.format("Counting elements to process... %4s", files.size());
        System.out.print(lastValue);
    }

    private void clearLastLine() {
        System.out.print(lastValue.replaceAll(".", "\b"));
    }

    public Map<String, Boolean> getFiles() {
        return files;
    }

}
