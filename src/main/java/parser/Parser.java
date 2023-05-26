package parser;

import exceptions.InvalidResourceException;
import mutex.Mutex;
import mutex.Mutexes;
import process.Process;
import scheduler.Scheduler;
import systemcalls.systemCalls;

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
                case "writeFile" ->  callWriteFile(arr[1],arr[2],p);
                case "readFile" -> System.out.println("readFile " + arr[1]); //TODO read from file and check for mutex
                case "printFromTo" -> callPrintFromTo(arr[1],arr[2],p);
                case "Halt" -> callHalt(p);
                default -> System.out.println("Invalid command"); //TODO add exception
            }
        }catch (InvalidResourceException e){
            System.err.println("Invalid Resource " + arr[1]);
        }

    }

    private static void callWriteFile(String s1, String s2, Process p) {
        checkFile(p);
        s1=p.getVariable(s1);
        s2=p.getVariable(s2);
        try{
            systemCalls.writeFile(s1,s2);
        }catch (Exception e){
            System.err.println("error Invalid file path");
        }
    }

    private static void checkFile(Process p){
        if(Mutex.fileMutexOwnerProcessID !=p.getId()){
            //TODO check if we should block the process instead
            System.err.println("error Invalid Program Code");
        }
    }

    private static void callHalt(Process p) {
        Scheduler.getInstance().killProcess(p);
    }

    private static void callPrintFromTo(String s1, String s2,Process p) {
        checkPrint(p);
        int start = Integer.parseInt(p.getVariable(s1));
        int end = Integer.parseInt(p.getVariable(s2));
        for(int i=start;i<=end;i++){
            systemCalls.print(i+"");
        }
    }

    private static void checkPrint(Process p) {
        if(Mutex.screenOutputMutexProcessID !=p.getId()){
            //TODO check if we should block the process instead
            System.err.println("error Invalid Program Code");
        }
    }

    private static void callAssign(String[] arr, Process p) {
        if (arr[2].equalsIgnoreCase("input")) {
            System.out.println("parser| Please enter a value: ");
            Scanner sc = new Scanner(System.in);
            Object o;
            String s = sc.nextLine();
            try {
                o = Integer.parseInt(s);
            } catch (Exception ignored) {
                o = s;
            }
            p.setVariable(arr[1], o);
        } else if (arr[2].equalsIgnoreCase("readfile")) {
            try {
             p.setVariable(arr[1], systemCalls.readFile(p.getVariable(arr[3])));
            }catch (Exception ignored){}
        }else{
            p.setVariable(arr[1],Integer.parseInt(arr[2]));
        }
    }

    private static void callPrint(String[] arr, Process p) {
        checkPrint(p);
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
