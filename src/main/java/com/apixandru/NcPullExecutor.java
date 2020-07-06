package com.apixandru;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static com.apixandru.PathUtils.*;

class NcPullExecutor {

    private static final Logger log = LoggerFactory.getLogger(NcPullExecutor.class);

    private final ShellRegular shell;
    private final NcListener ncListener;

    private int totalToVisit;
    private int totalVisited;

    NcPullExecutor(ShellRegular shell, NcListener ncListener) {
        this.shell = shell;
        this.ncListener = ncListener;
    }

    synchronized void doPull(String rootRemoteName, String rootLocalName) throws IOException {
        Map<String, Boolean> files = FileTree.loadTree(shell, rootRemoteName);
        totalToVisit = files.size();
        this.totalVisited = 0;
        for (Map.Entry<String, Boolean> fileEntry : files.entrySet()) {

            String currentRemotePath = fileEntry.getKey();
            String relativePath = getRelativePath(rootRemoteName, currentRemotePath);
            String currentLocalPath = normalizeForWindows(joinPath(rootLocalName, relativePath));
            boolean directory = fileEntry.getValue();

            doPull(currentRemotePath, currentLocalPath, directory);
        }
    }

    private void doPull(String currentRemotePath, String currentLocalPath, boolean directory) throws IOException {
        if (directory) {
            pullDirectory(currentRemotePath, currentLocalPath);
        } else {
            pullFile(currentRemotePath, currentLocalPath);
        }
    }

    private void pullDirectory(String remoteFolder, String localFolder) {
        log.info("[{}/{}] Pulling {} to {}", totalVisited++, totalToVisit, remoteFolder, localFolder);
        new File(localFolder)
                .mkdirs();
    }

    private void pullFile(String remoteFileAbsolutePath, String localFileName) throws IOException {
        log.info("[{}/{}] Downloading file {} to {}", totalVisited++, totalToVisit, remoteFileAbsolutePath, localFileName);
        File file = new File(localFileName);
        Path path = file.toPath();
        Files.write(path, ncPull(remoteFileAbsolutePath));
    }

    private byte[] ncPull(String remoteFileAbsolutePath) throws IOException {
        String result = shell.execute(ncListener.getNcBootstrap() + " < \"" + remoteFileAbsolutePath + "\"");
        if (!result.isEmpty()) {
            throw new IllegalStateException(result);
        }
        return ncListener.popLastFileContents();
    }

}
