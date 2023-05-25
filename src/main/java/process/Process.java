package process;

import Main.Constants;
import memory.Memory;

import java.io.File;
import java.util.Scanner;

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
        //TODO change this to a generic number of instructions to reach the case of storing a process to disk
        try{
            File myObj = new File("src/main/resources/programs/"+ this.getCodeLocation());
            Scanner reader = new Scanner(myObj);
            for(int i=0;i<line;i++){
                reader.nextLine();
            }
            mem.storeWord(startMemAdress,"I1",reader.nextLine());
            mem.storeWord(startMemAdress+1,"I2",reader.nextLine());
            mem.storeWord(startMemAdress+2,"I3",reader.nextLine());
            mem.storeWord(startMemAdress+3,"I4",reader.nextLine());
            mem.storeWord(startMemAdress+4,"I5",reader.nextLine());

            reader.close();
        }catch (Exception ignored){}
    }

    public int getId() {
        return (int) mem.getValueAt(this.lowerBound);
    }

    private String getCodeLocation(){
        int id =this.getId();
        if(id ==1){
            return Constants.PROGRAM_1_CODE;
        }
        if(id ==2){
            return Constants.PROGRAM_2_CODE;
        }
        return Constants.PROGRAM_3_CODE;

    }
}
