package org.licpnz.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.licpnz.LicApplication;
import org.licpnz.R;
import org.licpnz.api.Api;

/**
 * Created by Ilya on 14.12.2016.
 */

public class HostChangeDialogFragment extends DialogFragment implements DialogInterface.OnClickListener{

    public View mContainer;
    public EditText mEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContainer = getActivity().getLayoutInflater().inflate(R.layout.settings_api_host_change,null);
        mEditText = (EditText) mContainer.findViewById(R.id.dialog_host_change_edittext);
        mEditText.setText(Api.getHost());
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which==DialogInterface.BUTTON_POSITIVE){
            LicApplication.getInstance().setHost(mEditText.getText().toString());
        }else{

        }
        dismiss();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.settings_dialog_host_change_title))
                .setView(mContainer)
                .setPositiveButton("OK",this)
                .setNeutralButton("Отмена",this)
                .create();
    }
}
