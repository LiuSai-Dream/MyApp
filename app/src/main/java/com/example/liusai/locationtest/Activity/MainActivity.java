package com.example.liusai.locationtest.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.baidu.mapapi.SDKInitializer;
import com.example.liusai.locationtest.Adapter.MainBoardItemAdapter;
import com.example.liusai.locationtest.R;
import com.example.liusai.locationtest.Utils.AlarmManagerUtil;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initial Baidu act_map.
        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.act_main);

        // Setting alarm at the begining.
        AlarmManagerUtil.makeNotificationAlarm(this);

        // Setting content of GridView.
        GridView gridView = (GridView) findViewById(R.id.gridview);
        // gridView.setAdapter(new ImageAdapter(this));
        gridView.setAdapter(new MainBoardItemAdapter(this));
        gridView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = null;
        switch (position) {
            case 0:
                intent = new Intent(MainActivity.this, ExperimentalActivity.class);
                startActivity(intent);
                break;

            case 1:
                intent = new Intent(MainActivity.this, LocationActivity.class);
                startActivity(intent);
                break;

            case 2:
                intent = new Intent(MainActivity.this, ContentProviderOperationActivity.class);
                startActivity(intent);
                break;


        }
    }

}
