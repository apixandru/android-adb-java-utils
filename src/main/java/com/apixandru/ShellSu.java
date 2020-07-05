package com.apixandru;

import se.vidstige.jadb.JadbDevice;

public class ShellSu extends ShellRegular {

    public ShellSu(JadbDevice device) {
        super(device);
    }

    @Override
    public String execute(String command) {
        return super.execute("su -c '" + command + "'");
    }

}
