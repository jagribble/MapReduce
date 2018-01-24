package com.gribble;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ThreadClass {

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
                System.out.println("Error at "+(x+1)+": Values missing");
            } else{
                PassengerFlight passengerFlight = new PassengerFlight(passengerId,startingAirpot,destinationAirport,depatureTime,flightTime);
                MapperOutput keyValue = new MapperOutput(row[1],passengerFlight);
                System.out.println("Key: "+keyValue.getKey()+"   ®Value: "+keyValue.getValue());
                mapValue.add(keyValue);
            }

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

}
