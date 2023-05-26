package scheduler;

import Main.Arrival;
import Main.Constants;
import exceptions.InvalidResourceException;
import mutex.Mutexes;
import process.Process;
import process.ProcessInfo;
import process.State;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

import static Main.Constants.NUMBER_OF_INSTRUCTIONS_PER_TIME_SLICE;

public class Scheduler {
    public static Queue<Process> blockedOnFile = new LinkedList<Process>();
    public static Queue<Process> blockedOnUserInput = new LinkedList<Process>();
    public static Queue<Process> blockedOnScreenOutput = new LinkedList<Process>();

    public static Queue<Process> readyQueue = new LinkedList<Process>();

    public static Queue<Process> finishedQueue = new LinkedList<Process>();

    public static Process runningProcess;

    static Hashtable<Integer, ProcessInfo> processInfoTable = new Hashtable<>();
    private static final Scheduler instance = new Scheduler();

    private static int remainingInstruction = NUMBER_OF_INSTRUCTIONS_PER_TIME_SLICE;


    private static int clock = 0;

    private static final ArrayList<Arrival> arrivals = new ArrayList<Arrival>();

    static int maxArrivalTime = Integer.MIN_VALUE;
    static boolean isDeadLock = false;

    public void addNewProcess(Process process) {
        process.setState(State.Ready);
        readyQueue.add(process);
        System.out.println("Scheduler| added New Process with ID : " + process.getId());
        System.out.println("Scheduler| Ready Queue Updated: " + readyQueue.toString());
    }

    private Scheduler() {
    }

    public static Scheduler getInstance() {
        return instance;
    }

    public void addProgram(int time, String path) {
        if (time > maxArrivalTime)
            maxArrivalTime = time;

        arrivals.add(new Arrival(time, path));
    }


    public void processTimeUp(Process process) {
        System.out.println("Scheduler| time up for Process: " + process.getId());
        System.out.println("Scheduler| Preempted Process: " + process.getId());
        process.setState(State.Ready);
        readyQueue.add(process);

        System.out.println("Scheduler| Ready Queue Updated: " + readyQueue.toString());
        runningProcess =null;
        remainingInstruction = NUMBER_OF_INSTRUCTIONS_PER_TIME_SLICE;
    }

    public void killProcess(Process process) {

        process.freeMemory();
        process.setState(State.Finished);
        finishedQueue.add(process);
        runNextProcess();
    }

    public int updateClock() {
        for (Arrival tmp : arrivals) {
            if (tmp.getArrivedAt() == clock) {
                Process.createProccess(tmp.getProgramPath());
            }
        }

        if (runningProcess == null) {
            runNextProcess();

        }else{
            System.out.println("Scheduler| Running  Process: " + runningProcess.getId());
            runningProcess.execute();
            remainingInstruction--;
            if (remainingInstruction == 0) {
                processTimeUp(runningProcess);
            }
        }
        clock++;
        return clock;
    }

    public static int getClock() {
        return clock;
    }

    public static void runNextProcess() {
        runningProcess = readyQueue.poll();


        if (runningProcess == null) {
            if (arrivals.size() == finishedQueue.size()) {
                System.out.println("ALL Processes are Done");
            } else {
                if (getClock() > getInstance().maxArrivalTime)
                    isDeadLock = true;
            }
        } else {
            System.out.println("Scheduler| Running  Process: " + runningProcess.getId());
            runningProcess.setState(State.Running);
            remainingInstruction = NUMBER_OF_INSTRUCTIONS_PER_TIME_SLICE;
            runningProcess.execute();
            remainingInstruction--;
        }
    }

    public void simulate() {
        while (finishedQueue.size() != 3) {
            if(isDeadLock)
                break;
            System.out.println("Scheduler| Clock Cycle: " + getClock()+ " started");
            updateClock();
        }
        if (isDeadLock)
            System.out.println("No More Ready Process Exists. DeadLock Happened");
        else
            System.out.println("Finished All Programs");
    }

    public void blockProcess(Process process, Mutexes resourceBlockedOn) {
        System.out.println("Scheduler| Blocked Process: " + process.getId()+ " on resource: " + resourceBlockedOn);
        switch (resourceBlockedOn){
            case FILE -> blockedOnFile.add(process);
            case USERINPUT -> blockedOnUserInput.add(process);
            case SCREENOUTPUT -> blockedOnScreenOutput.add(process);
        }
        runningProcess =null;
    }
}
