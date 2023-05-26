package process;

import Main.Constants;
import memory.Memory;

import java.io.File;
import java.util.Scanner;

import static Main.Constants.*;

public class Process {
    int lowerBound;
    int maxBound;
    // the offset of the last element of the process in the memory will be used check boundaries for updating pc and to free process from memory

    Memory mem = Memory.getInstance();
    private int totalNumberOfInstruction = utils.programInfo.getTotalNumberOfInstruction(this.getCodeLocation());
    State state;

    //PCB
    //Memory boundaries
    public Process() {
        mem.getLowerBound();
        this.lowerBound = mem.getLowerBound();

        mem.storeWord(lowerBound + ID_OFFSET, "ID", Constants.PROCESS_COUNT++);
        mem.storeWord(lowerBound + STATE_OFFSET, "state", State.Ready);
        mem.storeWord(lowerBound + PC_OFFSET, "PC", 0);
        mem.storeWord(lowerBound + LOWER_BOUND_OFFSET, "lower-bound", lowerBound);


        this.loadInstructions(lowerBound + 7, 0);

        this.state = State.Ready;
    }

    private void loadInstructions(int startMemAddress, int line) {
        try {
            File myObj = new File("src/main/resources/programs/" + this.getCodeLocation());
            Scanner reader = new Scanner(myObj);

            //skipping already loaded instructions
            for (int i = 0; i < line; i++) {
                reader.nextLine();
            }
//what if there is no enough space for the  NUMBER_OF_INSTRUCTIONS ?
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
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getTotalNumberOfInstruction() {
        return totalNumberOfInstruction;
    }

    public void setTotalNumberOfInstruction(int totalNumberOfInstruction) {
        this.totalNumberOfInstruction = totalNumberOfInstruction;
    }

    public int getPC() {

        return (Integer) mem.loadWord(lowerBound + PC_OFFSET).getValue();
    }

    public int updatePC() {
// what if pc +1 is out of the allocated memory of the process;

        int newPC = (Integer) (getPC() + 1);
        mem.storeWord(lowerBound + PC_OFFSET, "PC", newPC);
        return newPC;
    }
}
