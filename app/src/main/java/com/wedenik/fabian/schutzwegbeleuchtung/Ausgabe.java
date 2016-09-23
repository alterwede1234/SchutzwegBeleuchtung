package com.wedenik.fabian.schutzwegbeleuchtung;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.io.File;
import java.util.*;

public class Ausgabe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ausgabe);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        final String laenge = extras.getString("Laenge");
        final String breite = extras.getString("Breite");
        final String land = extras.getString("Land");
        TextView textView = (TextView) findViewById(R.id.laenge);
        textView.setTextSize(20);
        textView.append(laenge);
        TextView textView2 = (TextView) findViewById(R.id.breite);
        textView2.setTextSize(20);
        textView2.append(breite);
        TextView textView3 = (TextView) findViewById(R.id.land);
        textView3.setTextSize(20);
        textView3.append(land);

        Spinner leuchte = (Spinner) findViewById(R.id.leuchte);
        Spinner lph_lpa = (Spinner) findViewById(R.id.lph_lpa);
        leuchte.setVisibility(View.INVISIBLE);
        lph_lpa.setVisibility(View.INVISIBLE);

        DataBaseHelper mDBHelper = new DataBaseHelper(Ausgabe.this);
        File database = getApplicationContext().getDatabasePath(DataBaseHelper.DB_NAME);
        if (!database.exists()){
            mDBHelper.getReadableDatabase();
        }

        List<String> Leuchtenmodelle = new ArrayList<>();
        Leuchtenmodelle = mDBHelper.getModelle(land, laenge, breite);
        Spinner leuchtenmodelle = (Spinner) findViewById(R.id.leuchtenmodelle);
        Set<String> hs = new HashSet<>();
        hs.addAll(Leuchtenmodelle);
        Leuchtenmodelle.clear();
        Leuchtenmodelle.addAll(hs);
        String[] data = new String[Leuchtenmodelle.size()];
        Leuchtenmodelle.toArray(data);
        Arrays.sort(data);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        leuchtenmodelle.setAdapter(adapter);

        leuchtenmodelle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                String modell = arg0.getItemAtPosition(arg2).toString();
                DataBaseHelper mDBHelper = new DataBaseHelper(Ausgabe.this);
                Spinner leuchte = (Spinner) findViewById(R.id.leuchte);
                Spinner lph_lpa = (Spinner) findViewById(R.id.lph_lpa);

                List<String> leuchten = mDBHelper.getLeuchten(land, laenge, breite, modell);
                Set<String> hs = new HashSet<>();
                hs.addAll(leuchten);
                leuchten.clear();
                leuchten.addAll(hs);
                String[] data = new String[leuchten.size()];
                leuchten.toArray(data);
                Arrays.sort(data);

                ArrayAdapter adapter = new ArrayAdapter<String>(Ausgabe.this,android.R.layout.simple_spinner_item,data);
                leuchte.setAdapter(adapter);

                leuchte.setVisibility(View.VISIBLE);
                lph_lpa.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0){

            }
        });

        leuchte.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String gewaehlte_leuchte = arg0.getItemAtPosition(arg2).toString();
                DataBaseHelper mDBHelper = new DataBaseHelper(Ausgabe.this);
                Spinner leuchtenmodelle = (Spinner) findViewById(R.id.leuchtenmodelle);
                Spinner lph_lpa = (Spinner) findViewById(R.id.lph_lpa);

                String modell = leuchtenmodelle.getSelectedItem().toString();

                List<String> hoehen = mDBHelper.getHoehen(land, laenge, breite, modell, gewaehlte_leuchte);

                Set<String> hs = new HashSet<>();
                hs.addAll(hoehen);
                hoehen.clear();
                hoehen.addAll(hs);
                String[] data = new String[hoehen.size()];
                hoehen.toArray(data);
                Arrays.sort(data);

                ArrayAdapter adapter = new ArrayAdapter<String>(Ausgabe.this,android.R.layout.simple_spinner_item,data);
                lph_lpa.setAdapter(adapter);


                lph_lpa.setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0){

            }
        });

        lph_lpa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String hoehe = arg0.getItemAtPosition(arg2).toString();
                DataBaseHelper mDBHelper = new DataBaseHelper(Ausgabe.this);
                Spinner leuchtenmodelle = (Spinner) findViewById(R.id.leuchtenmodelle);
                Spinner leuchte = (Spinner) findViewById(R.id.leuchte);
                TextView lichtpunkthoehe = (TextView) findViewById(R.id.lichtpunkthoehe);
                TextView lichtpunktabstand = (TextView) findViewById(R.id.lichtpunktabstand);

                String modell = leuchtenmodelle.getSelectedItem().toString();
                String gewaehlte_leuchte = leuchte.getSelectedItem().toString();

                String abstand = mDBHelper.getAbstand(land,laenge,breite, modell, gewaehlte_leuchte, hoehe);

                lichtpunkthoehe.setText(hoehe);
                lichtpunktabstand.setText(abstand);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


    }

    public void back_to_first_screen(View view) {
        Intent intent = new Intent(this, Eingabe.class);
        startActivity(intent);

    }

    public void show_data_sheet(View view) {
        Spinner Leuchte = (Spinner) findViewById(R.id.leuchte);
        String leuchte = Leuchte.getSelectedItem().toString();
        DataBaseHelper mDBHelper = new DataBaseHelper(Ausgabe.this);
        ArrayList<String> sprachen = mDBHelper.getSprachen(leuchte);
        Bundle dialog = new Bundle();
        dialog.putStringArrayList("sprachen", sprachen);
        dialog.putString("leuchte", leuchte);
        DialogFragment SprachenWahl = new SelectLanguageDialogFragment();
        SprachenWahl.setArguments(dialog);
        SprachenWahl.show(getFragmentManager(),"SelectLanguageDialogFragment" );
    }
}
