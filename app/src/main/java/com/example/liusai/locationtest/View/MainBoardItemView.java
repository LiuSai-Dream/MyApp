package com.example.liusai.locationtest.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liusai.locationtest.R;

public class MainBoardItemView extends FrameLayout {

    private ImageView imageView;

    private TextView textView;

    public MainBoardItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_main_board, this);

        imageView = (ImageView) findViewById(R.id.itemImageView);
        textView = (TextView) findViewById(R.id.itemTextView);
    }

    // Setting the content of ImageView
    public void setImageViewContent(int imgId) {
        imageView.setImageResource(imgId);
    }

    // Setting the content of TextView.
    public void setTextViewContent(String text) {
        textView.setText(text);
    }

}
