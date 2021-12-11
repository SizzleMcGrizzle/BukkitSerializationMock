package me.p3074098.bukkitserializationmock;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigurationFileUtils {

    /**
     * @param directoryName Parent directory name inside user application folder
     */
    public static File getApplicationDirectory(String directoryName) {
        Path basePath;
        if(System.getProperty("os.name").startsWith("Windows")) {
            basePath = Paths.get(System.getenv("APPDATA")).resolve(directoryName);
        } else if(System.getProperty("os.name").startsWith("Mac")) {
            basePath = Paths.get(System.getProperty("user.home")).resolve("Library").resolve("Application Support").resolve(directoryName);
        } else {
            basePath = Paths.get(System.getProperty("user.home")).resolve(directoryName);
        }

        return new File(basePath.toString());
    }

    public static File getApplicationFile(String directoryName, String path) {
        return new File(getApplicationDirectory(directoryName), path);
    }

    public static boolean createParentDirectories(File file) {
       return file.getParentFile().mkdirs();
    }

    public static boolean createFileAndParents(File file) {
        try {
            return createParentDirectories(file) && file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


}
