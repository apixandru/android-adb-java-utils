package com.apixandru;

import se.vidstige.jadb.JadbDevice;

public class AdbDevice {

    private final JadbDevice device;

    AdbDevice(JadbDevice device) {
        this.device = device;
    }

    public Shell suShell() {
        return new ShellSu(device);
    }

    public Shell shell() {
        return new ShellRegular(device);
    }

}
