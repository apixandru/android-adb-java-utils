package com.apixandru;

import se.vidstige.jadb.JadbConnection;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;

import java.io.IOException;
import java.util.List;

public class Adb {

    private final JadbConnection jadb = new JadbConnection();

    public List<JadbDevice> getDevices() {
        try {
            return jadb.getDevices();
        } catch (IOException | JadbException e) {
            throw new IllegalStateException("Cannot determine device list.", e);
        }
    }

    public JadbDevice getDevice(String deviceSerial) {
        return getDevices()
                .stream()
                .filter(device -> deviceSerial.equals(device.getSerial()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Device " + deviceSerial + " is not connected!"));
    }

    public AdbDevice getAdbDevice(String deviceSerial) {
        return new AdbDevice(getDevice(deviceSerial));
    }

}
