package Main;

public class Arrival {
    private int arrivedAt;
    private String programPath;

    public Arrival(int arrivedAt, String programPath) {
        this.arrivedAt = arrivedAt;
        this.programPath = programPath;
    }

    public int getArrivedAt() {
        return arrivedAt;
    }

    public String getProgramPath() {
        return programPath;
    }

}
