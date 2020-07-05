package com.apixandru;

import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.apixandru.IoUtils.readStream;

public class ShellRegular implements Shell {

    private final JadbDevice device;

    public ShellRegular(JadbDevice device) {
        this.device = device;
    }

    @Override
    public String execute(String command) {
        try {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(device.executeShell(command)))) {
                return readStream(br);
            }
        } catch (IOException | JadbException e) {
            throw new IllegalStateException("Failed to execute " + command, e);
        }
    }

}
