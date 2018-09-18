package com.example.tianyi.sensenote.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    public static boolean checkNullThenToast(Context context, Object obj, String msg){
        if(obj instanceof String){
            if(StringUtil.isEmpty((String)obj)) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}
