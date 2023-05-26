package Main;

import scheduler.Scheduler;

import static Main.Constants.*;

public class App {

    public static void main(String[] args) {

        Scheduler.arrivals.add(new Arrival(0, PROGRAM_1_CODE));
        Scheduler.arrivals.add(new Arrival(1, PROGRAM_2_CODE));
        Scheduler.arrivals.add(new Arrival(4, PROGRAM_3_CODE));

    }
}
