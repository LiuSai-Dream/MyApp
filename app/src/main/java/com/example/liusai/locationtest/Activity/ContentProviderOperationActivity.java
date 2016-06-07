package com.example.liusai.locationtest.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.liusai.locationtest.ContentProvider.ProviderOperation;
import com.example.liusai.locationtest.R;
import com.example.liusai.locationtest.Dialog.InputDialog;

public class ContentProviderOperationActivity extends Activity implements View.OnClickListener, InputDialog.OnInputCompleteListener {

    private static final String TAG = ContentProviderOperationActivity.class.getSimpleName();

    private ProviderOperation providerOperation;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_content_provider);

        providerOperation = new ProviderOperation(this);

        Button btnInsertData = (Button) findViewById(R.id.insertdata);
        Button btnDisplayData = (Button) findViewById(R.id.displaydata);
        Button btnDeleteData = (Button) findViewById(R.id.deletadata);

        listView = (ListView) findViewById(R.id.contentlistview);

        btnInsertData.setOnClickListener(this);
        btnDisplayData.setOnClickListener(this);
        btnDeleteData.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.insertdata:
                providerOperation.insert();
                providerOperation.display(listView);
                Toast.makeText(this, "插入数据完成", Toast.LENGTH_SHORT).show();
                break;

            case R.id.displaydata:
                providerOperation.display(listView);
                Toast.makeText(this, "显示数据完成", Toast.LENGTH_SHORT).show();
                break;

            case R.id.deletadata:
                // Pop an dialog to enter the id want to delete
                InputDialog.popInputId(this, "输入要删除的行号", this);
                break;
        }

    }

    @Override
    public void onInputComplete(int id) {

        providerOperation.delete(id);
        providerOperation.display(listView);

        Toast.makeText(this, "删除数据完成", Toast.LENGTH_SHORT).show();
    }




}
