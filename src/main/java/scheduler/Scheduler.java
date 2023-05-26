package scheduler;

import Main.Arrival;
import Main.Constants;
import cpu.CPU;
import memory.Memory;
import process.Process;
import process.ProcessInfo;
import process.State;
import utils.DiskIO;
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

    public static Queue<Process> finishedQueue = new LinkedList<Process>();

    public static Process runningProcess;

    static Hashtable<Integer, ProcessInfo> processInfoTable = new Hashtable<>();
    private static CPU cpu = CPU.getInstance();
    private static Scheduler instance = new Scheduler();

    private static int clock = 0;
//    static int currentProcessTimer = 0;

    public static ArrayList<Arrival> arrivals = new ArrayList<Arrival>();

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

        ProcessInfo curr = processInfoTable.get(process.getId());
        curr.setExecutedInstructions(curr.getExecutedInstructions() - Constants.NUMBER_OF_INSTRUCTIONS_PER_TIME_SLICE);
        processInfoTable.put(process.getId(), curr);
        process.setState(State.Ready);
        readyQueue.add(process);
        runNextProcess();

    }

    public void killProcess(Process process) {

        process.freeMemory();
        process.setState(State.Finished);
        finishedQueue.add(process);
        runNextProcess();
    }

    public int updateClock() {

        ++clock;
        for (Arrival tmp : arrivals) {
            if (tmp.getArrivedAt() == clock) ;
//                loadProgram(tmp.getProgramPath());
        }
        return clock;
    }

    public static int getClock() {
        return clock;
    }

    public static void runNextProcess() {
        runningProcess = readyQueue.poll();

        if (runningProcess == null) {
            if (arrivals.size() == finishedQueue.size())
                System.out.println("ALL Processes are Done");
            else {
                // How to deal with blocked
                //DEADLOCK
                System.out.println("No READY process exists , DeadLock happened");
            }

        } else {
            runningProcess.setState(State.Running);


            cpu.setExecutingProcess(runningProcess);

            cpu.executeProcess();

        }
    }

}
