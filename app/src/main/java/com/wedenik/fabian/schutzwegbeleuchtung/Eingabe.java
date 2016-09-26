package com.wedenik.fabian.schutzwegbeleuchtung;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Eingabe extends AppCompatActivity {
    private DataBaseHelper mDBHelper;
    private List<String> Laender;
    private List<String> Laenge;
    private List<String> Breite;

    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;

    private static String file_url = "http://swarcofiles.com/futurit/app/android_database.sqlite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eingabe);


        final Handler mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message message) {
                Toast.makeText(Eingabe.this, "Download complete", Toast.LENGTH_SHORT).show();
                mDBHelper = new DataBaseHelper(Eingabe.this);
                Spinner Laenderwahl = (Spinner) findViewById(R.id.Laenderwahl);
                Spinner Laengenwahl = (Spinner) findViewById(R.id.spinner2);

                File database = getApplicationContext().getDatabasePath(DataBaseHelper.DB_NAME);
                if (!database.exists()){
                    mDBHelper.getReadableDatabase();
                }

                Laender = mDBHelper.getLaender();

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Eingabe.this, android.R.layout.simple_spinner_item, Laender);
                Laenderwahl.setAdapter(adapter);

                Laenderwahl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        String land = arg0.getItemAtPosition(arg2).toString();

                        Spinner Laengenwahl = (Spinner) findViewById(R.id.spinner2);

                        Laenge = mDBHelper.getLaenge(land);

                        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(Eingabe.this, android.R.layout.simple_spinner_item, Laenge);

                        Laengenwahl.setAdapter(adapter1);

                        Object item = arg0.getItemAtPosition(arg2);
                        if (item!=null) {
                            Toast.makeText(Eingabe.this, item.toString()+"Selected",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });

                Laengenwahl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        String laenge = arg0.getItemAtPosition(arg2).toString();
                        DataBaseHelper mDBHelper = new DataBaseHelper(Eingabe.this);

                        Spinner Laenderwahl = (Spinner) findViewById(R.id.Laenderwahl);
                        Spinner Breitenwahl = (Spinner) findViewById(R.id.spinner);

                        String land = Laenderwahl.getSelectedItem().toString();

                        Breite = mDBHelper.getBreite(land, laenge);

                        Set<String> hs2 = new HashSet<>();

                        hs2.addAll(Breite);

                        Breite.clear();

                        Breite.addAll(hs2);

                        String[] data2 = new String[Breite.size()];

                        Breite.toArray(data2);
                        Arrays.sort(data2);

                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(Eingabe.this, android.R.layout.simple_spinner_item, data2);

                        Breitenwahl.setAdapter(adapter2);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });

            }
        };
        new DownloadFileFromURL(mHandler).execute(file_url);

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


    @Override
    protected ProgressDialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    /**
     * Background Async Task to download file
     * */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        private final Handler mHandler;

        public DownloadFileFromURL(Handler mHandler) {
            this.mHandler = mHandler;
        }

        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a typical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);


                String PATH = "/data/data/" + getApplicationContext().getPackageName() + "/databases/";
                File file = new File(PATH, "android_database.sqlite");

                openOrCreateDatabase("asdf.sqlite",MODE_PRIVATE, null);

                // Output stream
                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

                Message message = mHandler.obtainMessage(0, "dw finished");
                message.sendToTarget();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);

        }

    }
}

