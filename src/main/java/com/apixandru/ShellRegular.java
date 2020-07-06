package com.apixandru;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.apixandru.IoUtils.readStream;

class ShellRegular implements Shell {

    private static final Logger log = LoggerFactory.getLogger(ShellRegular.class);

    private final NcListener ncListener = new NcListener();
    private final JadbDevice device;

    ShellRegular(JadbDevice device) {
        this.device = device;

        Thread ncServer = new Thread(ncListener, "NetCat");
        ncServer.setDaemon(true);
        ncServer.start();
    }

    @Override
    public String execute(String command) {
        log.debug("Executing " + command);
        try {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(device.executeShell(command)))) {
                String readStream = readStream(br);
                if (readStream.endsWith(": Permission denied\n")) {
                    throw new IllegalStateException(readStream);
                }
                return readStream;
            }
        } catch (IOException | JadbException e) {
            throw new IllegalStateException("Failed to execute " + command, e);
        }
    }

    @Override
    public void pull(String remote, String local) throws IOException {
        new NcPullExecutor(this, ncListener)
                .doPull(remote, new File(local));
    }

    @Override
    public void close() throws IOException {
        ncListener.close();
    }

}
