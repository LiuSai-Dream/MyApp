package com.example.liusai.locationtest.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.liusai.locationtest.R;
import com.example.liusai.locationtest.Action.StationLocate;

/**
 * Created by liusai on 16/5/12.
 */
public class StationActivity extends Activity implements View.OnClickListener {

    private static final String TAG = StationActivity.class.getSimpleName();
    private Button button1 ;
    private TextView cellText, locationText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_station);

        button1 = (Button) findViewById(R.id.button1);
        cellText = (TextView) findViewById(R.id.cellText);
        locationText = (TextView) findViewById(R.id.locationText);

        button1.setOnClickListener(this);

    }

    public void onClick(View view) {
        StationLocate sl = new StationLocate(this, cellText, locationText);
        sl.execute();
    }

    protected void onResume() {
        super.onResume();
    //    MobclickAgent.onResume(this);  //Session 统计
    }

    protected void onPause() {
        super.onPause();
    //    MobclickAgent.onPause(this);
    }
}
