package pony.xcode.zxing;

import android.graphics.Bitmap;

import com.google.zxing.Result;

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public interface OnCaptureListener {


    /**
     * 接收解码后的扫码结果
     */
    void onHandleDecode(Result result, Bitmap barcode, float scaleFactor);


}
