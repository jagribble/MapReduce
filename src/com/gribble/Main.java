package com.gribble;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

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
            ArrayList<MapperOutput> mapperOutput = ThreadClass.mapper(lines);
            System.out.println(ThreadClass.shuffler(mapperOutput));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
