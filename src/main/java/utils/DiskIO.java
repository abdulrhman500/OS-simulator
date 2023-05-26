package utils;

import java.io.*;
import java.util.Hashtable;
import java.util.Scanner;

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

    public void storeToDisk(int processID, int bound) {
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            String fileName = getProcessFileName(processID);
            File processFile = new File(fileName);
            if (!processFile.exists())
                processFile.createNewFile();

            fileWriter = new FileWriter(processFile, true);
            bufferedWriter = new BufferedWriter(fileWriter);

            int lowerBound =bound;
            for (int i = 0; i < PROCESS_SPACE; i++) {
                System.out.println("LLLLL "+processID + "  ...... "+lowerBound);
                MemoryWord curr = Memory.getInstance().loadWord(i + lowerBound);
                System.out.println("gggggggg "+curr);
                String Name ;
                Object value ;
                String dataType ;

                if(curr == null){
                    Name ="null";
                    value = "null";
                    dataType = "null";
                }else {
                    Name = curr.getName();
                    value = curr.getValue();
                    dataType = getDataType(value);
                }
                bufferedWriter.write(Name);
                bufferedWriter.newLine();
                bufferedWriter.write(value + "");
                bufferedWriter.newLine();
                bufferedWriter.write(dataType);
                bufferedWriter.newLine();

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (fileWriter == null || bufferedWriter == null) return;
                bufferedWriter.flush();
                bufferedWriter.close();
                fileWriter.close();
                System.out.println("store To Disk is done");
                printFileToConsole(processID);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String getDataType(Object value) {
        if (value instanceof Integer)
            return Integer.class.getName();
        if (value instanceof String)
            return String.class.getName();
        if (value instanceof Double)
            return Double.class.getName();
        if (value instanceof Float)
            return Float.class.getName();

        return Object.class.getName();

    }

    public void loadFromDisk(int processID, int address) {
        System.out.println("The next file is loaded from disk to location "+address);
        printFileToConsole(processID);

        String fileName = getProcessFileName(processID);

        File processFile = new File(fileName);

        try {
            FileReader fileReader = new FileReader(fileName);

            // Create a BufferedReader object to read text
            BufferedReader bufferedReader = new BufferedReader(fileReader);


            for (int i = 0; i < PROCESS_SPACE; i++) {
                String Name = bufferedReader.readLine();
                Object value = bufferedReader.readLine();
                String dataType = bufferedReader.readLine();
                Memory.getInstance().storeWord(address + i, Name, changeObjectType(value, dataType));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (processFile.exists()) {
                processFile.delete();
            }
        }
    }

    private Object changeObjectType(Object value, String dataType) {

        if (dataType.equals(String.class.getName()))
            return (String) value;
        if (dataType.equals(Integer.class.getName()))
            return (Integer) value;
        if (dataType.equals(Double.class.getName()))
            return (Double) value;
        if (dataType.equals(Float.class.getName()))
            return (Float) value;

        return (Object) value;
    }

    private int getLowerBoundOf(int processID) {

        return 0;
    }
    public void printFileToConsole(int processID) {
        File tmp = new File(getProcessFileName(processID));
        try {
            Scanner sc = new Scanner(tmp);
            System.out.println("--------START OF PROCESS " + processID + " ON DISK-----------");

            while (sc.hasNext()) {
                System.out.println(sc.nextLine());
            }
            System.out.println("---------END OF PROCESS ON DISK--------------");


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    public String getProcessFileName(int processID) {
        return DISK_PATH + "/" + processID + ".txt";
    }
}
