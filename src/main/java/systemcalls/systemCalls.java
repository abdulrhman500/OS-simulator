package systemcalls;

import exceptions.IllegalMemoryAccessException;
import exceptions.InvalidResourceException;
import memory.Memory;
import mutex.Mutex;
import mutex.Mutexes;
import process.Process;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class systemCalls {
    public static void print(String s) {
        System.out.println("======> "+s+"<======");
    }

    public static void assign(int address, String newName, Object value) throws IllegalMemoryAccessException {
        Memory.getInstance().storeWord(address, newName, value);
    }

    public static void writeFile(String filePath, String text) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(filePath);
        pw.println(text);
        pw.close();
    }

    public static String readFile(String filePath) throws FileNotFoundException {
        Scanner sc = new Scanner(new FileReader(new File(filePath)));
        StringBuilder sb =new StringBuilder();
        while (sc.hasNextLine()){
            sb.append(sc.nextLine());
        }
        return sb.toString();
    }

    public static void printFromTo(int x,int y){
        for(int i=x;i<=y;i++){
            print(i+"");
        }
    }

    public static void semWait(Mutexes m,Process p) throws InvalidResourceException {
        Mutex.semWait(p,m);
    }

    public static void semSignal(Mutexes m,Process p) throws InvalidResourceException {
        Mutex.semSignal(p,m);
    }

}
