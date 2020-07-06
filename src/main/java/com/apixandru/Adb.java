package com.apixandru;

import se.vidstige.jadb.JadbConnection;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;

import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Adb {

    private final JadbConnection jadb = new JadbConnection();

    private List<JadbDevice> getDevicesRaw() {
        try {
            return jadb.getDevices();
        } catch (IOException | JadbException e) {
            throw new IllegalStateException("Cannot determine device list.", e);
        }
    }

    public List<AdbDevice> getDevices() {
        return getDevicesRaw()
                .stream()
                .map(AdbDevice::new)
                .collect(toList());
    }

    public AdbDevice getDevice(String deviceSerial) {
        return getDevices()
                .stream()
                .filter(device -> deviceSerial.equals(device.getSerial()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Device " + deviceSerial + " is not connected!"));
    }

}
