package com.example.liusai.locationtest.Adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liusai.locationtest.R;
import com.example.liusai.locationtest.View.MainBoardItemView;

/**
 * Created by user on 2016/5/31.
 */
public class MainBoardItemAdapter extends BaseAdapter {

    private static final String TAG = MainBoardItemAdapter.class.getSimpleName();

    private Context mContext;
    private TypedArray items;
    private int[] itemImgs;
    private String[] itemTexts;

    public MainBoardItemAdapter(Context c) {

        mContext = c;
        items = mContext.getResources().obtainTypedArray(R.array.itemimgs);
        itemTexts = mContext.getResources().getStringArray(R.array.itemtexts);

        itemImgs = new int[items.length()];
        for (int i = 0; i < items.length(); ++i) {
            itemImgs[i] = items.getResourceId(i, 0);
        }

        items.recycle();
    }

    @Override
    public int getCount() {
        return itemImgs.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return itemImgs[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MainBoardItemView mainBoardItemView = null;

        if (convertView == null) {
            mainBoardItemView = new MainBoardItemView(mContext, null);

        } else {
            mainBoardItemView = (MainBoardItemView) convertView;
        }

        mainBoardItemView.setImageViewContent(itemImgs[position]);
        mainBoardItemView.setTextViewContent(itemTexts[position]);
        return mainBoardItemView;
    }


}
