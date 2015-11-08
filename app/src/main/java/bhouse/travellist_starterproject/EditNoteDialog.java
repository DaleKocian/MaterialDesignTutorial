package bhouse.travellist_starterproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

/**
 * Created by dkocian on 11/7/2015.
 */
public abstract class EditNoteDialog {
    public static final String CANCEL = "Cancel";
    public static final String OK = "OK";
    Context mContext;
    AlertDialog alertDialog;
    AlertDialog.Builder builder;

    public EditNoteDialog(Context context) {
        this.mContext = context;
        builder = new AlertDialog.Builder(mContext);
        builder.setView(R.layout.edit_note_dialog);
        builder.setPositiveButton(OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText etNote = (EditText) alertDialog.findViewById(R.id.note);
                setOnPositiveButton(etNote);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(CANCEL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setOnNegativeButtonClicked();
                dialog.dismiss();
            }
        });

    }

    public abstract void setOnPositiveButton(EditText note);

    public abstract void setOnNegativeButtonClicked();

    public void show() {
        alertDialog = builder.show();
    }
}
