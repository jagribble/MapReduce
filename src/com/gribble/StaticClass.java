package com.gribble;

import java.util.ArrayList;
import java.util.HashMap;

public class StaticClass {

    public static HashMap<String,Airport> airportHashMap = new HashMap<String, Airport>();
    public static ArrayList<String> objective1Airports = new ArrayList<String>();

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


    static ReducerOuput missingAirports(){
        ArrayList<String> allAirports = new ArrayList<String>(StaticClass.airportHashMap.keySet());
        for (int x=0;x<StaticClass.objective1Airports.size();x++){
            allAirports.remove(StaticClass.objective1Airports.get(x));
        }
        String reducerString = "Missing Airports:\n";
        String reducerCSV = "";
        for(int x=0;x<allAirports.size();x++){
            reducerString += "          "+allAirports.get(x)+","+
                    StaticClass.airportHashMap.get(allAirports.get(x)).getAirportName()+"\n";
            reducerCSV += allAirports.get(x) +","+StaticClass.airportHashMap.get((allAirports.get(x))).getAirportName()+"\n";
        }
        ReducerOuput reducerOuput = new ReducerOuput(reducerString,reducerCSV);
        return reducerOuput;
    }

    static String makeCSVRow(String[] values){
        String csvString = "";
        for(int x=0;x<values.length;x++){
            csvString += values[x] +",";
        }
        csvString += "\n";
        return csvString;
    }

}
