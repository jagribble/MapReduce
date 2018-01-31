package com.gribble;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ThreadClass {

    static HashMap<String,Airport> airportHashMap = new HashMap<String, Airport>();

    /**
     * Maps the airports in a hash map to enable them to be esaily looked up
     *
     * @param airportArrayLines lines of input csv
     * **/
    static void getAirportHashMap(ArrayList<String> airportArrayLines){
        for(int x=0;x<airportArrayLines.size();x++){
            String[] row = airportArrayLines.get(x).split(",");
            if(row.length-1>2){
                String airportName = row[0];
                String airportCode = row[1];
                Float lat = Float.valueOf(row[2]);
                Float lng = Float.valueOf(row[3]);
                Airport airport = new Airport(airportName,airportCode,lat,lng);
                airportHashMap.put(airportCode,airport);
            }

        }
    }

    /** Objective 1
     *
     * Determine the number of flights from each airport; include a list of any airports not used.
     *
     * **/
    public static ArrayList<MapperOutput> mapper1(ArrayList<String> mapperLines){
        ArrayList<MapperOutput> mapValue = new ArrayList<MapperOutput>();
        for(int x=0;x<mapperLines.size();x++){
            String[] row = mapperLines.get(x).split(",");

        }
        return mapValue;
    }

    /** Objective 2
     * Maps over a number of lines passed in and returns (key,value) pairs
     *
     * Create a list of flights based on the Flight id, this output should include the passenger Id, relevant
     IATA/FAA codes, the departure time, the arrival time (times to be converted to HH:MM:SS format),
     and the flight times.
     *
     * @param mapperLines lines from the list for the single mapper to map.
     * @return array of (key,value) pairs
     */
    public static ArrayList<MapperOutput> mapper2(ArrayList<String> mapperLines) throws ParseException {
        //this.getAirportHashMap();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:MM:SS");
        ArrayList<MapperOutput> mapValue = new ArrayList<MapperOutput>();
        for (int x=0;x<mapperLines.size();x++){
            String[] row = mapperLines.get(x).split(",");
            String passengerId = row[0];
            String flightId = row[1];
            String startingAirpot = row[2];
            String destinationAirport = row[3];
            Date depatureTime = new Date(Integer.valueOf(row[4]));
            int flightTime = Integer.valueOf(row[5]);
            if(passengerId.isEmpty() || startingAirpot.isEmpty() || destinationAirport.isEmpty() || flightTime == 0 ){
                System.out.println("Error at "+(x+1)+": Values missing");
            } else if(!airportHashMap.containsKey(startingAirpot)){
                System.out.println("Error at "+(x+1)+": Starting airport does not exist in airport list ("+startingAirpot+")");
            } else{
                PassengerFlight passengerFlight = new PassengerFlight(passengerId,startingAirpot,destinationAirport,depatureTime,flightTime);
                MapperOutput keyValue = new MapperOutput(flightId,passengerFlight);
                System.out.println("Key: "+keyValue.getKey()+"   ®Value: "+keyValue.getValue());
                mapValue.add(keyValue);
            }

        }
        return mapValue;
    }

    /** Objective 3
     *
     * Calculate the number of passengers on each flight.
     *
     * **/
    public static ArrayList<MapperOutput> mapper3(ArrayList<String> mapperLines){
        ArrayList<MapperOutput> mapValue = new ArrayList<MapperOutput>();
        for(int x=0;x<mapperLines.size();x++){
            String[] row = mapperLines.get(x).split(",");

        }
        return mapValue;
    }

    static HashMap<String,ArrayList<Object>> shuffler(ArrayList<MapperOutput> mapperOutput){
        HashMap<String,ArrayList<Object>> hashMap = new HashMap<String, ArrayList<Object>>();
        for(int x=0;x<mapperOutput.size();x++){
            String key = mapperOutput.get(x).getKey();
            Object value = mapperOutput.get(x).getValue();
            if(hashMap.containsKey(key)){
                hashMap.get(key).add(value);
            } else{
                ArrayList<Object> objectArrayList = new ArrayList<Object>();
                objectArrayList.add(value);
                hashMap.put(key,objectArrayList);
            }
        }
        return hashMap;
    }

    /** Reducer 2
     *
     * Set up output for each key
     * **/
    static void reducer2(String key, ArrayList<MapperOutput> mapper){

    }

}
