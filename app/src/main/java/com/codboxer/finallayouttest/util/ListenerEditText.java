package com.codboxer.finallayouttest.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author Man
 * 11/04/2021
 */
public class ListenerEditText extends androidx.appcompat.widget.AppCompatEditText {
    private KeyImeChange keyImeChangeListener;

    // Constructor
    public ListenerEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnKeyImeChangeListener(KeyImeChange listener){
        keyImeChangeListener = listener;
    }

    @Override
    public void onEditorAction(int actionCode) {
        super.onEditorAction(actionCode);
        keyImeChangeListener.onEditorAction(actionCode);
    }

    @Override
    public boolean onKeyPreIme (int keyCode, KeyEvent event){
        if(keyImeChangeListener != null){
            return keyImeChangeListener.onKeyIme(keyCode, event);
        }
        return false;
    }

    // Interface
    public interface KeyImeChange {
        public boolean onKeyIme(int keyCode, KeyEvent event);
        
        public boolean onEditorAction(int actionCode);
    }
}
