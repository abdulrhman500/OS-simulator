package parser;

import exceptions.InvalidResourceException;
import memory.Memory;
import mutex.Mutexes;
import process.Process;
import systemcalls.systemCalls;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser {
    public static void parse(String data,Process p) {
        System.out.println("Parser| Parsing Command: " + data);
        String[] arr = data.split(" ");
        try {
            switch (arr[0]) {
                case "semWait" -> callSemWait(arr[1], p);
                case "semSignal" -> callSemSignal(arr[1], p);
                case "print" -> callPrint(arr,p);
                case "assign" -> callAssign(arr,p);
                case "writeFile" -> System.out.println("writeFile " + arr[1] + " " + arr[2]); //TODO write to file and check for mutex
                case "readFile" -> System.out.println("readFile " + arr[1]); //TODO read from file and check for mutex
                case "printFromTo" -> System.out.println("printFromTo " + arr[1] + " " + arr[2]); //TODO print from to and check for mutex
                default -> System.out.println("Invalid command"); //TODO add exception
            }
        }catch (InvalidResourceException e){
            System.err.println("Invalid Resource " + arr[1]);
        }

    }

    private static void callAssign(String[] arr, Process p) {
        if(arr[2].equalsIgnoreCase("input")){
            System.out.println("parser| waiting for input: ");
            Scanner sc =new Scanner(System.in);
            int x= sc.nextInt();
//            sc.close();
            p.setVariable(arr[1],x);
        }else{
            p.setVariable(arr[1],Integer.parseInt(arr[2]));
        }
    }

    private static void callPrint(String[] arr, Process p) {
        if(arr.length==2){
            systemCalls.print(p.getVariable(arr[1]));
        }else{
            StringBuilder sb = new StringBuilder();
            for(int i=1;i<arr.length;i++){
                sb.append(arr[i]).append(" ");
            }
            systemCalls.print(sb.toString());
        }
    }

    private static void callSemSignal(String s, Process p) throws InvalidResourceException {
        if(s.equalsIgnoreCase("userinput")){
            systemCalls.semSignal(Mutexes.USERINPUT,p);
        }
        if(s.equalsIgnoreCase("file")){
            systemCalls.semSignal(Mutexes.FILE,p);
        }
        if(s.equalsIgnoreCase("userOutput")){
            systemCalls.semSignal(Mutexes.SCREENOUTPUT,p);
        }
    }

    private static void callSemWait(String s,Process p) throws InvalidResourceException {
        if(s.equalsIgnoreCase("userinput")){
            systemCalls.semWait(Mutexes.USERINPUT,p);
        }
        if(s.equalsIgnoreCase("file")){
            systemCalls.semWait(Mutexes.FILE,p);
        }
        if(s.equalsIgnoreCase("userOutput")){
            systemCalls.semWait(Mutexes.SCREENOUTPUT,p);
        }
    }

}
