package net.manbucy.lockwallpaper.ui;

import android.Manifest;
import android.app.WallpaperManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import net.manbucy.lockwallpaper.R;
import net.manbucy.lockwallpaper.base.BaseActivity;
import net.manbucy.lockwallpaper.base.PermissionListener;
import net.manbucy.lockwallpaper.model.Image;
import net.manbucy.lockwallpaper.util.PermissionUtils;
import net.manbucy.lockwallpaper.util.Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends BaseActivity implements ImageOnTouchListener {
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;
    private List<Image> imageList = new ArrayList<>();
    private LinearLayout rootView;
    private ImageAdapter adapter;
    private PopupWindow popupWindow;
    private Button setWallpaper;
    private Button saveWallpaper;
    private Image clickImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionUtils.borrowPermission(new CompleteListener() {
            @Override
            public void onCompleted() {
                loadData();
            }
        });
        initView();
        initListener();
    }

    /**
     * 初始化view
     */
    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rootView = (LinearLayout) findViewById(R.id.root_view);

        adapter = new ImageAdapter(imageList, this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_layout, null);
        popupWindow = new PopupWindow(popupView, 350, 300);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);

        setWallpaper = (Button) popupView.findViewById(R.id.btn_set_wallpaper);
        saveWallpaper = (Button) popupView.findViewById(R.id.btn_save_wallpaper);
        Log.d("---MainActivity", "initView: ");
    }

    /**
     * 监听
     */
    private void initListener() {
        setWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
                try {
                    manager.setStream(new FileInputStream(clickImage.getImagePath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(MainActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }
        });
        saveWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWallpaperToLocal();
                Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }
        });
        Log.d("---MainActivity", "initListener: ");
    }

    /**
     * 加载数据
     */
    private void loadData() {
        //加载系统从网络下载好的锁屏画报
        SQLiteDatabase database = Utility.getDB("/data/user/0/cn.nubia.gallerylockscreen/databases/SIT_CONTENT");
        if (database != null) {
            Cursor cursor = database.query("SIT_CONTENT", new String[]{"TITLE", "CONTENT", "IMG_PATH"}, null,
                    null, null, null, null);
            while (cursor.moveToNext()) {
                Image image = new Image();
                image.setTitle(cursor.getString(0));
                image.setContent(cursor.getString(1));
                image.setImagePath(cursor.getString(2));
                if (!new File(image.getImagePath()).exists()) {
                    continue;
                }
                if (!imageList.contains(image)) {
                    imageList.add(image);
                }
            }
        } else {
            Toast.makeText(MainActivity.this, "读取数据库数据失败", Toast.LENGTH_SHORT).show();
        }
        //加载系统预存的锁屏画报
        File preset = new File("/data/user/0/cn.nubia.gallerylockscreen/files/preset");
        for (File file : preset.listFiles()) {
            Image image = new Image();
            image.setTitle("系统预设");
            image.setContent("系统预设锁屏画报");
            image.setImagePath(file.getAbsolutePath());
            imageList.add(image);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 保存图片至本地
     */
    private void saveWallpaperToLocal() {
        final String pathDir = "/storage/emulated/0/Pictures/LockWallpaper";
        final File fileDir = new File(pathDir);
        String[] strings = clickImage.getImagePath().split("/");
        final String imageName = strings[strings.length - 1];
        requestRuntimePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
            @Override
            public void onGranted() {
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                final String destFile = fileDir + "/" + imageName + ".png";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Utility.copyFile(clickImage.getImagePath(), destFile);
                    }
                }).start();
            }

            @Override
            public void onDenied(List<String> deniedPermission) {
                Toast.makeText(MainActivity.this, "请给予SD卡读写权限", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onTouch(Image image, float x, float y) {
        clickImage = image;
        popupWindow.showAtLocation(rootView, Gravity.START, (int) x, (int) y);
    }


    /**
     * 在按一次退出
     */
    public void pressBack() {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            PermissionUtils.repayPermission(new CompleteListener() {
                @Override
                public void onCompleted() {
                    finish();
                }
            });
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        pressBack();
    }
}
