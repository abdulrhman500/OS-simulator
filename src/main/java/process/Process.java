package process;

import Main.Constants;
import memory.Memory;

public class Process {
    int lowerBound;
    Memory mem = Memory.getInstance();


    State state;

    //PCB
    //Memory boundaries
    public Process(){
        mem.getLowerBound();
        this.lowerBound =mem.getLowerBound();

        mem.storeWord(lowerBound,"ID",Constants.PROCESS_COUNT++);
        mem.storeWord(lowerBound+1,"state",State.Ready);
        mem.storeWord(lowerBound+2,"PC",0);
        mem.storeWord(lowerBound+3,"lower-bound",lowerBound);



        this.loadFiveInstructions(lowerBound+7,0);

        this.state=State.Ready;
    }

    private void loadFiveInstructions(int startMemAdress,int line) {
        //TODO load 5 instructions from file
    }

    public int getId() {
        return (int) mem.getValueAt(this.lowerBound);
    }
}
