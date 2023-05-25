package systemcalls;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Output implements SystemCalls {

    public void execute() {

    }

    public void printOnScreen(String filePath, String text) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(filePath);
        pw.println(text);
        pw.close();
    }
}
