package mutex;

import exceptions.InvalidResourceException;
import process.Process;
import process.State;
import scheduler.Scheduler;

public class Mutex {
    static boolean fileMutexLocked = false;
    static boolean userInputMutexLocked = false;
    static boolean screenOutputMutexLocked = false;

    public static Integer fileMutexOwnerProcessID;
    public static Integer userInputMutexOwnerProcessID;
    public static Integer screenOutputMutexProcessID;

    private static final Scheduler scheduler = Scheduler.getInstance();


    public static boolean semWait(Process process, Mutexes resourceRequired) throws InvalidResourceException {
        boolean gotLockSuccessfully = true;
        switch (resourceRequired) {
            case FILE -> {
                {
                    if (!fileMutexLocked) {
                        //The File Resource is Available
                        fileMutexLocked = true;
                        fileMutexOwnerProcessID = process.getId();
                    } else {
                        System.out.println("Mutex| resource unavailable Blocking Process "+ process.getId());
                        scheduler.blockProcess(process, resourceRequired);
                        process.setState(State.Blocked);
                        gotLockSuccessfully = false;
                    }
                }
            }
            case USERINPUT -> {
                {
                    if (!userInputMutexLocked) {
                        //The UserInput Resource is Available.
                        userInputMutexLocked = true;
                        userInputMutexOwnerProcessID = process.getId();
                    } else {
                        System.out.println("Mutex| resource unavailable Blocking Process "+ process.getId());
                        scheduler.blockProcess(process, resourceRequired);
                        process.setState(State.Blocked);
                        gotLockSuccessfully = false;
                    }
                }
            }
            case SCREENOUTPUT -> {
                if (!screenOutputMutexLocked) {
                    screenOutputMutexLocked = true;
                    screenOutputMutexProcessID = process.getId();
                } else {
                    System.out.println("Mutex| resource unavailable Blocking Process "+ process.getId());
                    scheduler.blockProcess(process, resourceRequired);
                    process.setState(State.Blocked);
                    gotLockSuccessfully = false;
                }
            }
            default -> throw new InvalidResourceException();
        }
        printMutexTable();
        return gotLockSuccessfully;
    }

    public static void semSignal(Process process, Mutexes resourceReleased) throws InvalidResourceException {

        switch (resourceReleased) {
            case FILE -> {
                {
                    if (!fileMutexLocked || process.getId() != fileMutexOwnerProcessID) return;

                    System.out.println("Mutex| resource File release By Process "+ process.getId());
                    if (Scheduler.blockedOnFile.isEmpty()) {
                        fileMutexOwnerProcessID = null;
                        fileMutexLocked = false;
                    } else {
                        Process nextProcess = Scheduler.blockedOnFile.poll();
                        fileMutexOwnerProcessID = nextProcess.getId();
                        Scheduler.readyQueue.add(nextProcess);
                    }
                }
            }
            case USERINPUT -> {
                {
                    if (!userInputMutexLocked || process.getId() != userInputMutexOwnerProcessID) return;

                    System.out.println("Mutex| resource User Input release By Process "+ process.getId());
                    if (Scheduler.blockedOnUserInput.isEmpty()) {
                        userInputMutexOwnerProcessID = null;
                        userInputMutexLocked = false;
                    } else {
                        Process nextProcess = Scheduler.blockedOnUserInput.poll();
                        userInputMutexOwnerProcessID = nextProcess.getId();
                        Scheduler.readyQueue.add(nextProcess);
                    }
                }
            }
            case SCREENOUTPUT -> {
                if (!screenOutputMutexLocked || process.getId() != screenOutputMutexProcessID) return;

                System.out.println("Mutex| resource Screen Output release By Process "+ process.getId());
                if (Scheduler.blockedOnUserInput.isEmpty()) {
                    screenOutputMutexProcessID = null;
                    screenOutputMutexLocked = false;
                } else {
                    Process nextProcess = Scheduler.blockedOnScreenOutput.poll();
                    screenOutputMutexProcessID = nextProcess.getId();
                    Scheduler.readyQueue.add(nextProcess);
                }
            }
            default -> throw new InvalidResourceException();
        }
        printMutexTable();
    }

    public static void printMutexTable(){
        System.out.println("Mutex Table | \n"+"\t\tfile Mutex Owner: "+fileMutexOwnerProcessID+"\n"+
                "\t\tuserInput Mutex Owner: "+userInputMutexOwnerProcessID+"\n"+
                "\t\tscreenOutput Mutex Owner: "+screenOutputMutexProcessID+"\n");
    }

}
