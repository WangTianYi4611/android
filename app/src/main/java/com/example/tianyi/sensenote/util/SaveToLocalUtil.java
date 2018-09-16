package com.example.tianyi.sensenote.util;

import android.util.Log;

import com.example.tianyi.sensenote.bean.UserBean;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveToLocalUtil {

    public static final String USER_TOKEN_FILE = "/user_token_file";

    public static final void saveObject(String path, Object saveObject) {
        FileOutputStream fOps = null;
        ObjectOutputStream oOps = null;
        File file = new File(path);

        try {
            if(!file.exists()){
                boolean success = file.createNewFile();
                if(!success){
                    Log.e("file","error create file");
                }
            }
            fOps = new FileOutputStream(file);
            oOps = new ObjectOutputStream(fOps);
            oOps.writeObject(saveObject);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtils.close(oOps);
            CloseUtils.close(fOps);
        }
    }

    public static final Object restoreObject(String path) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        Object obj = null;
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        try {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            obj = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtils.close(fis);
            CloseUtils.close(ois);
        }
        return obj;

    }

    static class CloseUtils {
        public static void close(Closeable stream) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
