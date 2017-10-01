package net.manbucy.lockwallpaper.util;

import android.util.Log;

import com.stericson.RootShell.execution.Command;
import com.stericson.RootTools.RootTools;

import net.manbucy.lockwallpaper.ui.CompleteListener;

/**
 * PermissionUtils
 * 设置 /data/user/0/cn.nubia.gallerylockscreen/databases/SIT_CONTENT
 * /data/user/0/cn.nubia.gallerylockscreen/files/image/* 的访问权限
 * Created by yang on 2017/8/3.
 */
public class PermissionUtils {
    private static String[] paths;
    private static String[] permissions;

    //          "/data"  "771"
//          "/data/user"  "711"
//          "/data/user/0"  "771"
//          "/data/user/0/cn.nubia.gallerylockscreen" "751"
//          "/data/user/0/cn.nubia.gallerylockscreen/databases" "771"
//          "/data/user/0/cn.nubia.gallerylockscreen/databases/SIT_CONTENT" "660"
//          "/data/user/0/cn.nubia.gallerylockscreen/files" "771"
//          "/data/user/0/cn.nubia.gallerylockscreen/files/image" "700"
//          "/data/user/0/cn.nubia.gallerylockscreen/files/image/*" "600"
    static {
        paths = new String[]{
                "/data",
                "/data/user",
                "/data/user/0",
                "/data/user/0/cn.nubia.gallerylockscreen",
                "/data/user/0/cn.nubia.gallerylockscreen/files",
                "/data/user/0/cn.nubia.gallerylockscreen/files/image",
                "/data/user/0/cn.nubia.gallerylockscreen/files/image/*",
                "/data/user/0/cn.nubia.gallerylockscreen/databases",
                "/data/user/0/cn.nubia.gallerylockscreen/databases/SIT_CONTENT",
                "/data/user/0/cn.nubia.gallerylockscreen/databases/SIT_CONTENT-journal",
                "/data/user/0/cn.nubia.gallerylockscreen/files/preset",
                "/data/user/0/cn.nubia.gallerylockscreen/files/preset/*"};
        permissions = new String[]{"771", "711", "771", "751", "771",
                "700", "600", "771", "660","600", "700", "600"};
    }

    /**
     * 设置图片 数据库的访问权限为777
     */
    public static void borrowPermission(final CompleteListener listener) {
        String[] commands = new String[paths.length];
        for (int i = 0; i < paths.length; i++) {
            commands[i] = "chmod 777 " + paths[i];
        }
        Command command = new Command(0, commands) {
            @Override
            public void commandCompleted(int id, int exitcode) {
                super.commandCompleted(id, exitcode);
                Log.d("---PermissionUtils", "commandCompleted: " + id + "---" + exitcode);
                listener.onCompleted();
            }
        };
        try {
            RootTools.getShell(true).add(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 还原 图片数据库的访问权限
     */
    public static void repayPermission(final CompleteListener listener) {
        String[] commands = new String[paths.length];
        for (int i = 0; i < paths.length; i++) {
            commands[i] = "chmod " + permissions[i] + " " + paths[i];
        }
        Command command = new Command(0, commands) {
            @Override
            public void commandCompleted(int id, int exitcode) {
                super.commandCompleted(id, exitcode);
                Log.d("---PermissionUtils", "commandCompleted: " + id + "---" + exitcode);
                listener.onCompleted();
            }
        };
        try {
            RootTools.getShell(true).add(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

