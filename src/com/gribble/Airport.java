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

    /**
     * Constructor for Airport
     *
     * @param name  name of airport
     * @param code  airport code
     * @param lat   latitude
     * @param lon   longitude
     */
    public Airport(String name, String code, Float lat, Float lon){
        this.airportName = name;
        this.airportCode = code;
        this.latitude = lat;
        this.longitude = lon;
    }

    /**
     * Get airport name
     * @return
     */
    public String getAirportName() {
        return airportName;
    }


}
