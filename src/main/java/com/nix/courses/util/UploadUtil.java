package com.nix.courses.util;
import lombok.Getter;

import java.io.File;

public class UploadUtil {

    public static String getPath(Folder folder) {
        return new File("")
                .getAbsoluteFile() + "/" + folder.getFolder();
    }

    @Getter
    public enum Folder {

        CSV("csv");

        Folder(String folder) {
            this.folder = folder;
        }

        private final String folder;
    }
}