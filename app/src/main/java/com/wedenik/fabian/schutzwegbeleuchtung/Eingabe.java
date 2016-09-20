package com.wedenik.fabian.schutzwegbeleuchtung;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class Eingabe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eingabe);
    }

//    todo Bei Zurueckkehr auf einen der beiden Bildschirme den Stack verkleinern, damit man am Handy mit der ZURUECK-Taste nicht jeden einzelnen Schritt zurueckspringt

    public void berechnung(View view) {
        Intent intent1 = new Intent(this, Ausgabe.class);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        Spinner spinner3 = (Spinner) findViewById(R.id.Laenderwahl);
        Bundle bundle = new Bundle();
        String breite = spinner.getSelectedItem().toString();
        String laenge = spinner2.getSelectedItem().toString();
        String land = spinner3.getSelectedItem().toString();
        bundle.putString("Laenge", laenge);
        bundle.putString("Breite", breite);
        bundle.putString("Land", land);
        intent1.putExtras(bundle);
        startActivity(intent1);
    }

    public void Datenabfrage(View view) {
        TestAdapter mDbHelper = new TestAdapter(getApplicationContext());
        mDbHelper.createDatabase();
        mDbHelper.open();

        Cursor cursor = mDbHelper.getTestData();
        List<String> datalist = new ArrayList<String>();
        if(cursor.moveToFirst()) {
            do {
                String Land = cursor.getString(1);
                datalist.add(Land);
            } while (cursor.moveToNext());
        }
        Set<String> hs = new HashSet<>();
        hs.addAll(datalist);
        datalist.clear();
        datalist.addAll(hs);
        String[] data = new String[datalist.size()];
        datalist.toArray(data);


        //String MY_PREFS_NAME = "MzPrefsFile";
       // SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
       // editor.putStringSet("Laender", hs);
       // editor.apply();

        int asdf = cursor.getCount();

        Spinner Laenderwahl = (Spinner) findViewById(R.id.Laenderwahl);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        //adapter.add(String.valueOf(asdf));
        Laenderwahl.setAdapter(adapter);
        //cursor.close();
        mDbHelper.close();
    }
}
