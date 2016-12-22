package org.licpnz.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import org.licpnz.LicApplication;
import org.licpnz.R;
import org.licpnz.api.Api;

/**
 * Created by Ilya on 21.12.2016.
 */

public class ImageLoadingDialogFragment extends DialogFragment implements DialogInterface.OnClickListener,View.OnClickListener{

    public View mContainer;
    public RadioButton mEnableButton,mDisableButton;
    public boolean isImageLoadingEnabled;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContainer = getActivity().getLayoutInflater().inflate(R.layout.settings_image_loading_dialog,null);
        mEnableButton = (RadioButton) mContainer.findViewById(R.id.button_enabled);
        mDisableButton = (RadioButton) mContainer.findViewById(R.id.button_disabled);
        mEnableButton.setOnClickListener(this);
        mDisableButton.setOnClickListener(this);
        isImageLoadingEnabled = LicApplication.getInstance().getImageLoadingEnabled();
        if (isImageLoadingEnabled)
            mEnableButton.setChecked(true);
        else
            mDisableButton.setChecked(true);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which==DialogInterface.BUTTON_POSITIVE){
            LicApplication.getInstance().setIsImageLoadingEnabled(isImageLoadingEnabled);
        }else{

        }
        dismiss();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.settings_dialog_image_loading_title))
                .setView(mContainer)
                .setPositiveButton("OK",this)
                .setNeutralButton("Отмена",this)
                .create();
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.button_enabled) {
            mDisableButton.setChecked(false);
            //mEnableButton.setChecked(true);
            isImageLoadingEnabled = true;
        }else {
            mEnableButton.setChecked(false);
            //mDisableButton.setChecked(true);
            isImageLoadingEnabled = false;
        }
    }
}
