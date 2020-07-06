package com.apixandru;

public final class AdbFile {

    private final String name;
    private final String parentPath;

    public AdbFile(String name, String parentPath) {
        this.name = name;
        this.parentPath = parentPath;
    }

    public String getName() {
        return name;
    }

    public String getParentPath() {
        return parentPath;
    }

    public String getAbsolutePath() {
        return PathUtils.joinPath(parentPath, name);
    }

}
