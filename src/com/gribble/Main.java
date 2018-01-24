package com.gribble;

import java.io.*;
import java.util.ArrayList;

public class Main {

    public static ArrayList<String> lines = new ArrayList<String>();

    public static void getAirportData(){
        // read in file
        File airpotData = new File("Top30_airports_LatLong.csv");
        System.out.println(airpotData);

    }

    public static void getPassangerData(){
        // read in file
        File passangerData = new File("./src/AComp_Passenger_data.csv");
        System.out.println(passangerData);
        // store each line in array object
        String line = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(passangerData));
            while((line = reader.readLine()) != null ){
                System.out.println(line);
                lines.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void mapper(){

    }

    public void reducer(){

    }

    public static void main(String[] args) {
	    // write your code here
        getAirportData();
        getPassangerData();


    }
}
