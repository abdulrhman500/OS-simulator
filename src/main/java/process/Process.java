package process;

import Main.Constants;
import memory.Memory;
import parser.Parser;
import scheduler.Scheduler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static Main.Constants.*;

public class Process {
    int lowerBound;
//    int maxBound;
    // the offset of the last element of the process in the memory will be used check boundaries for updating pc and to free process from memory

    static Memory mem = Memory.getInstance();
    static Scheduler scheduler = Scheduler.getInstance();
//    private int totalNumberOfInstruction = Process.getTotalNumberOfInstruction(this.getCodeLocation());

    //PCB
    //Memory boundaries
    public Process() {
        this.lowerBound = mem.getLowerBound(PROCESS_COUNT);

        mem.storeWord(lowerBound + ID_OFFSET, "ID", Constants.PROCESS_COUNT++);
        mem.storeWord(lowerBound + STATE_OFFSET, "state", State.Ready);
        mem.storeWord(lowerBound + PC_OFFSET, "PC", (lowerBound+7));
        mem.storeWord(lowerBound + LOWER_BOUND_OFFSET, "lower-bound", lowerBound);


        this.loadInstructions(lowerBound + 7, 0);

    }

    private void loadInstructions(int startMemAddress, int line) {
        try {
            File myObj = new File("src/main/resources/programs/" + this.getCodeLocation());
            Scanner reader = new Scanner(myObj);

            //skipping already loaded instructions
            for (int i = 0; i < line; i++) {
                reader.nextLine();
            }

            for (int i = 0; i < Constants.NUMBER_OF_INSTRUCTIONS; i++) {
                mem.storeWord(startMemAddress + i, "I" + i, reader.nextLine());
                if (!reader.hasNextLine()) {
                    mem.storeWord(startMemAddress + i + 1, "Halt", "");
                    break;
                }
            }
            reader.close();
        } catch (Exception ignored) {
        }
    }

    public int getId() {
        return (int) mem.getValueAt(this.lowerBound);
    }

    private String getCodeLocation() {
        int id = this.getId();
        if (id == 1) {
            return Constants.PROGRAM_1_CODE;
        }
        if (id == 2) {
            return Constants.PROGRAM_2_CODE;
        }
        return Constants.PROGRAM_3_CODE;

    }

    public State getState() {
        return (State) mem.getValueAt(this.lowerBound + STATE_OFFSET);
    }

    public void setState(State state) {
        mem.storeWord(this.lowerBound + STATE_OFFSET, "state", state);
    }

//    public int getTotalNumberOfInstruction() {
//        return totalNumberOfInstruction;
//    }
//
//    public void setTotalNumberOfInstruction(int totalNumberOfInstruction) {
//        this.totalNumberOfInstruction = totalNumberOfInstruction;
//    }

    public int getPC() {
        return (Integer) mem.loadWord(lowerBound + PC_OFFSET).getValue();
    }

    public int incrementPC() {
        //TODO handle when the PC reaches the end of loaded instructions
        int newPC = (Integer) (getPC() + 1);
        mem.storeWord(lowerBound + PC_OFFSET, "PC", newPC);
        return newPC;
    }

    public static void createProccess(String path) {
        Process p = new Process();
        scheduler.addNewProcess(p);
    }

    public void execute(){
        int pc = this.getPC();
        Parser.parse((String) mem.getValueAt(pc));
        this.incrementPC();
    }

    public static int getTotalNumberOfInstruction(String path) {
        int count = 0;
        try {
            Scanner sc = new Scanner(new File(path));
            while (sc.hasNext()) {
                count++;
                sc.nextLine();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return count;
    }

    public void freeMemory() {
        int tmpBound = this.lowerBound;
        for (int i = 0; i < PROCESS_SPACE; i++)
            mem.freeWord(tmpBound + i);
    }
}
