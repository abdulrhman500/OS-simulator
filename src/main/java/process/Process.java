package process;

import Main.Constants;
import exceptions.InvalidResourceException;
import memory.Memory;
import parser.Parser;
import scheduler.Scheduler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

import static Main.Constants.*;

public class Process {
    int lowerBound;
    int numberOfOffsets;

    public void setLowerBound(int lowerBound) {
        this.lowerBound = lowerBound;
    }
public int ID ;
    int numberOfVariables;

    static Memory mem = Memory.getInstance();
    static Scheduler scheduler = Scheduler.getInstance();

    public Process() {
        this.numberOfOffsets=0;
        this.numberOfVariables=0;
        this.lowerBound = mem.getLowerBound(PROCESS_COUNT);
        this.ID = PROCESS_COUNT;
        mem.storeWord(lowerBound + ID_OFFSET, "ID", Constants.PROCESS_COUNT++);
        mem.storeWord(lowerBound + STATE_OFFSET, "state", State.Ready);
        mem.storeWord(lowerBound + PC_OFFSET, "PC", (lowerBound+7));
        mem.storeWord(lowerBound + LOWER_BOUND_OFFSET, "lower-bound", lowerBound);


        this.loadInstructions(lowerBound + 7, 0);
    }

    private void loadInstructions(int startMemAddress, int lineOffset) {
        try {
            File myObj = new File(this.getCodeLocation());
            Scanner reader = new Scanner(myObj);

            //skipping already loaded instructions
            for (int i = 0; i < lineOffset; i++) {
                reader.nextLine();
            }

            for (int i = 0; i < Constants.NUMBER_OF_INSTRUCTIONS; i++) {
                if (!reader.hasNextLine()) {
                    mem.storeWord(startMemAddress + i, "I"+i, "Halt");
                    break;
                }
                mem.storeWord(startMemAddress + i, "I" + i, reader.nextLine());
            }
            reader.close();
        } catch (Exception ignored) {
            System.out.println("Problem reading file");
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
        System.out.println("Process | State changed to "+ state +" for Process: " + this.getId());
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
        int newPC = (Integer) (getPC() + 1);
        if(newPC == NUMBER_OF_INSTRUCTIONS+ lowerBound + 7){
            this.numberOfOffsets++;
            newPC = lowerBound+7;
            this.loadInstructions(lowerBound + 7, NUMBER_OF_INSTRUCTIONS*this.numberOfOffsets);

        }
        mem.storeWord(lowerBound + PC_OFFSET, "PC", newPC);
        return newPC;
    }

    public static void createProccess(String path) {
        Process p = new Process();
        scheduler.addNewProcess(p);
    }

    public int execute(){
        int pc = this.getPC();
        boolean isHalt = Objects.equals((String) mem.getValueAt(pc), "Halt");
        Parser.parse((String) mem.getValueAt(pc),this);
        if(!isHalt){
            this.incrementPC();
            return 0;
        }
        return 1;
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
        for (int i = 0; i < PROCESS_SPACE; i++)
            mem.freeWord(this.lowerBound + i);
    }

    public String getVariable(String s) {
        return mem.getVariableByName(s,this.lowerBound+4);
    }

    @Override
    public String toString() {
        return this.getId()+"";
    }

    public void setVariable(String s, Object o) {
        mem.storeWord(this.lowerBound+4+this.numberOfVariables,s,o);
        this.numberOfVariables++;

    }
    public int getLowerBound() {
        return lowerBound;
    }
}
