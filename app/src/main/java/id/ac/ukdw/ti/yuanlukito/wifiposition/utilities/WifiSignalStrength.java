package id.ac.ukdw.ti.yuanlukito.wifiposition.utilities;

/**
 * Created by Yuan Lukito on 3/26/2015.
 */
public class WifiSignalStrength {
    public static int levelToQuality(int level){
        if(level <= -100)
            return 0;
        else if(level >= -50)
            return 100;
        else
            return 2 * (100 + level);
    }
}
