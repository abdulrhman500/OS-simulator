package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class programInfo {

    public static int getTotalNumberOfInstruction(String path) {
        int count = 0;
        try {
            Scanner sc = new Scanner(new File(path));
            while (sc.hasNext()) count++;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return count;
    }
}
