package com.gribble;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Static class for static functions and variables
 */
public class StaticClass {

    public static HashMap<String,Airport> airportHashMap = new HashMap<String, Airport>();
    public static ArrayList<String> objective1Airports = new ArrayList<String>();
    public static HashMap<String,String> passengerObjective3Hash = new HashMap<String, String>();
    public static HashMap<String,String> passengerObjective2Hash = new HashMap<String, String>();
    public static String objective1CSVString = "";
    public static String objective2CSVString = "";
    public static String objective3CSVString = "";
    public static String objective1TextString = "";
    public static String objective2TextString = "";
    public static String objective3TextString = "";
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

    /**
     * shuffle and sorts the mapper outputs
     *
     * @param mapperOutput  list of mapper outputs
     * @param hashMap       existing hashmap to add values too
     * @return
     */
    static HashMap<String,ArrayList<Object>> shuffler(ArrayList<MapperOutput> mapperOutput,HashMap<String, ArrayList<Object>> hashMap){

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


    /**
     * Split the lines of data into smaller chunks
     *
     * @param bigList   list of all rows
     * @param n         number of rows in each chunk
     * @return
     */
    public static ArrayList<ArrayList<String>> getChunks(ArrayList<String> bigList, int n){
        ArrayList<ArrayList<String>> chunks = new ArrayList<ArrayList<String>>();

        for (int i = 0; i < bigList.size(); i += n) {
            ArrayList<String> chunk = new ArrayList<String>(bigList.subList(i, Math.min(bigList.size(), i + n)));
            chunks.add(chunk);
        }

        return chunks;
    }

    /**
     * Calculate which airports are missing from objective 1
     *
     * @return airports missing
     */
    static ReducerOuput missingAirports(){
        ArrayList<String> allAirports = new ArrayList<String>(StaticClass.airportHashMap.keySet());
        for (int x=0;x<StaticClass.objective1Airports.size();x++){
            allAirports.remove(StaticClass.objective1Airports.get(x));
        }
        String reducerString = "Missing Airports:\n";
        String reducerCSV = "";
        for(int x=0;x<allAirports.size();x++){
            reducerString += "          "+allAirports.get(x)+","+
                    StaticClass.airportHashMap.get(allAirports.get(x)).getAirportName()+"\r\n";
            reducerCSV += allAirports.get(x) +","+StaticClass.airportHashMap.get((allAirports.get(x))).getAirportName()+"\n";
        }
        ReducerOuput reducerOuput = new ReducerOuput(reducerString,reducerCSV);
        return reducerOuput;
    }

    /**
     * Create a csv from the input
     *
     * @param name      name of file
     * @param csvString csv string
     */
    static void createCSV(String name,String csvString){
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(name);

            fileWriter.append(csvString);

            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }

        }
    }

    /**
     * Create a csvString from the inputs
     *
     * @param headings  headings for the CSV
     * @param reducerOuputs outputs from the reducer
     * @return
     */
    static String createCSVString(String[] headings, ArrayList<ReducerOuput> reducerOuputs){
        String csvString = "";
        csvString += StaticClass.makeCSVRow(headings);
        for (int x=0;x<reducerOuputs.size();x++){
            csvString += reducerOuputs.get(x).reducerCSV;
        }
        return csvString;
    }

    /**
     * Create a csvString from the inputs with additional data
     *
     * @param headings  headings for the CSV
     * @param reducerOuputs outputs from the reducer
     * @param additionalHeadings    additional heading
     * @param additionalData    additional data
     * @return
     */
    static String createCSVString(String[] headings, ArrayList<ReducerOuput> reducerOuputs,String[] additionalHeadings, String additionalData){
        String csvString = "";
        csvString += StaticClass.makeCSVRow(headings);
        for (int x=0;x<reducerOuputs.size();x++){
            csvString += reducerOuputs.get(x).reducerCSV;
        }

        csvString+="\n\n";
        csvString+=StaticClass.makeCSVRow(additionalHeadings);
        csvString+=additionalData;
        return csvString;
    }

    /**
     * Make text file from inputs
     *
     * @param nameOfFile    name of file
     * @param output    string to output to file
     */
    static void makeTxtFile(String nameOfFile,String output){
        BufferedWriter fileWriter = null;
        try {
            fileWriter = new BufferedWriter(new FileWriter(nameOfFile));


            fileWriter.append(output);

            System.out.println("TXT file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in Txt file writer !!!");
            e.printStackTrace();
        } finally {

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }

        }
    }

    /**
     * Make a csv row from the values given
     *
     * @param values    values to be made into csv row
     * @return
     */
    static String makeCSVRow(String[] values){
        String csvString = "";
        for(int x=0;x<values.length;x++){
            csvString += values[x] +",";
        }
        csvString += "\n";
        return csvString;
    }

}
