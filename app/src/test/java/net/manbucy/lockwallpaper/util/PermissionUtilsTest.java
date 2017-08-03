package net.manbucy.lockwallpaper.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * PermissionUtilsTest
 * Created by yang on 2017/8/3.
 */
public class PermissionUtilsTest {
    @Test
    public void borrowPermission() throws Exception {
        PermissionUtils.borrowPermission();
    }

    @Test
    public void repayPermission() throws Exception {
        PermissionUtils.repayPermission();
    }


}