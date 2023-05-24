import exceptions.IllegalMemoryAccessException;

public class Memory {
    private int[] memory;
    private boolean[] allocationTable;

    private static Memory instance = new Memory();
    private static int PROCESS_SPACE = 3;

    private Memory() {
        memory = new int[40];
        allocationTable = new boolean[40];
    }

    public static Memory getInstance() {
        return instance;
    }

    // The memory is large enough to hold the un-parsed lines of code, variables and PCB for any of the processes.
    // each word can store 1 variable and its corresponding data.
    // Processes should not access any data outside their allocated memory block.
    // A process should only be created at its arrival time. A process is considered
    // created when its program file is read into lines and it gets assigned a part of the
    // memory for instructions, variables and its PCB. Assume that each process needs enough space for 3 variables.

    // Feel free to separate the lines of code, variables and PCB within the memory
    // if needed as long as they fall within the same data structure meant to represent the memory.

    // Your code needs to be able to handle the case where the memory is not large
    // enough to run all the processes. When a new process is created, the system checks
    // if there is enough space, if not the system will unload one of the existing processes
    // and store its data on the disk. Feel free to assume the format for the memory
    // when stored on the disk. However while unloaded, the process remains in the
    // scheduler, thus when it is time for the unloaded process to run again, the process
    // memory is swapped back into the memory from the disk. As a result, you need to
    // program a way to read and write an existing process’s data to/from the disk as
    // well as manage the swapping between the processes while protecting each process’s memory.

    // Process Control Block
    // A process control block is a data structure used by computer operating systems to
    // store all the information about a process.In order to schedule your processes, you
    // will need to keep a PCB for every process. The PCB should contain the following
    // information:
    // 1. Process ID (The process is given an ID when is being created)
    // 2. Process State
    // 3. Program Counter
    // 4. Memory Boundaries


    public int loadWord(int address) throws IllegalMemoryAccessException {
        return memory[address];
    }

    public void storeWord(int address, int word) throws IllegalMemoryAccessException {
        memory[address] = word;
    }

    // public void createProcess(Process process) {
    //     int storeAddress = hasContiguousBlocksOfSize(process.size);
    //     if (storeAddress == -1) {
    //         unloadProcess();
    //     }
    // }

    public int hasContiguousBlocksOfSize(int requiredWordsCount) {
        for (int i = 0; i < allocationTable.length; i++) {
            if (!allocationTable[i]) {
                boolean contiguousBlocks = true;
                for (int j = i + 1; j < i + requiredWordsCount && j < allocationTable.length; j++) {
                    if (allocationTable[j]) {
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

    public int[] getMemory() {
        return this.memory;
    }

    // public static void main(String [] args) {
    //     Memory m = new Memory();
    //     for (int i = 0; i < m.memory.length; i++) {
    //         m.memory[i] = i;
    //         System.out.println(m.memory[i]);
    //     }
    // }
}
