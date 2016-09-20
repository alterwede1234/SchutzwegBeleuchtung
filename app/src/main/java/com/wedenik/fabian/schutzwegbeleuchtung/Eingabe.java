package com.wedenik.fabian.schutzwegbeleuchtung;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



public class Eingabe extends AppCompatActivity {
    private DataBaseHelper mDBHelper;
    private List<String> Laender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eingabe);
    }

//    todo Bei Zurueckkehr auf einen der beiden Bildschirme den Stack verkleinern, damit man am Handy mit der ZURUECK-Taste nicht jeden einzelnen Schritt zurueckspringt

    //Copy the database from assets


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
        setContentView(R.layout.activity_eingabe);
        Spinner Laenderwahl = (Spinner) findViewById(R.id.Laenderwahl);
        mDBHelper = new DataBaseHelper(this);

        File database = getApplicationContext().getDatabasePath(DataBaseHelper.DB_NAME);
        if (false == database.exists()){
            mDBHelper.getReadableDatabase();
            if(mDBHelper.copyDataBase(getApplicationContext())) {
                Toast.makeText(this, "Copy database succes", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,"Copy Data error", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Laender = mDBHelper.getLaender();

        Set<String> hs = new HashSet<>();
        hs.addAll(Laender);
        Laender.clear();
        Laender.addAll(hs);
        String[] data = new String[Laender.size()];
        Laender.toArray(data);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        Laenderwahl.setAdapter(adapter);
    }
}
