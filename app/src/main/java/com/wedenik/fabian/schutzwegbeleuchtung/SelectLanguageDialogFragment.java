package com.wedenik.fabian.schutzwegbeleuchtung;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by fabian wedenik on 25.09.16.
 */
public class SelectLanguageDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();
        ArrayList<String> Sprachen = bundle.getStringArrayList("sprachen");
        final String leuchte = bundle.getString("leuchte");
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_singlechoice, Sprachen);
        builder.setTitle("Select Language")
                .setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id){
                        DataBaseHelper mDBHelper = new DataBaseHelper(getActivity());
                        ListView lw = ((AlertDialog)dialog).getListView();
                        String selectedLanguage = lw.getAdapter().getItem(lw.getCheckedItemPosition()).toString();
                        String URL = mDBHelper.getURL(leuchte, selectedLanguage);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                        startActivity(browserIntent);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}
