package com.gribble;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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



    public void reducer(){

    }

    public static void main(String[] args) {
	    // write your code here
        getAirportData();
        System.out.println("----------------------");
        getPassangerData();
        try {
            ThreadClass.getAirportHashMap(airportLines);
            ArrayList<MapperOutput> mapperOutput = ThreadClass.mapper2(lines);
            HashMap<String, ArrayList<Object>> shuffledOutput = ThreadClass.shuffler(mapperOutput);
            System.out.println(shuffledOutput);
            ArrayList<String> listofKeys = new ArrayList<String>(shuffledOutput.keySet());
            ArrayList<String> reducer2Output = new ArrayList<String>();
            ArrayList<String> reducer3Output = new ArrayList<String>();
            System.out.println("-----------------------------------");

            for(int x=0;x<listofKeys.size();x++){
                String output = ThreadClass.reducer2(listofKeys.get(x),  shuffledOutput.get(listofKeys.get(x)));
                System.out.println(output);
                System.out.println("-----------------------------------");
                reducer2Output.add(output);
            }

            for(int x=0;x<listofKeys.size();x++){
                String output = ThreadClass.reducer3(listofKeys.get(x),  shuffledOutput.get(listofKeys.get(x)));
                System.out.println(output);
                System.out.println("-----------------------------------");
                reducer3Output.add(output);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
