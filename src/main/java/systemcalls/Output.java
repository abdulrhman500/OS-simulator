package systemcalls;

import exceptions.IllegalMemoryAccessException;
import memory.Memory;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Output {


    public void writeFile(String filePath, String text) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(filePath);
        pw.println(text);
        pw.close();
    }

    public void printOnScreen(String s) {
        System.out.println(s);
    }

    public void writeToMemory(int address, String newName, Object value) throws IllegalMemoryAccessException {
        Memory.getInstance().storeWord(address, newName, value);
    }
}
