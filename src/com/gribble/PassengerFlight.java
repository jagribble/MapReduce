package com.gribble;

import java.util.Date;

public class PassengerFlight {

    /**
     * Passenger id: Format: 𝑋𝑋𝑋𝑛𝑛𝑛𝑛𝑋𝑋n
     * Flight id: Format: 𝑋𝑋𝑋𝑛𝑛𝑛𝑛X
     * From airport IATA/FAA code: Format: XXX
     * Destination airport IATA/FAA code: Format: XXX
     * Departure time (GMT):  Format: 𝑛 [10] (This is in Unix ‘epoch’ time)
     * Total flight time (mins): Format: 𝑛 [1. .4]
     **/

    private String passengerId;
    private String flightId;
    private String sourceAirport;
    private String destinationAirport;
    private Date depatureTime;
    private int flightTime;

    public PassengerFlight(String pId, String sAirport, String dAirport, Date dTime, int fTime){
        this.passengerId = pId;
        this.sourceAirport = sAirport;
        this.destinationAirport = dAirport;
        this.depatureTime = dTime;
        this. flightTime = fTime;
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
