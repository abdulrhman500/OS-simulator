package mutex;

import exceptions.InvalidResourceException;
import process.Process;
import scheduler.Scheduler;

public class Mutex {
    static boolean fileMutexLocked = false;
    static boolean userInputMutexLocked = false;
    static boolean screenOutputMutexLocked = false;

    static Integer fileMutexOwnerProcessID;
    static Integer userInputMutexOwnerProcessID;
    static Integer screenOutputMutexProcessID;


    public static void semWait(Process process, Mutexes resourceRequired) throws InvalidResourceException {
        switch (resourceRequired){
            case FILE :{
                if(!fileMutexLocked){
                    //The File Resource is Available
                    fileMutexLocked = true;
                    fileMutexOwnerProcessID = process.getId();
                }
                else{
                    //The File Resource is not Available
                    Scheduler.blockedOnFile.add(process);
                }
            };break;
            case USERINPUT:{
                if(!userInputMutexLocked){
                    //The UserInput Resource is Available.
                    userInputMutexLocked = true;
                    userInputMutexOwnerProcessID = process.getId();
                }
                else{
                    //The UserInput Resource is not Available.
                    Scheduler.blockedOnUserInput.add(process);
                }
            };break;
            case SCREENOUTPUT:{
                if(!screenOutputMutexLocked){
                    //The ScreenInput Resource is Available.
                    screenOutputMutexLocked = true;
                    screenOutputMutexProcessID = process.getId();
                }
                else{
                    //The ScreenOutput Resource is not Available.
                    Scheduler.getBlockedScreenOutput.add(process);
                }
            }break;
            default:throw new InvalidResourceException();
        }


    }

    public static void semSignal(Process process, Mutexes resourceReleased) throws InvalidResourceException {

        switch (resourceReleased){
            case FILE :{
                if(!fileMutexLocked || process.getId() != fileMutexOwnerProcessID)return;

                if(Scheduler.blockedOnFile.isEmpty()){
                    fileMutexOwnerProcessID = null;
                    fileMutexLocked = false;
                }else{
                    Process nextProcess = Scheduler.blockedOnFile.poll();
                    fileMutexOwnerProcessID = nextProcess.getId();
                    Scheduler.readyQueue.add(nextProcess);
                }
            };break;
            case USERINPUT:{
                if(!userInputMutexLocked || process.getId() != userInputMutexOwnerProcessID)return;

                if(Scheduler.blockedOnUserInput.isEmpty()){
                    userInputMutexOwnerProcessID = null;
                    userInputMutexLocked = false;
                }else{
                    Process nextProcess = Scheduler.blockedOnUserInput.poll();
                    userInputMutexOwnerProcessID = nextProcess.getId();
                    Scheduler.readyQueue.add(nextProcess);
                }
            };break;
            case SCREENOUTPUT:{
                if(!screenOutputMutexLocked || process.getId() != screenOutputMutexProcessID)return;

                if(Scheduler.blockedOnUserInput.isEmpty()){
                    screenOutputMutexProcessID = null;
                    screenOutputMutexLocked = false;
                }else{
                    Process nextProcess = Scheduler.getBlockedScreenOutput.poll();
                    screenOutputMutexProcessID = nextProcess.getId();
                    Scheduler.readyQueue.add(nextProcess);
                }
            }break;
            default:throw new InvalidResourceException();
        }
    }


}
