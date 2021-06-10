package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {

    public static Map<Integer, Integer> getMappingByte(){
        Map<Integer, Integer> map = new HashMap<>();
        map.put(32768,15);
        map.put(16384,14);
        map.put(8192,13);
        map.put(4096,12);
        map.put(2048,11);
        map.put(1024,10);
        map.put(512,9);
        map.put(256,8);
        map.put(128,7);
        map.put(64,6);
        map.put(32,5);
        map.put(16,4);
        map.put(8,3);
        map.put(4,2);
        map.put(2,1);
        map.put(1,0);
        return map;
    }

    public static File parseFile(String path) throws FileNotFoundException {
        File file = new File(path);
        if(file.exists()) {
            return file;
        }

        throw new FileNotFoundException(String.format("File %s does not exists", path));
    }
}
