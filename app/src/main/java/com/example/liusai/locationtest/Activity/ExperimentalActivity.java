package com.example.liusai.locationtest.Activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.example.liusai.locationtest.Dialog.ExperimentalDialogFragment;
import com.example.liusai.locationtest.R;

public class ExperimentalActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_experimental);

        Button btn_exper = (Button) findViewById(R.id.exper);
        btn_exper.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.exper:
                FragmentManager fm = getFragmentManager();
                new ExperimentalDialogFragment().show(fm, "TAG USED BY SYSTEM");
                break;

        }

    }

}
