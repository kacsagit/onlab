package com.example.kata.onlab.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import com.example.kata.onlab.R;

/**
 * Created by Kata on 2017. 04. 17..
 */

public class Radio extends com.mikhaellopez.circularimageview.CircularImageView implements Checkable, View.OnClickListener  {
    boolean check=false;
    Context context;
    TypedArray attributes;


    private static final int[] CHECKED_STATE_SET = { android.R.attr.state_checked };

    public Radio(Context context) {
        this(context, null);
    }

    public Radio(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public Radio(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        attributes = context.obtainStyledAttributes(attrs, R.styleable.CircularImageView, defStyleAttr, 0);
        attributes.getInt(android.R.attr.checked ,0);
    }



    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    @Override
    public void setChecked(boolean checked) {
        check=checked;
        if (check){
            setBorderColor(Color.WHITE);
        }else{
            setBorderColor(Color.BLACK);
        }
    }

    @Override
    public int[] onCreateDrawableState(final int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);

        if (isChecked())
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        return drawableState;
    }

    @Override
    public boolean isChecked() {
        return check;
    }

    @Override
    public void toggle() {
        check=!check;

        if (check){
            setBorderColor(Color.WHITE);
        }else{
            setBorderColor(Color.BLACK);
        }
    }
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void onClick(View v) {
        //toggle();
    }
}
