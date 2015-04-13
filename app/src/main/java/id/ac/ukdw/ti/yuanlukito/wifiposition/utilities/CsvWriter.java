package id.ac.ukdw.ti.yuanlukito.wifiposition.utilities;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import id.ac.ukdw.ti.yuanlukito.wifiposition.model.DataSource;
import id.ac.ukdw.ti.yuanlukito.wifiposition.model.Location;
import id.ac.ukdw.ti.yuanlukito.wifiposition.model.WifiStation;

/**
 * Created by Yuan Lukito on 3/25/2015.
 */
public class CsvWriter {
    //separator
    private static final String CSV_SEPARATOR = ",";

    //predefined instance
    private static CsvWriter writer = new CsvWriter();

    //private constructor - singleton
    private CsvWriter(){  }

    //get instance
    public static CsvWriter getInstance(){
        return writer;
    }

    private String currentTimeStamp(){
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    //write location data
    public boolean writeLocation() {
        boolean result = false;
        DataSource dataSource = new DataSource(ApplicationContextProvider.getContext());
        try {
            dataSource.open();
            ArrayList<Location> locations = dataSource.getLocationList();
            String currentTime = currentTimeStamp();
            File locationsFile = new File(Environment.getExternalStorageDirectory() + File.separator + "locations-" + currentTime + ".csv");
            locationsFile.createNewFile();
            final FileOutputStream fos = new FileOutputStream(locationsFile);
            final OutputStreamWriter osw = new OutputStreamWriter(fos);
            Log.d("WIPOS", "Export Location Data to " + locationsFile.getAbsolutePath());
            osw.append("id" + CSV_SEPARATOR + "nama");
            osw.append("\n");
            for(Location loc: locations){
                osw.append(loc.getId() + CSV_SEPARATOR + loc.getName());
                osw.append("\n");
            }
            dataSource.close();
            osw.close();
            fos.close();
            result = true;
        } catch (SQLException e) {

        } catch (IOException e) {

        }
        return result;
    }

    public boolean writeWifiStation(){
        boolean result = false;
        DataSource dataSource = new DataSource(ApplicationContextProvider.getContext());
        try {
            dataSource.open();
            ArrayList<WifiStation> wifiStations = dataSource.getAllWifiStations();
            String currentTime = currentTimeStamp();
            File wifiStationsFile = new File(Environment.getExternalStorageDirectory() + File.separator + "wifistations-" + currentTime + ".csv");
            wifiStationsFile.createNewFile();
            final FileOutputStream fos = new FileOutputStream(wifiStationsFile);
            final OutputStreamWriter osw = new OutputStreamWriter(fos);
            Log.d("WIPOS", "Export Wifi Station Data to " + wifiStationsFile.getAbsolutePath());
            osw.append("bssid" + CSV_SEPARATOR + "ssid");
            osw.append("\n");
            for(WifiStation ws: wifiStations){
                osw.append(ws.getBssid() + CSV_SEPARATOR + ws.getSsid());
                osw.append("\n");
            }
            dataSource.close();
            osw.close();
            fos.close();
            result = true;
        } catch (SQLException e) {

        } catch (IOException e) {

        }
        return result;
    }
}
