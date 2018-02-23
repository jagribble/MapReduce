package com.gribble;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static ArrayList<String> lines = new ArrayList<String>();
    public static ArrayList<String> airportLines = new ArrayList<String>();

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

    public static void getAirportData(){
        airportLines = getLines("./src/Top30_airports_LatLong.csv");
    }

    public static void getPassangerData(){
        // read in file
        lines = getLines("./src/AComp_Passenger_data.csv");
    }

    static void runObjective1(){
        System.out.println("-------RUNNING MAPPER 1----");
        ThreadClass mapperThread = new ThreadClass("mapper1",lines,1);
        mapperThread.start();
        try {
            mapperThread.thread.join();
        } catch (InterruptedException e) {
            System.out.println("Main Thread interuppted");
            e.printStackTrace();
        }
        ArrayList<MapperOutput> mapperOutput = mapperThread.mapperOutput;
        System.out.println("--------END OF MAPPER 1----------");
        System.out.println("--------SHUFFLER 1----------");
        HashMap<String, ArrayList<Object>> shuffledOutput = StaticClass.shuffler(mapperOutput);
        System.out.println(shuffledOutput);
        System.out.println("--------END OF SHUFFLER 1----------");
        ArrayList<String> listofKeys = new ArrayList<String>(shuffledOutput.keySet());
        ArrayList<ThreadClass> threads = new ArrayList<ThreadClass>();
        ArrayList<ReducerOuput> reducer1Output = new ArrayList<ReducerOuput>();
        for(int x=0;x<listofKeys.size();x++){
            ThreadClass threadReducer = new ThreadClass("reducer"+1,shuffledOutput.get(listofKeys.get(x)),listofKeys.get(x),1);
            threads.add(threadReducer);
            threadReducer.start();
            try {
                threadReducer.thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        for(int x=0;x<threads.size();x++){
            ReducerOuput output = threads.get(x).reducerOuput;
            System.out.println(output.reducerString);
            System.out.println("-----------------------------------");
            reducer1Output.add(output);
        }

        ReducerOuput missingAirports = StaticClass.missingAirports();
        System.out.println(missingAirports.reducerString);
        String[] headings = {"Airport","Airport Code","Flights From Airport"};
        String[] additionalHeadings = {"Airport Code", "Airport Name"};
        createCSV("Objective1.csv",headings,reducer1Output,additionalHeadings,missingAirports.reducerCSV);


    }

    static void runObjective2() throws ParseException {
        System.out.println("-------RUNNING MAPPER 2----");
        ThreadClass mapperThread = new ThreadClass("mapper1",lines,2);
        mapperThread.start();
        try {
            mapperThread.thread.join();
        } catch (InterruptedException e) {
            System.out.println("Main Thread interuppted");
            e.printStackTrace();
        }
        ArrayList<MapperOutput> mapperOutput = mapperThread.mapperOutput;
        System.out.println("--------END OF MAPPER 2----------");
        System.out.println("--------SHUFFLER 2----------");
        HashMap<String, ArrayList<Object>> shuffledOutput = StaticClass.shuffler(mapperOutput);
        System.out.println(shuffledOutput);
        System.out.println("--------END OF SHUFFLER 1----------");
        ArrayList<String> listofKeys = new ArrayList<String>(shuffledOutput.keySet());
        ArrayList<ReducerOuput> reducer2Output = new ArrayList<ReducerOuput>();
        System.out.println("-----------------------------------");
        ArrayList<ThreadClass> threads = new ArrayList<ThreadClass>();
        for(int x=0;x<listofKeys.size();x++){
            ThreadClass threadReducer = new ThreadClass("reducer"+1,shuffledOutput.get(listofKeys.get(x)),listofKeys.get(x),2);
            threads.add(threadReducer);
            threadReducer.start();
            try {
                threadReducer.thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        for(int x=0;x<threads.size();x++){
            ReducerOuput output = threads.get(x).reducerOuput;
            System.out.println(output.reducerString);
            System.out.println("-----------------------------------");
            reducer2Output.add(output);
        }
        String[] headings = {"Flight ID","Flight Depature Time","Flight time","Source Airport","Destination Airport","Passengers"};
        createCSV("Objective2.csv",headings,reducer2Output);
    }

    static void runObjective3() throws ParseException {
        System.out.println("-------RUNNING MAPPER 3----");
        ThreadClass mapperThread = new ThreadClass("mapper1",lines,3);
        mapperThread.start();
        try {
            mapperThread.thread.join();
        } catch (InterruptedException e) {
            System.out.println("Main Thread interuppted");
            e.printStackTrace();
        }
        ArrayList<MapperOutput> mapperOutput = mapperThread.mapperOutput;
        System.out.println("--------END OF MAPPER 3----------");
        System.out.println("--------SHUFFLER 3----------");
        HashMap<String, ArrayList<Object>> shuffledOutput = StaticClass.shuffler(mapperOutput);
        System.out.println(shuffledOutput);
        System.out.println("--------END OF SHUFFLER 3----------");
        ArrayList<String> listofKeys = new ArrayList<String>(shuffledOutput.keySet());
        ArrayList<ReducerOuput> reducer3Output = new ArrayList<ReducerOuput>();
        ArrayList<ThreadClass> threads = new ArrayList<ThreadClass>();
        for(int x=0;x<listofKeys.size();x++){
            ThreadClass threadReducer = new ThreadClass("reducer"+1,shuffledOutput.get(listofKeys.get(x)),listofKeys.get(x),3);
            threads.add(threadReducer);
            threadReducer.start();
            try {
                threadReducer.thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        for(int x=0;x<threads.size();x++){
            ReducerOuput output = threads.get(x).reducerOuput;
            System.out.println(output.reducerString);
            System.out.println("-----------------------------------");
            reducer3Output.add(output);
        }
        String[] headings = {"Flight ID","Passengers on Flight"};
        createCSV("Objective3.csv",headings,reducer3Output);
    }

    static void createCSV(String name,String[] headings, ArrayList<ReducerOuput> reducerOuputs){
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(name);

            fileWriter.append(StaticClass.makeCSVRow(headings));
            for (int x=0;x<reducerOuputs.size();x++){
                fileWriter.append(reducerOuputs.get(x).reducerCSV);
            }
            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }

        }
    }

    static void createCSV(String name,String[] headings, ArrayList<ReducerOuput> reducerOuputs,String[] additionalHeadings, String additionalData){
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(name);

            fileWriter.append(StaticClass.makeCSVRow(headings));
            for (int x=0;x<reducerOuputs.size();x++){
                fileWriter.append(reducerOuputs.get(x).reducerCSV);
            }
            fileWriter.append("\n\n");
            fileWriter.append(StaticClass.makeCSVRow(additionalHeadings));

            fileWriter.append(additionalData);

            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }

        }
    }

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
