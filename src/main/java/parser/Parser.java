package parser;

import process.Process;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser {
    public static void parse(String data) {
        String[] arr = data.split(" ");
        switch (arr[0]){
            case "semWait" -> System.out.println("semWait " + arr[1]); //TODO actually call semWait and SemSignal and check for resource in enum
            case "semSignal" -> System.out.println("semSignal " +arr[1]);
            case "print" -> System.out.println("print "); //TODO call a print that checks for mutex and add all the array except for the first element
            case "assign" -> System.out.println("assign " + arr[1] + " " + arr[2]); //TODO call memory assign
            case "writeFile" -> System.out.println("writeFile " + arr[1] + " " + arr[2]); //TODO write to file and check for mutex
            case "readFile" -> System.out.println("readFile " + arr[1]); //TODO read from file and check for mutex
            case "printFromTo" ->System.out.println("printFromTo " + arr[1] + " " + arr[2]); //TODO print from to and check for mutex
            default -> System.out.println("Invalid command"); //TODO add exception
        }

    }

    public static void main(String[] args) throws FileNotFoundException {
        parse("Program_1.txt");
    }
}
