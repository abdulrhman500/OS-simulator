package Main;

public class Arrival {
    private int arrivedAt;
    private String programPath;

    @Override
    public String toString() {
        return "Arrival{" +
                "arrivedAt=" + arrivedAt +
                ", programPath='" + programPath + '\'' +
                '}';
    }

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
