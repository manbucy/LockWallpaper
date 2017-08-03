package net.manbucy.lockwallpaper.util;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import net.manbucy.lockwallpaper.base.App;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Utility
 * Created by yang on 2017/7/6.
 */

public class Utility {

    /**
     * 获取数据库
     *
     * @param srcPath 数据库路径
     * @return 数据库
     */
    public static SQLiteDatabase getDB(String srcPath) {
        File srcDbFile = new File(srcPath);
        if (!srcDbFile.exists()) {
            return null;
        }
        if (!srcDbFile.canRead()) {
            return null;
        }
        return SQLiteDatabase.openOrCreateDatabase(srcDbFile, null);
    }

    public static void copyFile(String srcFile, String destFile) {
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(srcFile);
            outputStream = new FileOutputStream(destFile);
            byte[] bytes = new byte[1024 * 10];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            App.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.parse("file://"+destFile)));
        }
    }
}
