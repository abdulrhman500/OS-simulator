package process;

public class Process {
    int id;
    int pc;

    //PCB
    //Memory boundaries
    public Process(int id){
        this.id =id;
        this.pc=0;
    }

    public int getId() {
        return this.id;
    }
}
