package com.apixandru;

public class Main {

    private static final String TARGET_DEVICE = "FA77M0305761";
    private static final String FOLDER_TO_COPY = "/sdcard";

    public static void main(String[] args) {
        Adb adb = new Adb();
        AdbDevice device = adb.getAdbDevice(TARGET_DEVICE);
        for (String remoteFile : device.suShell().ls(FOLDER_TO_COPY)) {
            System.out.println(remoteFile);
        }
        for (String remoteFile : device.shell().ls(FOLDER_TO_COPY)) {
            System.out.println(remoteFile);
        }
    }

}
