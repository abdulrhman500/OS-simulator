package memory;

import Main.Constants;
import scheduler.Scheduler;

public class Memory {
    private final MemoryWord[] memory;

    private static final Memory instance = new Memory();

    private static int numberOfProcessesInMemory =0;

    private Memory() {
        memory = new MemoryWord[40];
    }

    public static Memory getInstance() {
        return instance;
    }

    public String getVariableByName(String wantedName,int start) {
        for (int i = start; i < start + 3; i++) {
            if (memory[i].getName().equals(wantedName))
                return memory[i].getValue()+"";
        }
        return null;
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

    public int getLowerBound(int id){
        /**
         * since only 3 programs at max will ever be i the memory -> the first 3 memory blocks
         * will be used to point to the lower bound of each process and if a process is moved to disk
         * it's lower bound pointer will be set to -1
         * if Finished then pointer will be null
         */

        if(37/ Constants.PROCESS_SPACE >= 3){
            if(numberOfProcessesInMemory == 0){
                memory[0] =new MemoryWord("P1 lower bound", 3);
                numberOfProcessesInMemory++;
                return 3;
            }
            if(numberOfProcessesInMemory == 1){
                memory[1] =new MemoryWord("P2 lower bound", 3+Constants.PROCESS_SPACE);
                numberOfProcessesInMemory++;
                return 3+Constants.PROCESS_SPACE;
            }
            if(numberOfProcessesInMemory == 2){
                memory[2] =new MemoryWord("P3 lower bound", 3+2*Constants.PROCESS_SPACE);
                numberOfProcessesInMemory++;
                return 3+2*Constants.PROCESS_SPACE;
            }
        }
        if(37/ Constants.PROCESS_SPACE >= 2){
            if(numberOfProcessesInMemory == 0){
                memory[id-1] =new MemoryWord("P"+id +" lower bound",3);
                numberOfProcessesInMemory++;
                return 3+Constants.PROCESS_SPACE;
            }
            if(numberOfProcessesInMemory == 1){
                memory[id-1] =new MemoryWord("P"+id +" lower bound",3+Constants.PROCESS_SPACE);
                numberOfProcessesInMemory++;
                return 3+Constants.PROCESS_SPACE;
            }else{
                //TODO store a process to disk and set it's pointer to -1
                memory[id-1] =new MemoryWord("P"+id +" lower bound",3+Constants.PROCESS_SPACE);
                return 3+Constants.PROCESS_SPACE;
            }
        }else{
            //The memory can take only one process
            if(numberOfProcessesInMemory == 0){
                memory[id-1] =new MemoryWord("P"+id +" lower bound",3);
                numberOfProcessesInMemory++;
                return 3+Constants.PROCESS_SPACE;
            }else{
                //TODO store the process to disk and set it's pointer to -1;
                memory[id-1] =new MemoryWord("P"+id +" lower bound",3+Constants.PROCESS_SPACE);
                return 3+Constants.PROCESS_SPACE;
            }
        }
    }




    public MemoryWord[] getMemory() {
        return this.memory;
    }

    @Override
    public String toString() {
        StringBuilder sb =new StringBuilder();
        sb.append("=======> Memory: <=======\n");
        for(int i=0;i<40;i++){
            if(memory[i] != null) {
                sb.append(i).append(" ").append(memory[i].toString()).append("\n");
            }
        }
        sb.append("------------------");
        return sb.toString();
    }
}