package com.gribble;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ThreadClass implements Runnable {

    public Thread thread;
    private String threadName;
    private ArrayList<String> mapperLines;
    private int mode;
    private int mapperOffset;
    private boolean mapper;
    private ArrayList<Object> reducerValues;
    private String reducerKey;
    public String error = "";
    public ArrayList<MapperOutput> mapperOutput;
    public ReducerOuput reducerOuput;

    public ThreadClass(String tName,ArrayList<String> mLines,int m,int mOffset){
        this.threadName = tName;
        this.mapperLines = mLines;
        this.mode = m;
        this.mapper = true;
        this.mapperOffset = mOffset;
    }

    public ThreadClass(String tName,ArrayList<Object> rValues,String rKey,int m){
        this.threadName = tName;
        this.reducerValues = rValues;
        this.reducerKey = rKey;
        this.mode = m;
    }


    public void run(){
        if(this.mapper){
            try {
                switch (this.mode) {
                    case 1:
                        this.mapperOutput = this.mapper1(this.mapperLines);
                        break;
                    case 2:
                        this.mapperOutput = this.mapper2(this.mapperLines);
                        break;
                    case 3:
                        this.mapperOutput = this.mapper2(this.mapperLines);
                        break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else{
            switch (this.mode) {
                case 1:
                    this.reducerOuput = this.reducer1(this.reducerKey,this.reducerValues);
                    break;
                case 2:
                    this.reducerOuput = this.reducer2(this.reducerKey,this.reducerValues);
                    break;
                case 3:
                    this.reducerOuput = this.reducer3(this.reducerKey,this.reducerValues);
                    break;
            }
        }
    }

    public void start () {
        if (thread == null) {
            thread = new Thread (this, threadName);
            thread.start ();
        }
    }


    PassengerFlight checkForError(String[] row,int x){
        String passengerId = row[0];
        String flightId = row[1];
        String startingAirpot = row[2];
        String destinationAirport = row[3];
        String depatureTime = row[4];
        int flightTime = Integer.valueOf(row[5]);
        if(passengerId.isEmpty() || startingAirpot.isEmpty() || destinationAirport.isEmpty() || flightTime == 0 ){
            this.error += "Error at "+(x+mapperOffset+1)+": Values missing\n";
            System.err.println("Error at "+(x+mapperOffset+1)+": Values missing");
        } else if(!StaticClass.airportHashMap.containsKey(startingAirpot)){
            this.error += "Error at "+(x+mapperOffset+1)+": Starting airport does not exist in airport list ("+startingAirpot+")\n";
            System.err.println("Error at "+(x+mapperOffset+1)+": Starting airport does not exist in airport list ("+startingAirpot+")");
        } else{
            PassengerFlight passengerFlight = new PassengerFlight(passengerId,flightId,startingAirpot,destinationAirport,depatureTime,flightTime);
            if(passengerFlight.error){
                this.error += "Error at "+(x+mapperOffset+1)+": "+passengerFlight.errorMessage+"\n";
            }
            return passengerFlight;
        }
        return null;
    }


    /** Objective 1
     *
     * Determine the number of flights from each airport; include a list of any airports not used.
     *
     * **/
    public ArrayList<MapperOutput> mapper1(ArrayList<String> mapperLines){
        ArrayList<MapperOutput> mapValue = new ArrayList<MapperOutput>();
        for(int x=0;x<mapperLines.size();x++){
            String[] row = mapperLines.get(x).split(",");
            PassengerFlight passengerFlight = checkForError(row,x);
                if (passengerFlight != null && !passengerFlight.error){
                    MapperOutput keyValue = new MapperOutput(row[2],row[1]);
                    mapValue.add(keyValue);
                }
            }
//            String startingAirpot = row[2];
//            String flightId = row[1];
//            if(startingAirpot.isEmpty()){
//                System.err.println("Error at "+(x+1)+": starting airpot missing");
//            } else if(flightId.isEmpty()){
//                System.err.println("Error at "+(x+1)+": Flight ID missing");
//            }else if(!StaticClass.airportHashMap.containsKey(startingAirpot)){
//                System.err.println("Error at "+(x+1)+": Starting airport does not exist in airport list ("+startingAirpot+")");
//            } else{
//                MapperOutput keyValue = new MapperOutput(startingAirpot,flightId);
//                mapValue.add(keyValue);
//            }


        return mapValue;
    }

    /** Objective 2 & Objective 3 Mapper
     * - Maps over a number of lines passed in and returns (key,value) pairs
     *
     *  Create a list of flights based on the Flight id, this output should include the passenger Id, relevant
        IATA/FAA codes, the departure time, the arrival time (times to be converted to HH:MM:SS format),
         and the flight times.
     *
     * - Calculate the number of passengers on each flight.
     *
     * @param mapperLines lines from the list for the single mapper to map.
     * @return array of (key,value) pairs
     */
    public ArrayList<MapperOutput> mapper2(ArrayList<String> mapperLines) throws ParseException {
        //this.getAirportHashMap();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:MM:SS");
        ArrayList<MapperOutput> mapValue = new ArrayList<MapperOutput>();
        for (int x=0;x<mapperLines.size();x++){
            String[] row = mapperLines.get(x).split(",");
            PassengerFlight passengerFlight = checkForError(row,x);
            if (passengerFlight != null && !passengerFlight.error){
                MapperOutput keyValue = new MapperOutput(row[1],passengerFlight);
                mapValue.add(keyValue);
            }
        }
//                if (!passengerFlight.error){
//                    MapperOutput keyValue = new MapperOutput(flightId,passengerFlight);
//                    //System.out.println("Key: "+keyValue.getKey()+"   ®Value: "+keyValue.getValue());
//                    mapValue.add(keyValue);
//                }

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


     public ReducerOuput reducer1(String key, ArrayList<Object> values){
        ArrayList<String> flights = new ArrayList<String>();
        for(int x=0;x<values.size();x++){
            String flightID = String.valueOf(values.get(x));
            if(!flights.contains(flightID)){
                flights.add(flightID);
            }
        }
        String airportName = StaticClass.airportHashMap.get(key).getAirportName();
        StaticClass.objective1Airports.add(key);
        String reducerString = "Airport:              "+airportName+"\n";
        reducerString += "Airport Code:         "+ key+"\n";
        reducerString += "Flights From Airport: "+ flights.size()+"\n";
        String[] options = {airportName,key,String.valueOf(flights.size())};
        String rCSV = StaticClass.makeCSVRow(options);
        return new ReducerOuput(reducerString,rCSV);
    }

    /** Reducer 2
     *
     * Set up output for each key
     * **/
    public ReducerOuput reducer2(String key, ArrayList<Object> values){
        PassengerFlight flight = (PassengerFlight) values.get(0);
        String reducerString = "";
        reducerString += "Flight ID:            "+key+"\n";
        reducerString += "Flight Depature Time: "+flight.getDepatureTime()+"\n";
        reducerString += "Flight time:          "+flight.getFlightTime()+"\n";
        reducerString += "Source Airport:       "+flight.getSourceAirport()+"\n";
        reducerString += "Destination Airport:  "+flight.getDestinationAirport()+"\n";
        reducerString += "Passengers:           "+"\n";
        String passengerString = "";
        for(int x=0;x<values.size();x++){
            PassengerFlight passenger = (PassengerFlight) values.get(x);
            reducerString += "                    "+passenger.getPassengerId()+"\n";
            passengerString +=passenger.getPassengerId()+";";
        }
        String[] options = {key,String.valueOf(flight.getDepatureTime()),String.valueOf(flight.getFlightTime()),
                flight.getSourceAirport(),flight.getDestinationAirport(),passengerString};
        String rCSV = StaticClass.makeCSVRow(options);
        return new ReducerOuput(reducerString,rCSV);


    }

    public ReducerOuput reducer3(String key, ArrayList<Object> values){
        PassengerFlight flight = (PassengerFlight) values.get(0);
        String reducerString = "";
        reducerString += "Flight ID:            "+key+"\n";
        reducerString += "Passengers on Flight: "+values.size();
        String[] options = {key,String.valueOf(values.size())};
        String rCSV = StaticClass.makeCSVRow(options);
        return new ReducerOuput(reducerString,rCSV);

    }



}
