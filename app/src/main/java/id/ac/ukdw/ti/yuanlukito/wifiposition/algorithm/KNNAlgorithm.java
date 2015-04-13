package id.ac.ukdw.ti.yuanlukito.wifiposition.algorithm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.ac.ukdw.ti.yuanlukito.wifiposition.model.DataSource;
import id.ac.ukdw.ti.yuanlukito.wifiposition.model.Location;
import id.ac.ukdw.ti.yuanlukito.wifiposition.model.WifiStation;

/**
 * Created by Yuan Lukito on 3/27/2015.
 */
public class KNNAlgorithm extends Algorithm {
    private HashMap<Location, ArrayList<WifiStation>> wifiData;
    private DataSource dataSource;
    public KNNAlgorithm(){
        super("K-Nearest Neighbors Algorithm");
    }

    @Override
    public void setUp() {
        wifiData = new HashMap<>();

        //get all required data
        dataSource = getDataSource();
        try {
            dataSource.open();
            wifiData = dataSource.getAllWifiData();
            dataSource.close();
        } catch (SQLException e) {

        }
    }

    @Override
    public Location getCurrentLocation(List<WifiStation> wifiStations) {
        Location result = new Location(-99, "Unknown Location");

        return new Location(0, "Incomplete implementation!");
    }
}
