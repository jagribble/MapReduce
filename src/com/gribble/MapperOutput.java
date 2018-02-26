package com.gribble;

public class MapperOutput {
    private String key;
    private Object value;

    /**
     * constructor for mapper output (key-value pair)
     *
     * @param key   key
     * @param value value
     */
    public MapperOutput(String key,Object value){
        this.key = key;
        this.value = value;
    }


    /**
     * Get the key
     *
     * @return
     */
    public String getKey() {
        return key;
    }

    /**
     * Get the Value
     *
     * @return
     */
    public Object getValue() {
        return value;
    }

}
