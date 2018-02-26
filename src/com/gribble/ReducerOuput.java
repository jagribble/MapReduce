package com.gribble;

public class ReducerOuput {
    protected String reducerString;
    protected String reducerCSV;

    /**
     * Constructor for ReducerOutput
     *
     * @param rString   reducer String for text file
     * @param rCSV  reducer csv string for csv file
     */
    public ReducerOuput(String rString,String rCSV){
        this.reducerString = rString;
        this.reducerCSV = rCSV;
    }


}
