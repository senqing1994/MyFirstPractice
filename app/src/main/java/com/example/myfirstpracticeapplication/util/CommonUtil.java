package com.example.myfirstpracticeapplication.util;

import android.text.Editable;
import android.text.Selection;
import android.widget.EditText;

public class CommonUtil {

    public static void cursorToEnd(EditText editText){
        Editable text = editText.getText();
        Selection.setSelection(text,text.length());
    }
}
