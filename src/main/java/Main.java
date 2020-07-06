import com.apixandru.Adb;
import com.apixandru.AdbDevice;
import com.apixandru.Shell;

import java.io.IOException;

public class Main {

    private static final String TARGET_DEVICE = "FA77M0305761";
    private static final String FOLDER_ON_PHONE = "/sdcard/";
    private static final String FOLDER_LOCAL = "target/sdcard/";

    public static void main(String[] args) throws IOException {
        Adb adb = new Adb();
        AdbDevice device = adb.getDevice(TARGET_DEVICE);
        executeInShell(device.suShell());
        executeInShell(device.shell());
    }

    private static void executeInShell(Shell shell) throws IOException {
        try (shell) {
            shell.pull(FOLDER_ON_PHONE, FOLDER_LOCAL);
        }
    }

}
