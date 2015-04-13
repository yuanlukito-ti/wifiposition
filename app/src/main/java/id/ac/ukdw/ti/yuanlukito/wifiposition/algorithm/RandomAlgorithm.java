package id.ac.ukdw.ti.yuanlukito.wifiposition.algorithm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import id.ac.ukdw.ti.yuanlukito.wifiposition.model.DataSource;
import id.ac.ukdw.ti.yuanlukito.wifiposition.model.Location;
import id.ac.ukdw.ti.yuanlukito.wifiposition.model.WifiStation;

/**
 * Created by Yuan Lukito on 3/26/2015.
 */
public class RandomAlgorithm extends Algorithm {

    public RandomAlgorithm(){
        super("Random Algorithm by Yuan Lukito");
    }

    @Override
    public void setUp() {

    }

    @Override
    public Location getCurrentLocation(List<WifiStation> wifiStations) {
        //get datasource
        DataSource ds = getDataSource();

        //default: not found / unknown location
        Location result = new Location(-99, "Unknown");

        //get locations list, take one location based on a random index
        try {
            ds.open();
            ArrayList<Location> locations = ds.getLocationList();
            Random random = new Random();
            int indexResult = random.nextInt(locations.size());
            result = locations.get(indexResult);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //yeah, that's fast!
        return result;
    }
}
