package com.wedenik.fabian.schutzwegbeleuchtung;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabian on 22.09.16.
 */
public class DataSheetHelper extends SQLiteOpenHelper {
    private static String TAG = "DataSheetHelper"; // Tag just for the LogCat window
    //destination path (location) of our database on device
    public static final String DB_PATH = "/data/data/com.wedenik.fabian.schutzwegbeleuchtung/databases/";
    public static final String DB_NAME ="datasheets.sqlite";// Database name
    private SQLiteDatabase mDataBase;
    private final Context mContext;

    public DataSheetHelper(Context context)
    {
        super(context, DB_NAME, null, 1);// 1? Its database Version
        this.mContext = context;
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

    public ArrayList<String> getSprachen(String leuchte){
        ArrayList<String> sprachen = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDataBase.rawQuery("SELECT distinct Sprache FROM android_metadata WHERE Leuchte='"+leuchte+"'",null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String sprache = cursor.getString(0);
            sprachen.add(sprache);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return sprachen;
    }

    public String getURL(String leuchte, String sprache){
        openDatabase();
        Cursor cursor = mDataBase.rawQuery("SELECT URL FROM android_metadata WHERE Leuchte='"+leuchte+"' AND Sprache='"+sprache+"'", null);
        cursor.moveToFirst();
        String URL = cursor.getString(0);
        cursor.close();
        close();
        return URL;
    }
}
