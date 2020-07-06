import com.apixandru.Adb;
import com.apixandru.AdbDevice;
import com.apixandru.AdbFile;
import com.apixandru.Shell;

public class Main {

    private static final String TARGET_DEVICE = "FA77M0305761";
    private static final String FOLDER_TO_COPY = "/sdcard/";

    public static void main(String[] args) {
        Adb adb = new Adb();
        AdbDevice device = adb.getAdbDevice(TARGET_DEVICE);
        executeInShell(device.suShell());
        executeInShell(device.shell());
    }

    private static void executeInShell(Shell shell) {
        for (AdbFile file : shell.ls(FOLDER_TO_COPY)) {
            System.out.println(file.getName() + " " + shell.isDirectory(file));
        }
    }

}
