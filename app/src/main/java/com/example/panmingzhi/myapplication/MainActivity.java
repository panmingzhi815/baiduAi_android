package com.example.panmingzhi.myapplication;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener, Runnable {

    private Map<String, Long> lastPlayTime = new HashMap<>();
    private final static Logger LOGGER = LoggerFactory.getLogger(MainActivity.class);
    private TextureView textureView;
    private Camera2 camera2;
    private MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textureView = findViewById(R.id.textureView);
        textureView.setSurfaceTextureListener(this);
        ActionBar actionBar = this.getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        camera2 = new Camera2(this,new Surface(surfaceTexture));
        camera2.openCamera();
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(this, 3000, 200, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }


    @Override
    public void run() {
        try {
            LOGGER.debug("正在识别中...");
            Bitmap temp = textureView.getBitmap();
            Bitmap bitmap = Images.zoomImg(temp, textureView.getWidth() / 8, textureView.getHeight() / 8);
            byte[] byteArray = Images.bitmap2bytes(bitmap);

            temp.recycle();
            bitmap.recycle();

            List<IdentifyUserResult> identifyUserResults = BaiduFaceAi.getInstance().identifyUser(byteArray);
            for (IdentifyUserResult identifyUserResult : identifyUserResults) {
                if (identifyUserResult.getScores()[0] < 65f){
                    continue;
                }
                File mp3 = new File(this.getFilesDir(), identifyUserResult.getUser_info() + ".mp3");
                if (mp3.exists()) {
                    playUserInfo(mp3);
                    continue;
                }

                BaiduTtsAi.getInstance().tts(mp3,identifyUserResult.getUser_info());

            }
        } catch (Exception e) {
            LOGGER.error("识别时发生错误",e);
        }
    }

    private void playUserInfo(File mp3) {
        Long currentTime = System.currentTimeMillis();
        Long lastTime = lastPlayTime.get(mp3.getName());
        if (lastTime != null) {
            if(currentTime - lastTime < 1500){
                return;
            }
        }
        lastPlayTime.put(mp3.getName(),currentTime);
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(mp3.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            Log.e("BAIDU_FACE_AI","播报异常");
        }finally {

        }

    }


}
