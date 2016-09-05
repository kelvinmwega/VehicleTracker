package org.tracker.ibmiot.vehicletracker.pojos;

/**
 * Created by Kelvin on 7/22/2016.
 */
public class vehicleData {

    int ID;
    String deviceName;
    String latt;
    String lont;
    String speed;
    String dateTime;

    public vehicleData(){};

    public vehicleData(int ID, String deviceName,String latt, String lont, String speed, String dateTime){
        this.ID = ID;
        this.deviceName = deviceName;
        this.latt = latt;
        this.lont = lont;
        this.speed = speed;
        this.dateTime = dateTime;
    };

    public vehicleData(String deviceName,String latt, String lont, String speed, String dateTime){
        this.deviceName = deviceName;
        this.latt = latt;
        this.lont = lont;
        this.speed = speed;
        this.dateTime = dateTime;
    }

    public int getID(){
        return this.ID;
    }

    public void setID(int ID){
        this.ID = ID;
    }

    public String getDeviceName(){
        return this.deviceName;
    }

    public void setDeviceName(String deviceName){
        this.deviceName = deviceName;
    }

    public String getLatt(){
        return this.latt;
    }

    public void setLatt(String latt){
        this.latt = latt;
    }

    public String getLont(){
        return this.lont;
    }

    public void setLont(String lont){
        this.lont = lont;
    }

    public String getSpeed(){
       return this.speed;
    }

    public void setSpeed(String speed){
        this.speed = speed;
    }

    public String getDateTime(){
        return dateTime;
    }

    public void setDateTime(String dateTime){
        this.dateTime = dateTime;
    }

}
