package cpu;

import process.Process;
import scheduler.Scheduler;

import static Main.Constants.NUMBER_OF_INSTRUCTIONS_PER_TIME_SLICE;
import static Main.Constants.TIME_SLICE;

public class CPU {

    private static CPU instance;
    private Process process;

    private Scheduler scheduler;


    private int remainingInstruction = NUMBER_OF_INSTRUCTIONS_PER_TIME_SLICE * TIME_SLICE;

    public static CPU getInstance() {
        if (instance == null)
            return new CPU();
        return instance;
    }

    private CPU() {

    }

    public void setExecutingProcess(Process executingProcess) {
        this.process = executingProcess;
        remainingInstruction = NUMBER_OF_INSTRUCTIONS_PER_TIME_SLICE * TIME_SLICE;
    }

    public void executeProcess() {
        while (remainingInstruction != 0) {
            int currentPC = process.getPC();
            //execute Instruction ?
//after each execution of an instruction clock at scheduler should be updated;
         scheduler.updateClock();
            // if Instruction is halt then call and break;
            scheduler.killProcess(process);

            //after each instruction the RemainingInstruction is decreased br 1;
            decreaseRemainingInstruction();

        }
        //  we also need to check if remaining instruction is halt;
        boolean isHalt =false;
        if (isHalt) {
            scheduler.killProcess(process);
        return;
        }
        if (remainingInstruction == 0) {
            scheduler.processTimeUp(process);
            return;
        }
    }

    public int getRemainingInstruction() {
        return remainingInstruction;
    }

    private void decreaseRemainingInstruction() {
        this.remainingInstruction--;
    }
}
