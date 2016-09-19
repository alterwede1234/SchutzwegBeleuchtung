package com.wedenik.fabian.schutzwegbeleuchtung;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

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

    public String[] Datenabfrage() {
        public String[] spinner_eintraege() {
            String Tabellen_Name="probedatenbank";
            String selectQuery="SELECT * FROM " + Tabellen_Name;
                SQLiteDatabase db = DataBaseHelper.this.openDatabase();
            Cursor cursor = db.rawQuery(selectQuery,  null);
                String[] data = null;
            if(cursor.moveToFirst()) {
                do {
                    // get the data into array or class variable
                } while (cursor.moveToNext());
            }
            db.close();
            return data;
        }
    }
}
