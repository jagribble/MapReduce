package com.gribble;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Class to run threads for mapper and reducer
 */
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

    /**
     * ThreadClass Constructor for mapper
     *
     * @param tName thread name
     * @param mLines    mapperlines
     * @param m mode (1,2,3)
     * @param mOffset   mapper offset - where the mapper starts from in the csv
     */
    public ThreadClass(String tName,ArrayList<String> mLines,int m,int mOffset){
        this.threadName = tName;
        this.mapperLines = mLines;
        this.mode = m;
        this.mapper = true;
        this.mapperOffset = mOffset;
    }

    /**
     * ThreadClass Constructor for Reducer
     *
     * @param tName thread name
     * @param rValues   reducer values ( mapper outputs)
     * @param rKey  reducer key
     * @param m mode(1,2,3)
     */
    public ThreadClass(String tName,ArrayList<Object> rValues,String rKey,int m){
        this.threadName = tName;
        this.reducerValues = rValues;
        this.reducerKey = rKey;
        this.mode = m;
    }


    /**
     * Run the thread
     */
    public void run(){
        // For the relevant type run the relvent mode
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

    /**
     * Start the thread
     */
    public void start () {
        if (thread == null) {
            thread = new Thread (this, threadName);
            thread.start ();
        }
    }


    /**
     *Check for errors in the data
     *
     * @param row   row passed to object
     * @param x position in mapper
     * @return  passenger
     */
    PassengerFlight checkForError(String[] row,int x){
        String passengerId = row[0];
        String flightId = row[1];
        String startingAirpot = row[2];
        String destinationAirport = row[3];
        String depatureTime = row[4];
        int flightTime = Integer.valueOf(row[5]);
        // if the rows are empty print error
        if(passengerId.isEmpty() || startingAirpot.isEmpty() || destinationAirport.isEmpty() || flightTime == 0 ){
            this.error += "Error at "+(x+mapperOffset+1)+": Values missing\r\n";
            System.err.println("Error at "+(x+mapperOffset+1)+": Values missing");
            // if the starting airport is not in the airport has map print error
        } else if(!StaticClass.airportHashMap.containsKey(startingAirpot)){
            this.error += "Error at "+(x+mapperOffset+1)+": Starting airport does not exist in airport list ("+startingAirpot+")\r\n";
            System.err.println("Error at "+(x+mapperOffset+1)+": Starting airport does not exist in airport list ("+startingAirpot+")");
            // if the destination airport is not in the airport hash mpa print error
        } else if(!StaticClass.airportHashMap.containsKey(destinationAirport)){
            this.error += "Error at "+(x+mapperOffset+1)+": Destination airport does not exist in airport list ("+destinationAirport+")\r\n";
            System.err.println("Error at "+(x+mapperOffset+1)+": Destination airport does not exist in airport list ("+destinationAirport+")");
        } else{
            // If when contructing passanger error is created then print error or error correction else return passenger
            PassengerFlight passengerFlight = new PassengerFlight(passengerId,flightId,startingAirpot,destinationAirport,depatureTime,flightTime);
            if(passengerFlight.error){
                this.error += "Error at "+(x+mapperOffset+1)+": "+passengerFlight.errorMessage+"\r\n";
            }
            if(passengerFlight.errorCorretion){
                this.error += "Error Correction at: "+(x+mapperOffset+1)+": "+passengerFlight.errorMessage+"\r\n";
            }
            return passengerFlight;
        }
        return null;
    }

    /**
     * Objective 1
     * Determine the number of flights from each airport; include a list of any airports not used.
     *
     * @param mapperLines   lines passed to the mapper
     * @return
     */
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

        return mapValue;
    }


    /**
     * Make ouptut for objective 1
     *
     * @param key   key of airport
     * @param values    values of flights
     * @return
     */
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
        String reducerString = "Airport:              "+airportName+"\r\n";
        reducerString += "Airport Code:         "+ key+"\r\n";
        reducerString += "Flights From Airport: "+ flights.size()+"\r\n";
        String[] options = {airportName,key,String.valueOf(flights.size())};
        String rCSV = StaticClass.makeCSVRow(options);
        return new ReducerOuput(reducerString,rCSV);
    }


    /**
     * Make output for objective 2
     *
     * @param key   key of flightID
     * @param values    Value of passenger flights
     * @return
     */
    public ReducerOuput reducer2(String key, ArrayList<Object> values){
        PassengerFlight flight = (PassengerFlight) values.get(0);

        String arrivalTime = new SimpleDateFormat("HH:mm:ss").format(flight.getArraivalTime());
        String reducerString = "";
        reducerString += "Flight ID:            "+key+"\r\n";
        reducerString += "Flight Depature Time: "+flight.getDepatureTime()+"\r\n";
        reducerString += "Flight time:          "+flight.getFlightTime()+"minutes\r\n";
        reducerString += "Arrival Time:         "+arrivalTime+"\r\n";
        reducerString += "Source Airport:       "+flight.getSourceAirport()+"\r\n";
        reducerString += "Destination Airport:  "+flight.getDestinationAirport()+"\r\n";
        reducerString += "Passengers:           "+"\r\n";
        String passengerString = "";
        for(int x=0;x<values.size();x++){
            PassengerFlight passenger = (PassengerFlight) values.get(x);
            // Check if the passenger is in the hashmap if not add it to the list if it is then discard duplicates
            if(!StaticClass.passengerObjective2Hash.containsKey(passenger.getPassengerId())){
                StaticClass.passengerObjective2Hash.put(passenger.getPassengerId(),passenger.getPassengerId());
                reducerString += "                    "+passenger.getPassengerId()+"\r\n";
                passengerString +=passenger.getPassengerId()+";";
            }
        }

        String[] options = {key,String.valueOf(flight.getDepatureTime()),String.valueOf(flight.getFlightTime()),arrivalTime,
                flight.getSourceAirport(),flight.getDestinationAirport(),passengerString};
        String rCSV = StaticClass.makeCSVRow(options);
        return new ReducerOuput(reducerString,rCSV);


    }

    /**
     * Make output for objetive 3
     *
     * @param key   key of Flight ID
     * @param values value of passenger flights
     * @return
     */
    public ReducerOuput reducer3(String key, ArrayList<Object> values){

        String reducerString = "";

        int count = 0;
        for(int x=0;x<values.size();x++){
            PassengerFlight passenger = (PassengerFlight) values.get(x);
            // Check if the passenger is in the hashmap if not add it to the list if it is then discard duplicates
            if(!StaticClass.passengerObjective3Hash.containsKey(passenger.getPassengerId())){
                StaticClass.passengerObjective3Hash.put(passenger.getPassengerId(),passenger.getPassengerId());
                count++;
            }
        }
        reducerString += "Flight ID:            "+key+"\r\n";
        reducerString += "Passengers on Flight: "+count;
        String[] options = {key,String.valueOf(count)};
        String rCSV = StaticClass.makeCSVRow(options);
        return new ReducerOuput(reducerString,rCSV);

    }



}
