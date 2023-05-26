package scheduler;

import cpu.CPU;
import memory.Memory;
import process.Process;
import process.ProcessInfo;
import process.State;

import javax.print.CancelablePrintJob;
import javax.print.DocFlavor;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

public class Scheduler {
    public static Queue<Process> blockedOnFile = new LinkedList<Process>();
    public static Queue<Process> blockedOnUserInput = new LinkedList<Process>();
    public static Queue<Process> getBlockedScreenOutput = new LinkedList<Process>();

    public static Queue<Process> readyQueue = new LinkedList<Process>();

//    public static Queue<Process> blockedQueue = new LinkedList<Process>();

    public static Queue<Process> finishedQueue = new LinkedList<Process>();

    public static Process runningProcess;

    static Hashtable<Integer, ProcessInfo> processInfoTable = new Hashtable<>();
    private static CPU cpu = CPU.getInstance();
    private static Scheduler instance = new Scheduler();

    private static int clock = 0;
//    static int currentProcessTimer = 0;

    public void addNewProcess(Process process) {
        process.setState(State.Ready);
        readyQueue.add(process);
//        processInfoTable.put(process.getId(), pInfo);
    }

    private Scheduler() {

    }
    public static Scheduler getInstance() {
        return instance;
    }



    public void processTimeUp(Process process) {
        //update process info
        process.setState(State.Blocked);

    }

    public void killProcess(Process process) {

        Memory.getInstance().freeWord(process.getId()-1);

    }

    public int updateClock() {
        return ++clock;
    }

    public static int getClock() {
        return clock;
    }

    public static void runNextProcess() {
        Process nextProcess = readyQueue.poll();
        nextProcess.setState(State.Running);
        cpu.setExecutingProcess(nextProcess);
        cpu.executeProcess();

    }

}
