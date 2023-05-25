package memory;

import Main.Constants;
import exceptions.IllegalMemoryAccessException;

import java.util.Arrays;

public class Memory {
    private MemoryWord[] memory;

    private static Memory instance = new Memory();

    private Memory() {
        memory = new MemoryWord[40];
    }

    public static Memory getInstance() {
        return instance;
    }

    public int searchForWord(String wantedName) {
        for (int i = 0; i < memory.length; i++) {
            if (memory[i].getName().equals(wantedName))
                return i;
        }
        return -1;
    }

    public Object getValueAt(int address){
        return memory[address].getValue();
    }

    public MemoryWord loadWord(int address) {
        return memory[address];
    }

    public void storeWord(int address, String newName, Object newValue) {
        memory[address] = new MemoryWord(newName, newValue);
    }

    public void freeWord(int address) {
        memory[address] = null;
    }

    public int getLowerBound(){
        int lowerBound = hasContiguousBlocks();
        if (lowerBound != -1) {
            //TODO save a process to disk and call this method again
        }
        return lowerBound;
    }

    private int hasContiguousBlocks() {
        for (int i = 0; i < memory.length; i++) {
            if (memory[i] == null || memory[i].getValue() == null) {
                boolean contiguousBlocks = true;
                for (int j = i + 1; j < i + Constants.PROCESS_SPACE && j < memory.length; j++) {
                    if (memory[j].getValue() != null) {
                        contiguousBlocks = false;
                        break;
                    }
                }
                if (contiguousBlocks) {
                    return i;
                }
            }
        }
        return -1;
    }

    public MemoryWord[] getMemory() {
        return this.memory;
    }

    @Override
    public String toString() {
        StringBuilder sb =new StringBuilder();
        for(int i=0;i<40;i++){
            sb.append(i).append(" ").append(memory[i].toString()).append("\n");
        }
        return sb.toString();
    }
}