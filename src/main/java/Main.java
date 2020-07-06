import com.apixandru.Adb;
import com.apixandru.AdbDevice;
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
        for (String remoteFile : shell.ls(FOLDER_TO_COPY)) {
            String path = joinPath(FOLDER_TO_COPY, remoteFile);
            System.out.println(path + " " + shell.isDirectory(path));
        }
    }

    private static String joinPath(String part1, String part2) {
        String result = part1;
        if (!part1.endsWith("/")) {
            result += '/';
        }
        if (part2.startsWith("/")) {
            return result + part2.substring(1);
        }
        return result + part2;
    }
}
