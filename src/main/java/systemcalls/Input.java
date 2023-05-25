package systemcalls;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Input implements SystemCalls{

        public void execute() {

        }

        public String readFile(String filePath) throws FileNotFoundException {
            Scanner sc = new Scanner(new FileReader(new File(filePath)));
            return sc.nextLine();
        }

        public String readFromScreen() {
            Scanner sc = new Scanner(System.in);
            return sc.nextLine();
        }

        public Process getProcessFromMemory(int process)

}
