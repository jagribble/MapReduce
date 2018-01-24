package com.gribble;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
                System.out.println(line);
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


    /** Objective 2
     * Maps over a number of lines passed in and returns (key,value) pairs
     *
     * @param mapperLines lines from the list for the single mapper to map.
     * @return array of (key,value) pairs
     */
    public static ArrayList<MapperOutput> mapper(ArrayList<String> mapperLines) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:MM:SS");
        ArrayList<MapperOutput> mapValue = new ArrayList<MapperOutput>();
        for (int x=0;x<mapperLines.size();x++){
            String[] row = mapperLines.get(x).split(",");
            String passengerId = row[0];
            String startingAirpot = row[2];
            String destinationAirport = row[3];
            Date depatureTime = new Date(Integer.valueOf(row[4]));
            int flightTime = Integer.valueOf(row[5]);
            if(passengerId.isEmpty() || startingAirpot.isEmpty() || destinationAirport.isEmpty() || flightTime == 0 ){
                System.out.println("Error at "+(x+1)+" Value missing");
            }
            PassengerFlight passengerFlight = new PassengerFlight(passengerId,startingAirpot,destinationAirport,depatureTime,flightTime);
            MapperOutput keyValue = new MapperOutput(row[1],passengerFlight);
            mapValue.add(keyValue);
        }
        return mapValue;
    }

    public void reducer(){

    }

    public static void main(String[] args) {
	    // write your code here
        getAirportData();
        System.out.println("----------------------");
        getPassangerData();
        try {
            mapper(lines);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
