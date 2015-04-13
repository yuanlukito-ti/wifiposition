package id.ac.ukdw.ti.yuanlukito.wifiposition.algorithm;

import java.util.List;

import id.ac.ukdw.ti.yuanlukito.wifiposition.model.Location;
import id.ac.ukdw.ti.yuanlukito.wifiposition.model.WifiStation;

/**
 * Created by Yuan Lukito on 3/27/2015.
 */
public class NaiveBayesAlgorithm extends Algorithm{

    public NaiveBayesAlgorithm(){
        super("Naive Bayes Algorithm");
    }

    @Override
    public void setUp() {

    }

    @Override
    public Location getCurrentLocation(List<WifiStation> wifiStations) {
        return new Location(0, "Incomplete implementation!");
    }
}
