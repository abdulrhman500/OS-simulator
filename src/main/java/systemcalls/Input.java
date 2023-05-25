package systemcalls;

import memory.Memory;
import memory.MemoryWord;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Input {

    public String readFile(String filePath) throws FileNotFoundException {
        Scanner sc = new Scanner(new FileReader(new File(filePath)));
        return sc.nextLine();
    }

    public String readFromScreen() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public Object readFromMemory(int address) {
        return Memory.getInstance().loadWord(address).getValue();
    }

}
