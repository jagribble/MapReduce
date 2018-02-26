package com.gribble;

import java.util.Date;

/**
 * Class to store information about a passenger flight
 */
public class PassengerFlight {

    /**
     * Passenger id:                           Format: 𝑋𝑋𝑋𝑛𝑛𝑛𝑛𝑋𝑋𝑛
     * Flight id:                              Format: 𝑋𝑋𝑋𝑛𝑛𝑛𝑛𝑋
     * From airport IATA/FAA code:             Format: 𝑋𝑋𝑋
     * Destination airport IATA/FAA code:      Format: 𝑋𝑋𝑋
     * Departure time (GMT):                   Format: 𝑛[10] (This is in Unix ‘epoch’ time)
     * Total flight time (mins):               Format: 𝑛[1. .4]
     **/
    private Syntax[] passengerSyntax = {Syntax.CAPATIAL_CASE,Syntax.CAPATIAL_CASE,Syntax.CAPATIAL_CASE,Syntax.NUMBER,
                            Syntax.NUMBER,Syntax.NUMBER,Syntax.NUMBER,Syntax.CAPATIAL_CASE,Syntax.CAPATIAL_CASE,Syntax.NUMBER};
    private Syntax[] flightSyntax = {Syntax.CAPATIAL_CASE,Syntax.CAPATIAL_CASE,Syntax.CAPATIAL_CASE,Syntax.NUMBER,
            Syntax.NUMBER,Syntax.NUMBER,Syntax.NUMBER,Syntax.CAPATIAL_CASE};
    private Syntax[] sourceAirportSyntax = {Syntax.CAPATIAL_CASE,Syntax.CAPATIAL_CASE,Syntax.CAPATIAL_CASE};
    private Syntax[] destinationAirportSyntax = {Syntax.CAPATIAL_CASE,Syntax.CAPATIAL_CASE,Syntax.CAPATIAL_CASE};
    private Syntax[] departureTimeSyntax = {Syntax.NUMBER,Syntax.NUMBER,Syntax.NUMBER,Syntax.NUMBER,Syntax.NUMBER,
            Syntax.NUMBER,Syntax.NUMBER,Syntax.NUMBER,Syntax.NUMBER,Syntax.NUMBER};
    private String passengerId;
    private String flightId;
    private String sourceAirport;
    private String destinationAirport;
    private Date depatureTime;
    private String depatureTimeString;
    private int flightTime;
    private float flightTimeHours;
    private Date arraivalTime;
    protected Boolean error = false;
    protected Boolean errorCorretion = false;
    protected String errorMessage;


    /**
     * PassengerFlight Constructor
     *
     *
     * @param pId   passengerId
     * @param fId   FlightId
     * @param sAirport  source airport
     * @param dAirport  destination airport
     * @param dTime destination time
     * @param fTime flight time
     */
    public PassengerFlight(String pId,String fId, String sAirport, String dAirport, String dTime, int fTime){
        this.passengerId = pId;
        this.flightId = fId;
        this.sourceAirport = sAirport;
        this.destinationAirport = dAirport;
        this.depatureTimeString = dTime;
        this.depatureTime = new Date(Long.valueOf(dTime)*1000);
        this.flightTime = fTime;
        checkValidation();
        this.flightTimeHours = (float) Math.floor(this.flightTime/60);
        int minutues = (int) Math.floor(((this.flightTime - this.flightTimeHours)/60)/60);
        long arrrivalMili = this.depatureTime.getTime() + ((this.flightTime * 60) *1000);
        this.arraivalTime = new Date(arrrivalMili);
    }

    /**
     * Check if instance is valid
     */
    private void checkValidation(){
        char[] pId = this.passengerId.toCharArray();
        char[] fId = this.flightId.toCharArray();
        char[] sAirport = this.sourceAirport.toCharArray();
        char[] dAirport = this.destinationAirport.toCharArray();
        char[] dTime = this.depatureTimeString.toCharArray();
        String fTime = String.valueOf(this.flightTime);
        String errorString = "";
        // check validty of each varible according to their syntax.
        // If true then invalid
        Boolean passenger = checkInValid(passengerSyntax,pId);
        Boolean flight = checkInValid(flightSyntax,fId);
        Boolean source = checkInValid(sourceAirportSyntax,sAirport);
        Boolean destination = checkInValid(destinationAirportSyntax,dAirport);
        Boolean depatureTime = checkInValid(departureTimeSyntax,dTime);
        Boolean flightTime = false;
        if(fTime.length()<1 || fTime.length()>4){
            flightTime = true;
        }
        if(passenger || flight || source || destination || depatureTime || flightTime){
            String errorWith = "";
            // if each varible is true add the column name to the error with to output what columns are errored
            if(passenger){
                int orginalLength = this.passengerId.length();
                this.passengerId = this.passengerId.replaceAll("\\P{Print}","");
                int replacementLenth = this.passengerId.length();
                // if length has changed it has found and unreadable character and replaced it
                if(orginalLength != replacementLenth && !checkInValid(passengerSyntax,this.passengerId.toCharArray())){
                    this.errorCorretion = true;
                    this.errorMessage = "Unreadable character detected and Corrected "+this.passengerId;
                }
                errorWith+=", PassengerID";
            }
            if(flight){
                errorWith+=", FlightID";
            }
            if(source){
                errorWith+=", Source Airport";
            }
            if(destination){
                errorWith+=", destination Airport";
            }
            if(depatureTime){
                errorWith+=", depature Time";
            }
            if(flightTime){
                errorWith+=", Flight Time";
            }
            if(!this.errorCorretion){
                System.err.println("Error: Syntax Error with Passenger Flight;"+errorWith);
                this.error = true;
                this.errorMessage = "Error: Syntax Error with Passenger Flight;"+errorWith;
            }

        }

    }

    /**
     * Check if the value is valid
     *
     * @param accepted  accepted values using Syntax enum
     * @param values    value
     * @return  if true then invalid
     */
    Boolean checkInValid(Syntax[] accepted ,char[] values){
        for(int x=0;x<values.length;x++){
            switch (accepted[x]){
                case CAPATIAL_CASE:
                    if(!Character.isUpperCase(values[x])){
                        return true;
                    }
                    break;
                case LOWER_CASE:
                    if(!Character.isLowerCase(values[x])){
                        return true;
                    }
                    break;
                case NUMBER:
                    if(!Character.isDigit(values[x])){
                        return true;
                    }
                    break;
                case WHITESPACE:
                    if(!Character.isWhitespace(values[x])){
                        return true;
                    }
                    break;

            }
        }
        return false;
    }

    /**
     * Get Passenger ID
     * @return  PassengerID
     */
    public String getPassengerId() {
        return passengerId;
    }

    /**
     * Get Source Airport
     *
     * @return  Source Airport
     */
    public String getSourceAirport() {
        return sourceAirport;
    }


    /**
     * Get Destination Airport
     * @return  Destination Airport
     */
    public String getDestinationAirport() {
        return destinationAirport;
    }


    /**
     * Get departure time
     * @return departure time
     */
    public Date getDepatureTime() {
        return depatureTime;
    }


    /**
     * Get Flight Time
     * @return flight time
     */
    public int getFlightTime() {
        return flightTime;
    }


    /**
     * Get arrival date
     * @return  arrival date
     */
    public Date getArraivalTime() {
        return arraivalTime;
    }
}
