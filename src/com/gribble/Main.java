package com.gribble;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Main with functions to call each objective
 */
public class Main {

    public static ArrayList<String> lines = new ArrayList<String>();
    public static ArrayList<String> airportLines = new ArrayList<String>();


    /**
     * Get lines from file
     *
     * @param pathToFile    path to read file from
     * @return
     */
    static ArrayList<String> getLines(String pathToFile){
        ArrayList<String> arrayOfLines = new ArrayList<String>();
        // store each line in array object
        String line = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathToFile));
            while((line = reader.readLine()) != null ){
              //  System.out.println(line);
                arrayOfLines.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayOfLines;
    }

    /**
     * Get airport data locally
     */
    public static void getAirportData(){
        airportLines = getLines("./src/Top30_airports_LatLong.csv");
    }

    /**
     * Get airport data from Directory
     *
     * @param dirName   directory specified by user
     */
    public static void getAirportData(String dirName){
        airportLines = getLines(dirName+"/Top30_airports_LatLong.csv");
        System.out.println(airportLines);
    }

    /**
     * Get Passenger Data locally
     */
    public static void getPassangerData(){
        // read in file
        lines = getLines("./src/AComp_Passenger_data.csv");
    }

    /**
     * Get passenger data from directory
     *
     * @param dirName   directory specified by user
     */
    public static void getPassangerData(String dirName){
        // read in file
        lines = getLines(dirName+"/AComp_Passenger_data.csv");
        System.out.println(lines);
    }

    /**
     * Objetive 1
     *
     * @return ReducerString output
     */
    static String runObjective1(){
        String errorString = "";
        System.out.println("-------RUNNING MAPPER 1----");
        // Split the lines in to equal chunks
        ArrayList<ArrayList<String>> lineChunks = StaticClass.getChunks(lines,10);
        ArrayList<ThreadClass> mapperThreads = new ArrayList<ThreadClass>();
        // for each chunk make a new thread to run mapper 1
        for (int x=0;x<lineChunks.size();x++){
            ThreadClass mapperThread = new ThreadClass("mapper1"+x,lineChunks.get(x),1,x*10);
            // start the thread
            mapperThread.start();
            // add the mapper to the thread list
            mapperThreads.add(mapperThread);


        }
        for(int x=0;x<mapperThreads.size();x++){
            try {
                // wait for all threads to finish by joining them
                mapperThreads.get(x).thread.join();
            } catch (InterruptedException e) {
                System.out.println("Main Thread interuppted");
                e.printStackTrace();
            }
        }
        // shuffled output as hashmap
        HashMap<String, ArrayList<Object>> shuffledOutput = new HashMap<String, ArrayList<Object>>();
        for (int x=0;x<mapperThreads.size();x++){
            // get the mapper output from each thread and add it to the shuffled output
            errorString += mapperThreads.get(x).error;
            ArrayList<MapperOutput> mapperOutput = mapperThreads.get(x).mapperOutput;
            shuffledOutput = StaticClass.shuffler(mapperOutput,shuffledOutput);
        }

        System.out.println("--------END OF MAPPER 1----------");
        System.out.println("--------SHUFFLER 1----------");

        System.out.println(shuffledOutput);
        System.out.println("--------END OF SHUFFLER 1----------");
        ArrayList<String> listofKeys = new ArrayList<String>(shuffledOutput.keySet());
        ArrayList<ThreadClass> threads = new ArrayList<ThreadClass>();
        ArrayList<ReducerOuput> reducer1Output = new ArrayList<ReducerOuput>();
        // For each key in the shuffled mapper output make a reducer thread and add it to the reducer thread array
        for(int x=0;x<listofKeys.size();x++){
            ThreadClass threadReducer = new ThreadClass("reducer-2"+x,shuffledOutput.get(listofKeys.get(x)),listofKeys.get(x),1);
            threadReducer.start();
            threads.add(threadReducer);

        }
        for(int x=0;x<threads.size();x++){
            try {
                // wait for all threads to finish by joining them
                threads.get(x).thread.join();
            } catch (InterruptedException e) {
                System.out.println("Main Thread interuppted");
                e.printStackTrace();
            }
        }
        String reducedOutput = "";
        // get the output from each reducer thread and add it to the ouput list
        for(int x=0;x<threads.size();x++){
            ReducerOuput output = threads.get(x).reducerOuput;
            reducedOutput+= output.reducerString;
            System.out.println(output.reducerString);
            reducedOutput += "\r\n-----------------------------------\r\n";
            System.out.println("-----------------------------------");
            reducer1Output.add(output);
        }

        ReducerOuput missingAirports = StaticClass.missingAirports();
        reducedOutput += missingAirports.reducerString;
        System.out.println(missingAirports.reducerString);
        String[] headings = {"Airport","Airport Code","Flights From Airport"};
        String[] additionalHeadings = {"Airport Code", "Airport Name"};
        StaticClass.makeTxtFile("Objetive1ERRORS.log",errorString);
        StaticClass.objective1CSVString = StaticClass.createCSVString(headings,reducer1Output,additionalHeadings,missingAirports.reducerCSV);
        StaticClass.objective1TextString = reducedOutput;
        return reducedOutput;

    }

    /**
     * Objective 2
     *
     * @return Reducer String output
     * @throws ParseException
     */
    static String runObjective2() throws ParseException {
        String errorString = "";
        System.out.println("-------RUNNING MAPPER 2----");
        // Split the lines in to equal chunks
        ArrayList<ArrayList<String>> lineChunks = StaticClass.getChunks(lines,10);
        ArrayList<ThreadClass> mapperThreads = new ArrayList<ThreadClass>();
        // for each chunk make a new thread to run mapper 1
        for (int x=0;x<lineChunks.size();x++){
            ThreadClass mapperThread = new ThreadClass("mapper2"+x,lineChunks.get(x),2,x*10);
            // start the thread
            mapperThread.start();
            // add the mapper to the thread list
            mapperThreads.add(mapperThread);

        }
        for(int x=0;x<mapperThreads.size();x++){
            try {
                // wait for all threads to finish by joining them
                mapperThreads.get(x).thread.join();
            } catch (InterruptedException e) {
                System.out.println("Main Thread interuppted");
                e.printStackTrace();
            }
        }

        // shuffled output as hashmap
        HashMap<String, ArrayList<Object>> shuffledOutput = new HashMap<String, ArrayList<Object>>();
        for (int x=0;x<mapperThreads.size();x++){
            // get the mapper output from each thread and add it to the shuffled output
            errorString += mapperThreads.get(x).error;
            ArrayList<MapperOutput> mapperOutput = mapperThreads.get(x).mapperOutput;
            shuffledOutput = StaticClass.shuffler(mapperOutput,shuffledOutput);
        }
        System.out.println("--------END OF MAPPER 2----------");
        System.out.println("--------SHUFFLER 2----------");

        System.out.println(shuffledOutput);

        System.out.println("--------END OF SHUFFLER 1----------");

        ArrayList<String> listofKeys = new ArrayList<String>(shuffledOutput.keySet());
        ArrayList<ReducerOuput> reducer2Output = new ArrayList<ReducerOuput>();
        System.out.println("-----------------------------------");
        ArrayList<ThreadClass> threads = new ArrayList<ThreadClass>();
        for(int x=0;x<listofKeys.size();x++){
            ThreadClass threadReducer = new ThreadClass("reducer-2"+x,shuffledOutput.get(listofKeys.get(x)),listofKeys.get(x),2);
            threadReducer.start();
            threads.add(threadReducer);
        }
        for(int x=0;x<threads.size();x++){
            try {
                // wait for all threads to finish by joining them
                threads.get(x).thread.join();
            } catch (InterruptedException e) {
                System.out.println("Main Thread interuppted");
                e.printStackTrace();
            }
        }
        String reducedOutput = "";
        for(int x=0;x<threads.size();x++){
            ReducerOuput output = threads.get(x).reducerOuput;
            reducedOutput += output.reducerString;
            System.out.println(output.reducerString);
            reducedOutput += "\r\n-----------------------------------\r\n";
            System.out.println("-----------------------------------");
            reducer2Output.add(output);
        }
        StaticClass.makeTxtFile("Objetive2ERRORS.log",errorString);
        String[] headings = {"Flight ID","Flight Depature Time","Flight time","Arrival Time","Source Airport","Destination Airport","Passengers"};
        StaticClass.objective2CSVString = StaticClass.createCSVString(headings,reducer2Output);
        StaticClass.objective2TextString = reducedOutput;
        return reducedOutput;
    }

    /**
     * Objective 3
     *
     * @return  Reducer String output
     * @throws ParseException
     */
    static String runObjective3() throws ParseException {
        String errorString = "";
        System.out.println("-------RUNNING MAPPER 3----");
        // Split the lines in to equal chunks
        ArrayList<ArrayList<String>> lineChunks = StaticClass.getChunks(lines,10);
        ArrayList<ThreadClass> mapperThreads = new ArrayList<ThreadClass>();
        // for each chunk make a new thread to run mapper 1
        for (int x=0;x<lineChunks.size();x++){
            ThreadClass mapperThread = new ThreadClass("mapper3"+x,lineChunks.get(x),3,x*10);
            // start the thread
            mapperThread.start();
            // add the mapper to the thread list
            mapperThreads.add(mapperThread);
            // start the thread
            mapperThread.start();

        }
        for(int x=0;x<mapperThreads.size();x++){
            try {
                // wait for all threads to finish by joining them
                mapperThreads.get(x).thread.join();
            } catch (InterruptedException e) {
                System.out.println("Main Thread interuppted");
                e.printStackTrace();
            }
        }
        // shuffled output as hashmap
        HashMap<String, ArrayList<Object>> shuffledOutput = new HashMap<String, ArrayList<Object>>();
        for (int x=0;x<mapperThreads.size();x++){
            // get the mapper output from each thread and add it to the shuffled output
            errorString += mapperThreads.get(x).error;
            ArrayList<MapperOutput> mapperOutput = mapperThreads.get(x).mapperOutput;
            shuffledOutput = StaticClass.shuffler(mapperOutput,shuffledOutput);
        }
        System.out.println("--------END OF MAPPER 3----------");
        System.out.println("--------SHUFFLER 3----------");

        System.out.println(shuffledOutput);

        System.out.println("--------END OF SHUFFLER 3----------");
        ArrayList<String> listofKeys = new ArrayList<String>(shuffledOutput.keySet());
        ArrayList<ReducerOuput> reducer3Output = new ArrayList<ReducerOuput>();
        ArrayList<ThreadClass> threads = new ArrayList<ThreadClass>();
        for(int x=0;x<listofKeys.size();x++){
            ThreadClass threadReducer = new ThreadClass("reducer-3"+x,shuffledOutput.get(listofKeys.get(x)),listofKeys.get(x),3);
            threadReducer.start();
            threads.add(threadReducer);
        }
        for(int x=0;x<threads.size();x++){
            try {
                // wait for all threads to finish by joining them
                threads.get(x).thread.join();
            } catch (InterruptedException e) {
                System.out.println("Main Thread interuppted");
                e.printStackTrace();
            }
        }
        String reducedOutput = "";
        for(int x=0;x<threads.size();x++){
            ReducerOuput output = threads.get(x).reducerOuput;
            System.out.println(output.reducerString);
            reducedOutput+=output.reducerString;
            reducedOutput += "\r\n-----------------------------------\r\n";
            System.out.println("-----------------------------------");
            reducer3Output.add(output);
        }
        StaticClass.makeTxtFile("Objetive3ERRORS.log",errorString);
        String[] headings = {"Flight ID","Passengers on Flight"};
        StaticClass.objective3CSVString = StaticClass.createCSVString(headings,reducer3Output);
        StaticClass.objective3TextString = reducedOutput;
        return reducedOutput;
    }


    /**
     * Run in command line
     * @param args
     */
    public static void main(String[] args) {
	    // write your code here
        getAirportData();
        System.out.println("----------------------");
        getPassangerData();
        StaticClass.getAirportHashMap(airportLines);

        try {
            runObjective1();

            runObjective2();

            runObjective3();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
