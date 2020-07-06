package com.apixandru;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;

import static org.apache.commons.io.IOUtils.toByteArray;

final class NcListener implements Runnable, Closeable {

    private static final Logger log = LoggerFactory.getLogger(NcListener.class);

    private final ServerSocket serverSocket;
    private final String hostAddress;

    private final LinkedBlockingDeque<byte[]> contents = new LinkedBlockingDeque<>(1);

    private volatile boolean listening = true;

    NcListener() {
        try {
            int randomPort = new Random()
                    .nextInt(10_000) + 20_000;

            this.hostAddress = InetAddress.getLocalHost()
                    .getHostAddress();

            serverSocket = new ServerSocket(randomPort);

            log.debug("Listening at " + hostAddress + ":" + randomPort);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to instantiate server socket!");
        }
    }

    byte[] popLastFileContents() throws IOException {
        try {
            return contents.takeFirst();
        } catch (InterruptedException e) {
            throw new IOException("Failed to download file", e);
        }
    }

    @Override
    public void close() throws IOException {
        listening = false;
        serverSocket.close();
    }

    @Override
    public void run() {
        while (listening) {
            try {
                try (Socket socket = serverSocket.accept();
                     OutputStream ignored = socket.getOutputStream();
                     InputStream inputStream = new BufferedInputStream(socket.getInputStream())) {

                    log.debug("Accepted connection " + socket.getPort());
                    byte[] data = toByteArray(inputStream);

                    contents.putLast(data);
                }
            } catch (InterruptedException ex) {
                log.debug("Interrupted");
            } catch (IOException e) {
                if (!requestedClose(e)) {
                    throw new IllegalStateException("Failed to download file!", e);
                }
            }
        }
        log.debug("Socket closed.");
    }

    private boolean requestedClose(IOException e) {
        return !listening
                && e instanceof SocketException
                && e.getMessage().equals("Socket closed");
    }

    public String getNcBootstrap() {
        return "nc " + hostAddress + " " + serverSocket.getLocalPort();
    }

}
