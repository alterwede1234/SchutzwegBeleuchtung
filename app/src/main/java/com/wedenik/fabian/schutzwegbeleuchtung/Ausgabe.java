package com.wedenik.fabian.schutzwegbeleuchtung;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Ausgabe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ausgabe);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String laenge = extras.getString("Laenge");
        String breite = extras.getString("Breite");
        String land = extras.getString("Land");
        TextView textView = (TextView) findViewById(R.id.laenge);
        textView.setTextSize(20);
        textView.append(laenge);
        TextView textView2 = (TextView) findViewById(R.id.breite);
        textView2.setTextSize(20);
        textView2.append(breite);
        TextView textView3 = (TextView) findViewById(R.id.land);
        textView3.setTextSize(20);
        textView3.append(land);

    }

    public void back_to_first_screen(View view) {
        Intent intent = new Intent(this, Eingabe.class);
        startActivity(intent);

    }

    public void show_data_sheet(View view) {
        String url = getString(R.string.url_zu_datenblatt);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}
