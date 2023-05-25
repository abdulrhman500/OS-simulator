import exceptions.IllegalMemoryAccessException;

public class Memory {
    private MemoryWord[] memory;

    private static Memory instance = new Memory();
    private static int PROCESS_SPACE = 3;

    private Memory() {
        memory = new MemoryWord[40];
    }

    public static Memory getInstance() {
        return instance;
    }

    public int searchForWord(String wantedName) {
        for (int i = 0; i < memory.length; i++) {
            if (memory[i].name.equals(wantedName))
                return i;
        }
        return -1;
    }

    public Object getValueAt(int address) throws IllegalMemoryAccessException {
        return memory[address].value;
    }

    public MemoryWord loadWord(int address) {
        return memory[address];
    }

    public void storeWord(int address, String newName, Object newValue) throws IllegalMemoryAccessException {
        memory[address].name = newName;
        memory[address].value = newValue;
    }

    public void freeWord(int address) {
        memory[address] = null;
    }

    public int hasContiguousBlocksOfSize(int requiredWordsCount) {
        for (int i = 0; i < memory.length; i++) {
            if (memory[i] == null || memory[i].value == null) {
                boolean contiguousBlocks = true;
                for (int j = i + 1; j < i + requiredWordsCount && j < memory.length; j++) {
                    if (memory[j].value != null) {
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
}