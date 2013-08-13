package com.android4all.uselesspuffin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by jbettcher on 8/13/13.
 */
public class InfoDialogFragment extends DialogFragment {
    static final String TAG = InfoDialogFragment.class.getSimpleName();
    private View viewInfo;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        viewInfo = inflater.inflate(R.layout.dialog_info, null);
        builder.setView(viewInfo);
        builder.setMessage(R.string.dialog_info_title);
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onStart() {
        TextView textVersionCode = (TextView) viewInfo.findViewById(R.id.text_version_code);
        TextView textScreenSize = (TextView) viewInfo.findViewById(R.id.text_screen_size);
        TextView textFlavorName = (TextView) viewInfo.findViewById(R.id.text_flavor_name);

        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            textVersionCode.setText("Version Code: " + String.valueOf(pInfo.versionCode));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        String dimens = String.valueOf(outMetrics.widthPixels) + "x" + String.valueOf(outMetrics.heightPixels);
        textScreenSize.setText(dimens + " (" + String.valueOf(outMetrics.densityDpi) + " dpi)");

        textFlavorName.setText("This is the " + getActivity().getText(R.string.flavor_name) + " variant");

        super.onStart();
    }

}
