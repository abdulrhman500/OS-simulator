package process;

public class ProcessInfo {
    private int arrivalTime;
    private int totalNumberOfInstructions;
    private int executedInstructions;

    public ProcessInfo(int arrivalTime, int totalNumberOfInstructions, int executedInstructions) {
        this.arrivalTime = arrivalTime;
        this.totalNumberOfInstructions = totalNumberOfInstructions;
        this.executedInstructions = executedInstructions;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getTotalNumberOfInstructions() {
        return totalNumberOfInstructions;
    }

    public void setTotalNumberOfInstructions(int totalNumberOfInstructions) {
        this.totalNumberOfInstructions = totalNumberOfInstructions;
    }

    public int getExecutedInstructions() {
        return executedInstructions;
    }

    public void setExecutedInstructions(int executedInstructions) {
        this.executedInstructions = executedInstructions;
    }


}
