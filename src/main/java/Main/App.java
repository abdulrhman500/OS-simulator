package Main;

import scheduler.Scheduler;
import utils.programLoader;

import static Main.Constants.*;

public class App {

    public static void main(String[] args) {
        programLoader pl = new programLoader();
        while (true) {
            if (Scheduler.finishedQueue.size() == 3)
                break;

            if (Scheduler.getClock() == 0) {
                pl.load(PROGRAM_1_CODE);
            }

            if (Scheduler.getClock() == 1) {
                pl.load(PROGRAM_2_CODE);
            }

            if (Scheduler.getClock() == 4) {
                pl.load(PROGRAM_3_CODE);
            }

        }

    }
}
