package id.ac.ukdw.ti.yuanlukito.wifiposition.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Yuan Lukito on 3/24/2015.
 */
public class DataSource {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public DataSource(Context context){
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        if (db.isOpen()) {
            db.close();
        }
    }

    public int getLocationId(String locationName){
        String[] args = {locationName};
        Cursor cursor = db.rawQuery("SELECT id_ruangan FROM location WHERE nama_ruangan = ?", args);
        int id = -1;
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            id = cursor.getInt(0);
        }
        cursor.close();
        return id;
    }

    public String getLocationName(int locationId){
        String[] args = {locationId + ""};
        Cursor cursor = db.rawQuery("SELECT nama_ruangan FROM location WHERE id_ruangan = ?", args);
        String name = "Unknown";
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            name = cursor.getString(0);
        }
        return name;
    }

    public String[] getLocationArray(){
        String[] allLocations = null;
        String[] column = new String[]{"nama_ruangan"};
        Cursor cursor = db.query(DatabaseHelper.LOCATION_TABLENAME, column, null, null, null, null, null);
        allLocations = new String[cursor.getCount()];
        cursor.moveToFirst();
        int i = 0;
        while(!cursor.isAfterLast()){
            allLocations[i] = cursor.getString(0);
            i++;
            cursor.moveToNext();
        }
        if(!cursor.isClosed())
            cursor.close();
        return allLocations;
    }

    public ArrayList<Location> getLocationList(){
        ArrayList<Location> locations = new ArrayList<>();
        String[] column = new String[]{"id_ruangan", "nama_ruangan"};
        Cursor cursor = db.query(DatabaseHelper.LOCATION_TABLENAME, column, null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Location loc = new Location(cursor.getInt(0), cursor.getString(1));
            locations.add(loc);
            cursor.moveToNext();
        }
        if(!cursor.isClosed())
            cursor.close();
        return locations;
    }

    public ArrayList<WifiStation> getAllWifiStations(){
        ArrayList<WifiStation> wifiStations = new ArrayList<>();
        final String[] columns = {"bssid", "ssid"};
        Cursor cursor = db.query(DatabaseHelper.WIFISTATION_TABLENAME, columns, null, null, null, null, null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                WifiStation ws = new WifiStation(cursor.getString(0), cursor.getString(1), -999, -999);
                wifiStations.add(ws);
                cursor.moveToNext();
            }
        }
        if(!cursor.isClosed())
            cursor.close();
        return wifiStations;
    }

    public long insertWifiStation(WifiStation wifiStation){
        ContentValues values = new ContentValues();
        values.put("bssid", wifiStation.getBssid());
        values.put("ssid", wifiStation.getSsid());
        values.put("frequency", wifiStation.getFrequency());
        return db.insert(DatabaseHelper.WIFISTATION_TABLENAME, null, values);
    }

    public boolean isWifiStationExist(WifiStation wifiStation){
        String[] args = {wifiStation.getBssid()};
        boolean exist = false;
        Cursor cursor = db.rawQuery("SELECT COUNT(bssid) FROM wifistation WHERE bssid = ?", args);
        cursor.moveToFirst();
        if(cursor.getInt(0) > 0)
            exist = true;
        return exist;
    }

    public boolean insertWifiData(int locationId, ArrayList<WifiStation> wifiStations){
        boolean result = false;
        ArrayList<WifiStation> allStations = getAllWifiStations();
        //insert to wifidata
        ContentValues values = new ContentValues();
        values.put("id_ruangan", locationId);
        long wifiDataId = db.insert(DatabaseHelper.WIFIDATA_TABLENAME, null, values);
        String message;
        if(wifiDataId != -1){
            //insert to wifidatadetail

            //iterate allStations
            for(WifiStation ws: allStations) {
                values.clear();
                values.put("id_wifidata", wifiDataId);
                values.put("bssid", ws.getBssid());
                values.put("ssid", ws.getSsid());
                if(wifiStations.contains(ws)) {
                    WifiStation w = wifiStations.get(wifiStations.indexOf(ws));
                    values.put("frequency", w.getFrequency());
                    values.put("level", w.getLevel());
                }
                else {
                    values.put("frequency", -1);
                    values.put("level", -999);
                }
                db.insert(DatabaseHelper.WIFIDATADETAIL_TABLENAME, null, values);
            }
            result = true;
        }
        return result;
    }

    public boolean deleteLocation(int locationId){
        String where = "id_ruangan = " + locationId;
        String args[] = null;
        int result = db.delete(DatabaseHelper.LOCATION_TABLENAME, where, args);
        return (result == 1);
    }

    public boolean deleteWifiStation(String bssid){
        String where = "bssid = ?";
        String args[] = {bssid};
        int result = db.delete(DatabaseHelper.WIFISTATION_TABLENAME, where, args);
        return (result == 1);
    }

    public HashMap<Location, ArrayList<WifiStation>> getAllWifiData(){
        HashMap<Location, ArrayList<WifiStation>> wifiData = new HashMap<>();
        //get all wifi data
        final String[] columns = {"id_data, id_ruangan, waktu_pengambilan"};
        final String[] columnsDetail = {"id_wifidata, bssid, ssid, frequency, level"};
        Cursor wifiDataCursor = db.query(DatabaseHelper.WIFIDATA_TABLENAME, columns, null, null, null, null, null);
        wifiDataCursor.moveToFirst();
        while(!wifiDataCursor.isAfterLast()){
            int locationId = wifiDataCursor.getInt(0);
            String locationName = getLocationName(locationId);
            Location location = new Location(locationId, locationName);
            Cursor wifiDataDetailCursor = db.query(DatabaseHelper.WIFIDATADETAIL_TABLENAME, columnsDetail, null, null, null, null, null);
            ArrayList<WifiStation> stations = new ArrayList<>();
            wifiDataDetailCursor.moveToFirst();
            while(!wifiDataDetailCursor.isAfterLast()){
                String bssid = wifiDataDetailCursor.getString(1);
                String ssid = wifiDataDetailCursor.getString(2);
                int frequency = wifiDataDetailCursor.getInt(3);
                int level = wifiDataDetailCursor.getInt(4);
                WifiStation ws = new WifiStation(bssid, ssid, frequency, level);
                stations.add(ws);
                wifiDataDetailCursor.moveToNext();
            }
            wifiData.put(location, stations);
            wifiDataCursor.moveToNext();
        }
        return wifiData;
    }
}
