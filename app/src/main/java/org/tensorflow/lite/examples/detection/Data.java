package org.tensorflow.lite.examples.detection;

public class Data {
    String data;
    String id;
    public  Data(){

    }
    public Data(String data,String id){
        this.data = data;
        this.id = id;
    }
    public String getData(){
        return data;
    }
    public String getId(){
        return id;
    }


}
