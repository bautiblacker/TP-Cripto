package utils;

import java.io.File;
import java.io.FileNotFoundException;

public class FileUtils {

    public static File parseFile(String path) throws FileNotFoundException {
        File file = new File(path);
        if(file.exists()) {
            return file;
        }

        throw new FileNotFoundException(String.format("File %s does not exists", path));
    }
}
