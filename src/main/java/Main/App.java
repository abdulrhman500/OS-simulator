package Main;

import scheduler.Scheduler;

import static Main.Constants.*;

public class App {
    static Scheduler scheduler = Scheduler.getInstance();

    public static void main(String[] args) {

        scheduler.addProgram(0, PROGRAM_1_CODE);
        scheduler.addProgram(1, PROGRAM_2_CODE);
        scheduler.addProgram(4, PROGRAM_3_CODE);

        scheduler.simulate();


    }
}
