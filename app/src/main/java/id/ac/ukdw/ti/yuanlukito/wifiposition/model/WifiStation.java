package id.ac.ukdw.ti.yuanlukito.wifiposition.model;

/**
 * Created by Yuan Lukito on 3/24/2015.
 */
public class WifiStation {
    private final String bssid;
    private final String ssid;
    private final int frequency;
    private final int level;
    private boolean isOnDB = false;

    public WifiStation(String bssid, String ssid, int frequency, int level){
        this.bssid = bssid;
        this.ssid = ssid;
        this.frequency = frequency;
        this.level = level;
    }

    public void setIsOnDB(boolean isOnDB){
        this.isOnDB = isOnDB;
    }

    public boolean getIsOnDB(){
        return isOnDB;
    }

    public String getBssid() {
        return bssid;
    }

    public String getSsid() {
        return ssid;
    }

    public int getFrequency() {
        return frequency;
    }

    public int getLevel() {
        return level;
    }

    public String toString(){
        return "BSSID: " + bssid + ", SSID: " + ssid + ", Frequency: " + frequency + ", Level: " + level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WifiStation that = (WifiStation) o;

        return bssid.equalsIgnoreCase(that.bssid);
    }

    @Override
    public int hashCode() {
        return bssid.hashCode();
    }
}
