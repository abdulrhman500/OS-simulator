package scheduler;

import Main.Arrival;
import Main.Constants;
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
    public static Queue<Process> getBlockedScreenOutput = new LinkedList<Process>();

    public static Queue<Process> readyQueue = new LinkedList<Process>();

    public static Queue<Process> finishedQueue = new LinkedList<Process>();

    public static Process runningProcess;

    static Hashtable<Integer, ProcessInfo> processInfoTable = new Hashtable<>();
    private static Scheduler instance = new Scheduler();

    private static int remainingInstruction = NUMBER_OF_INSTRUCTIONS_PER_TIME_SLICE;


    private static int clock = 0;
//    static int currentProcessTimer = 0;

    private static ArrayList<Arrival> arrivals = new ArrayList<Arrival>();

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

    public void addProgram(int time,String path){
        arrivals.add(new Arrival(time,path));
    }


    public void processTimeUp(Process process) {
        process.setState(State.Ready);
        readyQueue.add(process);
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
            if (tmp.getArrivedAt() == clock){
                Process.createProccess(tmp.getProgramPath());
            }
        }

        if(runningProcess == null){
            runNextProcess();
        }else{
            runningProcess.execute();
            remainingInstruction--;
            if(remainingInstruction==0){
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
            }else {
                // How to deal with blocked
                //DEADLOCK
                //TODO handle the case when it's not a deadlock only a late process arrival
                System.out.println("No READY process exists , DeadLock happened");
            }
        } else {
            runningProcess.setState(State.Running);
            remainingInstruction = NUMBER_OF_INSTRUCTIONS_PER_TIME_SLICE;
            runningProcess.execute();
            remainingInstruction--;
        }
    }

    public void simulate() {
        while (finishedQueue.size() != 3) {
            updateClock();
        }
        System.out.println("Finished All Programs");
    }
}
