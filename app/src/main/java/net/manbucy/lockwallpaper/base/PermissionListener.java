package net.manbucy.lockwallpaper.base;

import java.util.List;

/**
 * PermissionListener
 * Created by yang on 2017/image2/26.
 */

public interface PermissionListener {
    void onGranted();

    void onDenied(List<String> deniedPermission);
}
