package com.wedenik.fabian.schutzwegbeleuchtung;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by fabian on 20.09.16.
 */
public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){

        Object item = parent.getItemAtPosition(pos);
        if (item!=null) {
            Toast.makeText(this, item.toString(),
                    Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "Selected",
                Toast.LENGTH_SHORT).show();

        String land = parent.getItemAtPosition(pos).toString();
        DataBaseHelper mDBHelper = new DataBaseHelper(this);
        setContentView(R.layout.activity_eingabe);

        Spinner Laengenwahl = (Spinner) findViewById(R.id.spinner2);
        Spinner Breitenwahl = (Spinner) findViewById(R.id.spinner);

        File database = getApplicationContext().getDatabasePath(DataBaseHelper.DB_NAME);
        if (!database.exists()){
            mDBHelper.getReadableDatabase();
        }
        List<String> Laenge = mDBHelper.getLaenge(land);
        List<String> Breite = mDBHelper.getBreite(land);

        Set<String> hs1 = new HashSet<>();
        Set<String> hs2 = new HashSet<>();

        hs1.addAll(Laenge);
        hs2.addAll(Breite);

        Laenge.clear();
        Breite.clear();

        Laenge.addAll(hs1);
        Breite.addAll(hs2);

        String[] data1 = new String[Laenge.size()];
        String[] data2 = new String[Breite.size()];

        Laenge.toArray(data1);
        Arrays.sort(data1);
        Breite.toArray(data2);
        Arrays.sort(data2);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data2);

        Laengenwahl.setAdapter(adapter1);
        Breitenwahl.setAdapter(adapter2);

    }

    public void onNothingSelected(AdapterView<?> parent){

    }
}
