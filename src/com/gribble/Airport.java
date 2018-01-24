package com.gribble;

public class Airport {

    /**
     * Airport name: Format: 𝑋 [3. .20]
     * Airport IATA/FAA code:  Format: 𝑋𝑋𝑋
     * Latitude: Format: 𝑛. 𝑛 [3. .13]
     * Longitude: Format: 𝑛. 𝑛 [3. .13]
     **/

    private String airportName;
    private String airportCode;
    private Float latitude;
    private Float longitude;

    public Airport(String name, String code, Float lat, Float lon){
        this.airportName = name;
        this.airportCode = code;
        this.latitude = lat;
        this.longitude = lon;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }
}
