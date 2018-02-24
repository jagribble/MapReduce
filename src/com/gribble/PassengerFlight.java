package com.gribble;

import java.util.Date;


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
    protected Boolean error = false;
    protected String errorMessage;

    public PassengerFlight(String pId,String fId, String sAirport, String dAirport, String dTime, int fTime){
        this.passengerId = pId;
        this.flightId = fId;
        this.sourceAirport = sAirport;
        this.destinationAirport = dAirport;
        this.depatureTimeString = dTime;
        this.depatureTime = new Date(Integer.valueOf(dTime));
        this. flightTime = fTime;
        checkValidation();
    }

    private void checkValidation(){
        char[] pId = this.passengerId.toCharArray();
        char[] fId = this.flightId.toCharArray();
        char[] sAirport = this.sourceAirport.toCharArray();
        char[] dAirport = this.destinationAirport.toCharArray();
        char[] dTime = this.depatureTimeString.toCharArray();
        String errorString = "";
        // check validty of each varible according to their syntax.
        // If true then invalid
        Boolean passenger = checkValid(passengerSyntax,pId);
        Boolean flight = checkValid(flightSyntax,fId);
        Boolean source = checkValid(sourceAirportSyntax,sAirport);
        Boolean destination = checkValid(destinationAirportSyntax,dAirport);
        Boolean depatureTime = checkValid(departureTimeSyntax,dTime);
        if(passenger || flight || source || destination || depatureTime){
            System.err.println("Error: Syntax Error with Passenger Flight");
            this.error = true;
            this.errorMessage = "Error: Syntax Error with Passenger Flight";
        }

    }

    Boolean checkValid(Syntax[] accepted ,char[] values){
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

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getSourceAirport() {
        return sourceAirport;
    }

    public void setSourceAirport(String sourceAirport) {
        this.sourceAirport = sourceAirport;
    }

    public String getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(String destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public Date getDepatureTime() {
        return depatureTime;
    }

    public void setDepatureTime(Date depatureTime) {
        this.depatureTime = depatureTime;
    }

    public int getFlightTime() {
        return flightTime;
    }

    public void setFlightTime(int flightTime) {
        this.flightTime = flightTime;
    }
}
