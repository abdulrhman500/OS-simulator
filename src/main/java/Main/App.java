package Main;

import process.Process;
import scheduler.Scheduler;

import static Main.Constants.*;

public class App {

    public static void main(String[] args) {
        while (Scheduler.finishedQueue.size() != 3) {
            if (Scheduler.getClock() == 0) {
                Process.createProccess(PROGRAM_1_CODE);
            }
            if (Scheduler.getClock() == 1) {
                Process.createProccess(PROGRAM_2_CODE);
            }
            if (Scheduler.getClock() == 4) {
                Process.createProccess(PROGRAM_3_CODE);
            }
        }
    }
}
