package id.ac.ukdw.ti.yuanlukito.wifiposition.model;

/**
 * Created by Yuan Lukito on 3/24/2015.
 */
public class Location {
    private int id;
    private String name;

    public Location(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String toString(){
        return name;
    }
}
