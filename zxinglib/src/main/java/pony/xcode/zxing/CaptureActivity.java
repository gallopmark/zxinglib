/*
 * Copyright (C) 2018 Jenly Yu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pony.xcode.zxing;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import pony.xcode.zxing.camera.CameraManager;


public class CaptureActivity extends AppCompatActivity implements OnCaptureCallback {


    private CaptureHelper mCaptureHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = getLayoutId();
        if (isContentView(layoutId)) {
            setContentView(layoutId);
        }
        initUI();
    }

    /**
     * 初始化
     */
    public void initUI() {
        SurfaceView surfaceView = findViewById(getSurfaceViewId());
        ViewfinderView viewfinderView = findViewById(getViewfinderViewId());
        mCaptureHelper = new CaptureHelper(this, surfaceView, viewfinderView);
        mCaptureHelper.setOnCaptureCallback(this);
    }

    /**
     * 返回true时会自动初始化{@link #setContentView(int)}，返回为false是需自己去初始化{@link #setContentView(int)}
     *
     * @return 默认返回true
     */
    public boolean isContentView(@LayoutRes int layoutId) {
        return true;
    }

    /**
     * 布局id
     */
    public int getLayoutId() {
        return R.layout.zxl_capture;
    }

    /**
     * {@link ViewfinderView} 的 id
     */
    public int getViewfinderViewId() {
        return R.id.viewfinderView;
    }


    /**
     * 预览界面{@link SurfaceView} 的id
     */
    public int getSurfaceViewId() {
        return R.id.surfaceView;
    }

    /**
     * Get {@link CaptureHelper}
     *
     * @return {@link #mCaptureHelper}
     */
    public CaptureHelper getCaptureHelper() {
        return mCaptureHelper;
    }

    /**
     * Get {@link CameraManager}
     *
     * @return {@link #mCaptureHelper#getCameraManager()}
     */
    public CameraManager getCameraManager() {
        return mCaptureHelper.getCameraManager();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCaptureHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCaptureHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCaptureHelper.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mCaptureHelper.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * 接收扫码结果回调
     *
     * @param result 扫码结果
     * @return 返回true表示拦截，将不自动执行后续逻辑，为false表示不拦截，默认不拦截
     */
    @Override
    public boolean onResultCallback(String result) {
        return false;
    }
}