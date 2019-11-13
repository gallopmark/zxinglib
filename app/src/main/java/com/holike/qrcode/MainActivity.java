package com.holike.qrcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import pony.xcode.zxing.CaptureHelper;
import pony.xcode.zxing.OnCaptureCallback;
import pony.xcode.zxing.ViewfinderView;

public class MainActivity extends AppCompatActivity implements OnCaptureCallback {
    private SurfaceView surfaceView;
    private ViewfinderView viewfinderView;
    private CaptureHelper mHelper;
    private Button button;
    private boolean isOpenFlash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surfaceView = findViewById(R.id.surfaceView);
        viewfinderView = findViewById(R.id.viewfinderView);
        button = findViewById(R.id.bt_switch_flash);
        viewfinderView.setFrameRatio(0.5f);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpenFlash) {
                    mHelper.switchFlash(false);
                    isOpenFlash = false;
                } else {
                    mHelper.switchFlash(true);
                    isOpenFlash = true;
                }
            }
        });
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//            init();
//        } else
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 10086);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            init();
            init();
            mHelper.onResume();
        }
    }

    private void init() {
        mHelper = new CaptureHelper(this, surfaceView, viewfinderView)
                .vibrate(true)
                .playBeep(true)
                .fullScreenScan(false)//全屏扫码
                .supportVerticalCode(true)//支持扫垂直条码，建议有此需求时才使用。
                .continuousScan(true)
                .setOnCaptureCallback(this);
        mHelper.onCreate();
    }

    @Override
    public boolean onResultCallback(String result) {
        if (!TextUtils.isEmpty(result))
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mHelper != null)
            mHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mHelper != null)
            mHelper.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mHelper != null)
            mHelper.onDestroy();
        super.onDestroy();
    }
}
