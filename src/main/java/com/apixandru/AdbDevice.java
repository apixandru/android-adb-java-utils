package com.apixandru;

import se.vidstige.jadb.JadbDevice;

public class AdbDevice {

    private final JadbDevice device;

    AdbDevice(JadbDevice device) {
        this.device = device;
    }

    Shell suShell() {
        return new ShellSu(device);
    }

    Shell shell() {
        return new ShellRegular(device);
    }

}
