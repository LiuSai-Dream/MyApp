package com.example.liusai.locationtest.Dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.example.liusai.locationtest.ContentProvider.Provider;
import com.example.liusai.locationtest.R;

/**
 * Created by user on 2016/6/4.
 */
public class InputDialog {

    private  static final String TAG = InputDialog.class.getSimpleName();

    public interface OnInputCompleteListener {

        public abstract void onInputComplete(int id);
    }

    public static void popInputId(Context context, String title, final OnInputCompleteListener onInputCompleteListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        View viewInflated = LayoutInflater.from(context).inflate(R.layout.dia_input, null , false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.idinput);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String strId = input.getText().toString();

                if (!TextUtils.isEmpty(strId)) {

                    Log.d(TAG, "输入的内容 " + strId);
                    int id = Integer.valueOf(strId);
                    onInputCompleteListener.onInputComplete(id);

                    Log.d(TAG, "value is " + id);

                }

            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

}
