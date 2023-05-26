package memory;

import Main.Constants;
import scheduler.Scheduler;
import utils.DiskIO;

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

//    private int getLowerBound0(int id){
//        /**
//         * since only 3 programs at max will ever be i the memory -> the first 3 memory blocks
//         * will be used to point to the lower bound of each process and if a process is moved to disk
//         * it's lower bound pointer will be set to -1
//         * if Finished then pointer will be null
//         */
//
//        if(37/ Constants.PROCESS_SPACE >= 3){
//            if(numberOfProcessesInMemory == 0){
//                memory[0] =new MemoryWord("P1 lower bound", 3);
//                numberOfProcessesInMemory++;
//                return 3;
//            }
//            if(numberOfProcessesInMemory == 1){
//                memory[1] =new MemoryWord("P2 lower bound", 3+Constants.PROCESS_SPACE);
//                numberOfProcessesInMemory++;
//                return 3+Constants.PROCESS_SPACE;
//            }
//            if(numberOfProcessesInMemory == 2){
//                memory[2] =new MemoryWord("P3 lower bound", 3+2*Constants.PROCESS_SPACE);
//                numberOfProcessesInMemory++;
//                return 3+2*Constants.PROCESS_SPACE;
//            }
//        }
//       else if(37/ Constants.PROCESS_SPACE >= 2){
//            if(numberOfProcessesInMemory == 0){
//                memory[id-1] =new MemoryWord("P"+id +" lower bound",3);
//                numberOfProcessesInMemory++;
//                return 3;
//            }
//            if(numberOfProcessesInMemory == 1){
//                memory[id-1] =new MemoryWord("P"+id +" lower bound",3+Constants.PROCESS_SPACE);
//                numberOfProcessesInMemory++;
//                return 3+Constants.PROCESS_SPACE;
//            }else{
//                memory[id-1] =new MemoryWord("P"+id +" lower bound",3+Constants.PROCESS_SPACE);
//                return 3+Constants.PROCESS_SPACE;
//            }
//        }else{
//            //The memory can take only one process
//            if(numberOfProcessesInMemory == 0){
//                memory[id-1] =new MemoryWord("P"+id +" lower bound",3);
//                numberOfProcessesInMemory++;
//                return 3+Constants.PROCESS_SPACE;
//            }else{
//                memory[id-1] =new MemoryWord("P"+id +" lower bound",3+Constants.PROCESS_SPACE);
//                return 3+Constants.PROCESS_SPACE;
//            }
//        }
//    return ;
//    }



    public int getLowerBound(int processId) {
        Integer old = null;
        if(memory[processId - 1] != null )
         old =  (Integer) memory[processId - 1].getValue();
        if (old != null && old != -1) return old;

        int bound = -1;
        int total_used_memory = Constants.PROCESS_SPACE * numberOfProcessesInMemory;
        if (37 - total_used_memory >= Constants.PROCESS_SPACE) {
            bound = 3 + numberOfProcessesInMemory * Constants.PROCESS_SPACE;
            memory[processId - 1] = new MemoryWord("P" + processId + " lower bound", bound);
            numberOfProcessesInMemory++;
            return bound;
        } else {
            int lowerBoundOfRunning = Scheduler.runningProcess.getLowerBound();
            int chosenProcessID = -1;
            for (int i = 0; i < 3; i++) {
                //choose any process but not the running one
                if (memory[i] !=null && lowerBoundOfRunning != (Integer) memory[i].getValue())
                {
                    chosenProcessID = i ;
                    break;
                }
            }
            if(chosenProcessID == -1) {
                chosenProcessID = 0;
                memory[chosenProcessID] = new MemoryWord("P" + (chosenProcessID + 1), 3);
            }

            //will return the lower bound of the chosen proccses
            bound = (Integer) memory[chosenProcessID].getValue();
            //make the chosen process to point to -1
            System.out.println("Memory | Process with ID = " + (chosenProcessID + 1) + " is moved to disk and points to -1 .");
            System.out.println("Memory | Process with ID = " + (processId + 1) + " took its place and points to address = " + bound + " .");

            memory[chosenProcessID].setValue(-1);
            DiskIO.getInstance().storeToDisk(chosenProcessID + 1,bound);
        }

        memory[processId - 1].setValue(bound);

        if (old == -1) {

            DiskIO.getInstance().loadFromDisk(processId, bound);
            System.out.println("Memory | Process with ID = " + (processId + 1) + " is loaded from Disk and its address = " + bound + " .");
        }


        return bound;
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