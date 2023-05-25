package scheduler;

import process.Process;
import process.ProcessInfo;
import process.State;

import javax.print.DocFlavor;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

public class Scheduler {
    public static Queue<Process> blockedOnFile = new LinkedList<Process>();
    public static Queue<Process> blockedOnUserInput = new LinkedList<Process>();
    public static Queue<Process> getBlockedScreenOutput = new LinkedList<Process>();

    public static Queue<Process> readyQueue = new LinkedList<Process>();

    public static Queue<Process> blockedQueue = new LinkedList<Process>();

    public static Queue<Process> finishedQueue = new LinkedList<Process>();

    public static Process runningProcess;

    static Hashtable<Integer, ProcessInfo> processInfoTable = new Hashtable<>();

    static int clock = 0;


    public void addNewProcess(Process process) {
        ProcessInfo pInfo = new ProcessInfo(clock, process.getTotalNumberOfInstruction(), 0);
        process.setState(State.Ready);
        readyQueue.add(process);
        processInfoTable.put(process.getId(), pInfo);
    }

    public Scheduler() {

    }


}
