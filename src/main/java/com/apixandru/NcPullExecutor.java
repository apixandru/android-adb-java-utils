package com.apixandru;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

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

    private AtomicInteger countVisits(String remote) {
        AtomicInteger counter = new AtomicInteger();
        AtomicReference<String> lastValue = new AtomicReference<>("");
        countVisits(remote, counter, lastValue);
        clearLastLine(lastValue);
        return counter;
    }

    private void countVisits(String remote, AtomicInteger buffer, AtomicReference<String> lastValue) {
        logCountStatus(lastValue, buffer.incrementAndGet());

        if (shell.isDirectory(remote)) {
            for (AdbFile remoteFile : shell.ls(remote)) {
                if (shell.isDirectory(remoteFile)) {
                    countVisits(remoteFile.getAbsolutePath(), buffer, lastValue);
                } else {
                    buffer.incrementAndGet();
                }
            }
        }
    }

    private void logCountStatus(AtomicReference<String> lastValue, int currentVisit) {
        clearLastLine(lastValue);
        String current = String.format("Counting elements to process... %4s", currentVisit);
        lastValue.set(current);
        System.out.print(current);
    }

    private void clearLastLine(AtomicReference<String> lastValue) {
        System.out.print(lastValue.get().replaceAll(".", "\b"));
    }

    synchronized void doPull(String remote, File local) throws IOException {
        AtomicInteger counter = countVisits(remote);
        this.totalToVisit = counter.get();
        this.totalVisited = 0;
        if (shell.isDirectory(remote)) {
            pullDirectory(remote, local);
        } else {
            pullFile(remote, local);
        }
    }

    private void pullDirectory(String remoteFolder, File localFolder) throws IOException {
        log.info("[{}/{}] Pulling {} to {}", totalVisited++, totalToVisit, remoteFolder, localFolder);
        localFolder.mkdirs();
        for (AdbFile remoteFile : shell.ls(remoteFolder)) {
            File localFile = new File(localFolder, remoteFile.getName());
            if (shell.isDirectory(remoteFile)) {
                pullDirectory(remoteFile.getAbsolutePath(), localFile);
            } else {
                pullFile(remoteFile.getAbsolutePath(), localFile);
            }
        }
    }

    private void pullFile(String remoteFileAbsolutePath, File localFile) throws IOException {
        log.info("[{}/{}] Downloading file {} to {}", totalVisited++, totalToVisit, remoteFileAbsolutePath, localFile);
        Files.write(localFile.toPath(), ncPull(remoteFileAbsolutePath));
    }

    private byte[] ncPull(String remoteFileAbsolutePath) throws IOException {
        String result = shell.execute(ncListener.getNcBootstrap() + " < \"" + remoteFileAbsolutePath + "\"");
        if (!result.isEmpty()) {
            throw new IllegalStateException(result);
        }
        return ncListener.popLastFileContents();
    }

}
