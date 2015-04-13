package id.ac.ukdw.ti.yuanlukito.wifiposition.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Yuan Lukito on 3/20/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    //database name & version
    private static final String DATABASE_NAME = "wifidb";
    private static final int DATABASE_VERSION = 4;

    public static final String LOCATION_TABLENAME = "location";
    public static final String WIFISTATION_TABLENAME = "wifistation";
    public static final String WIFIDATA_TABLENAME = "wifidata";
    public static final String WIFIDATADETAIL_TABLENAME = "wifidatadetail";

    //create table
    private static final String CREATE_TABLE_LOCATION
            = "CREATE TABLE " + LOCATION_TABLENAME +"(id_ruangan INTEGER PRIMARY KEY, nama_ruangan TEXT)";
    private static final String CREATE_TABLE_WIFISTATION
            = "CREATE TABLE " + WIFISTATION_TABLENAME + "(bssid TEXT PRIMARY KEY, ssid TEXT, frequency INTEGER)";
    private static final String CREATE_TABLE_WIFIDATA
            = "CREATE TABLE " + WIFIDATA_TABLENAME + "(id_data INTEGER PRIMARY KEY AUTOINCREMENT, id_ruangan INTEGER, waktu_pengambilan DATETIME DEFAULT CURRENT_TIMESTAMP)";
    private static final String CREATE_TABLE_WIFIDATADETAIL
            = "CREATE TABLE " + WIFIDATADETAIL_TABLENAME +  "(id_detail INTEGER PRIMARY KEY AUTOINCREMENT, id_wifidata INTEGER, bssid TEXT, ssid TEXT, frequency INTEGER, level INTEGER)";

    public static final String[] LOCATION_NAME_ARRAY = {"Fakultas Teologi", "Fakultas Teknologi Informasi",
            "Fakultas Bioteknologi", "Fakultas Arsitektur dan Desain",
            "Fakultas Bisnis", "Perpustakaan", "Lembaga Penelitian dan Pengabdian Masyarakat (LPPM)",
            "Kerumahtanggaan", "Rektorat", "PPP", "PPKPK", "Pusat Pelatihan Bahasa",
            "Biro Akademik", "Biro Keuangan", "Biro Kemahasiswaan",
            "Audit Internal", "Pusat Sumber Daya Manusia (PSDM)", "Yayasan",
            "PPTPM", "Puspelkom", "Humas dan Admisi", "Toko Buku UKDW", "Koperasi",
            "Poliklinik", "Bank BRI", "Bank BNI", "Bank BPD", "Puspindika",
            "Pendeta Universitas", "Pojok BEJ", "Lab Teknik Informatika",
            "Office of International Affairs (OIA)", "Lab Sistem Informasi",
            "Auditorium", "Kantin", "Atrium Didaktos", "Taman Firdaus", "Ruang Harun",
            "Ruang Tasdik", "Ruang Vicon", "Ruang Monev", "Jembatan Golden Bridge", "Atrium Agape"};

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LOCATION);
        db.execSQL(CREATE_TABLE_WIFIDATA);
        db.execSQL(CREATE_TABLE_WIFIDATADETAIL);
        db.execSQL(CREATE_TABLE_WIFISTATION);

        //insert data ruangan ke tabel location
        for(int i=0; i<LOCATION_NAME_ARRAY.length; i++){
            ContentValues values = new ContentValues();
            values.put("id_ruangan", i+1);
            values.put("nama_ruangan", LOCATION_NAME_ARRAY[i]);
            db.insert("LOCATION", null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS LOCATION");
        db.execSQL("DROP TABLE IF EXISTS WIFISTATION");
        db.execSQL("DROP TABLE IF EXISTS WIFIDATA");
        db.execSQL("DROP TABLE IF EXISTS WIFIDATADETAIL");

        onCreate(db);
    }
}