package process;

import Main.Constants;
import memory.Memory;

import java.io.File;
import java.util.Scanner;

public class Process {
    int lowerBound;
    Memory mem = Memory.getInstance();
   private int totalNumberOfInstruction = utils.programInfo.getTotalNumberOfInstruction(this.getCodeLocation());
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



        this.loadInstructions(lowerBound+7,0);

        this.state=State.Ready;
    }

    private void loadInstructions(int startMemAddress, int line) {
        try{
            File myObj = new File("src/main/resources/programs/"+ this.getCodeLocation());
            Scanner reader = new Scanner(myObj);

            //skipping already loaded instructions
            for(int i=0;i<line;i++){
                reader.nextLine();
            }

            for(int i=0;i<Constants.NUMBER_OF_INSTRUCTIONS;i++){
                mem.storeWord(startMemAddress+i,"I"+i,reader.nextLine());
                if(!reader.hasNextLine()){
                    mem.storeWord(startMemAddress+i+1,"Halt","");
                    break;
                }
            }
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
}
