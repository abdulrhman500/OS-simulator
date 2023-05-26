package utils;

import java.io.*;
import java.util.Hashtable;

import Main.Constants;
import memory.Memory;
import memory.MemoryWord;
import process.Process;

import static Main.Constants.*;

public class DiskIO {
    private static DiskIO instance;

    private DiskIO() {

    }

    public static DiskIO getInstance() {
        if (instance == null)
            return new DiskIO();
        return instance;
    }

    Hashtable<Integer, Process> stringProcessHashtable = new Hashtable<>();

    public void storeToDisk(Process process) throws IOException {
        String fileName = getProcessFileName(process.getId());
        File processFile = new File(fileName);
        if (!processFile.exists()) {
            processFile.createNewFile();
        }
        FileOutputStream fileWriter = new FileOutputStream(processFile);
        ObjectOutputStream Writer = new ObjectOutputStream(fileWriter);
        int lowerBound = process.getLowerBound();
        for (int i = 0; i < Constants.PROCESS_SPACE; i++) {
            MemoryWord curr = Memory.getInstance().loadWord(lowerBound + i);
            Writer.writeObject(curr);
        }
        Writer.flush();
        Writer.close();
        fileWriter.close();
        process.setLowerBound(-1);
        stringProcessHashtable.put(process.getId(), process);
    }

    public Process loadFromDisk(int processID) {

        String fileName = getProcessFileName(processID);

        File processFile = new File(fileName);

        try {
            FileInputStream fileInputStream = new FileInputStream(processFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            MemoryWord[] info = new MemoryWord[PROCESS_SPACE];
            for (int i = 0; i < PROCESS_SPACE; i++) {
                MemoryWord curr = (MemoryWord) objectInputStream.readObject();
                info[i] = curr;
            }
            int lowerBound = (Integer) info[3].getValue();

            if (lowerBound == -1) {
                int prID = (Integer) info[0].getValue();
                lowerBound = Memory.getInstance().getLowerBound(prID - 1);
                info[3].setValue(lowerBound);
            }

            for (int i = 0; i < info.length; i++) {
                Memory.getInstance().storeWord(lowerBound + i, info[i].getName(), info[i].getValue());
            }

            stringProcessHashtable.get(processID).setLowerBound(lowerBound);
            Process tmp = stringProcessHashtable.get(processID);
            stringProcessHashtable.remove(processID);
            return tmp;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (processFile.exists()) {
                processFile.delete();
            }
        }
    }
    private int getLowerBoundOf(int processID) {

        return 0;
    }

    public String getProcessFileName(int processID) {
        return DISK_PATH + "/" + processID + ".txt";
    }
}
