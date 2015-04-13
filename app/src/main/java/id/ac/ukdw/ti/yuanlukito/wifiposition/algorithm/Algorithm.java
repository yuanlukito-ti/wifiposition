package id.ac.ukdw.ti.yuanlukito.wifiposition.algorithm;

import java.util.ArrayList;
import java.util.List;

import id.ac.ukdw.ti.yuanlukito.wifiposition.model.DataSource;
import id.ac.ukdw.ti.yuanlukito.wifiposition.model.DatabaseHelper;
import id.ac.ukdw.ti.yuanlukito.wifiposition.model.Location;
import id.ac.ukdw.ti.yuanlukito.wifiposition.model.WifiStation;
import id.ac.ukdw.ti.yuanlukito.wifiposition.utilities.ApplicationContextProvider;

/**
 * Created by Yuan Lukito on 3/24/2015.
 */
public abstract class Algorithm {
    private final String algorithmName;
    private DataSource datasource;

    public Algorithm(String algorithmName){
        this.algorithmName = algorithmName;
    }

    public String getAlgorithmName(){
        return algorithmName;
    }

    //data access
    protected DataSource getDataSource(){
        if(datasource == null)
            datasource = new DataSource(ApplicationContextProvider.getContext());
        return datasource;
    }

    //set up
    public abstract void setUp();

    //calculate position
    public abstract Location getCurrentLocation(List<WifiStation> wifiStations);
}
