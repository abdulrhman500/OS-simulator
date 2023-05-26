package scheduler;

import Main.Arrival;
import cpu.CPU;
import process.Process;
import process.ProcessInfo;
import process.State;

import javax.print.CancelablePrintJob;
import javax.print.DocFlavor;
import java.util.ArrayList;
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
    private static CPU cpu = CPU.getInstance();

    private static int clock = 0;
//    static int currentProcessTimer = 0;

    public static ArrayList<Arrival> arrivals = new ArrayList<Arrival>();
    public void addNewProcess(Process process) {
        ProcessInfo pInfo = new ProcessInfo(getClock(), process.getTotalNumberOfInstruction(), 0);
        process.setState(State.Ready);
        readyQueue.add(process);
        processInfoTable.put(process.getId(), pInfo);
    }

    public Scheduler() {

    }


    public void processTimeUp(Process process) {
        //update process info
        process.setState(State.Blocked);

    }

    public void killProcess(Process process) {

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
