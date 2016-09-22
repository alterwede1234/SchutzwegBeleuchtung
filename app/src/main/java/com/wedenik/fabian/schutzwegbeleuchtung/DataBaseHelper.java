package com.wedenik.fabian.schutzwegbeleuchtung;

/**
 * Created by fabian on 19.09.16.
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper
{
    private static String TAG = "DataBaseHelper"; // Tag just for the LogCat window
    //destination path (location) of our database on device
    public static final String DB_PATH = "/data/data/com.wedenik.fabian.schutzwegbeleuchtung/databases/";
    public static final String DB_NAME ="android_database.sqlite";// Database name
    private SQLiteDatabase mDataBase;
    private final Context mContext;

    public DataBaseHelper(Context context)
    {
        super(context, DB_NAME, null, 1);// 1? Its database Version
        this.mContext = context;
    }

    public boolean copyDataBase(Context context)
    {
        try {
            InputStream mInput = context.getAssets().open(DB_NAME);
            String outFileName = DB_PATH + DB_NAME;
            OutputStream mOutput = new FileOutputStream(outFileName);
            byte[] mBuffer = new byte[1024];
            int mLength;
            while ((mLength = mInput.read(mBuffer))>0)
            {
                mOutput.write(mBuffer, 0, mLength);
            }
            mOutput.flush();
            mOutput.close();
            mInput.close();
            Log.w("Eingabe","DB copied");
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void openDatabase()
    {
        String dbPath = mContext.getDatabasePath(DB_NAME).getPath();
        if(mDataBase != null && mDataBase.isOpen())
        {
            return;
        }
        mDataBase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    //Check that the database exists here: /data/data/your package/databases/Da Name
    private boolean checkDataBase()
    {
        File dbFile = new File(DB_PATH + DB_NAME);
        //Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    }




    @Override
    public synchronized void close()
    {
        if(mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase mDataBase){

    }

    @Override
    public void onUpgrade(SQLiteDatabase mDataBase, int oldVersion, int newVersion) {

    }

    public List<String> getLaender() {
        List<String> Laender = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDataBase.rawQuery("SELECT * FROM android_metadata", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String Land = cursor.getString(0);
            Laender.add(Land);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return Laender;
    }
    public List<String> getLaenge(String land) {
        List<String> Laenge = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDataBase.rawQuery("SELECT * FROM android_metadata WHERE Land='"+land+"'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String laenge = cursor.getString(1);
            Laenge.add(laenge);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return Laenge;
    }
    public List<String> getBreite(String land, String laenge) {
        List<String> Breite = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDataBase.rawQuery("SELECT * FROM android_metadata WHERE Land='"+land+"' AND Laenge='"+laenge+"'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String breite = cursor.getString(2);
            Breite.add(breite);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return Breite;
    }
    public List<String> getModelle(String land, String laenge, String breite) {
        List<String> Leuchtenmodell = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDataBase.rawQuery("Select * FROM android_metadata WHERE Land='"+land+"' AND Laenge='"+laenge+"' AND Breite='"+breite+"'",null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String leuchtenmodell = cursor.getString(3);
            Leuchtenmodell.add(leuchtenmodell);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return Leuchtenmodell;
    }
    public List<String> getLeuchten(String land, String laenge, String breite, String modell) {
        List<String> Leuchten = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDataBase.rawQuery("Select * FROM android_metadata WHERE Land='"+land+"' AND Laenge='"+laenge+"' AND Breite='"+breite+"' AND Leuchtenmodell='"+modell+"'",null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String leuchte = cursor.getString(4);
            Leuchten.add(leuchte);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return Leuchten;
    }
    public List<String> getHoehen(String land, String laenge, String breite, String modell, String leuchte) {
        List<String> Hoehen = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDataBase.rawQuery("Select * FROM android_metadata WHERE Land='"+land+"' AND Laenge='"+laenge+"' AND Breite='"+breite+"' AND Leuchtenmodell='"+modell+"' AND Leuchte='"+leuchte+"'",null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String hoehe = cursor.getString(5);
            Hoehen.add(hoehe);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return Hoehen;
    }
    public String getAbstand(String land, String laenge, String breite, String modell, String leuchte, String hoehe){
        openDatabase();
        Cursor cursor = mDataBase.rawQuery("Select * FROM android_metadata WHERE Land='"+land+"' AND Laenge='"+laenge+"' AND Breite='"+breite+"' AND Leuchtenmodell='"+modell+"' AND Leuchte='"+leuchte+"' AND Hoehe='"+hoehe+"'",null);
        cursor.moveToFirst();
        String abstand = cursor.getString(6);
        cursor.close();
        close();
        return abstand;
    }
}