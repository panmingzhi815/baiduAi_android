package com.example.panmingzhi.myapplication;

import android.content.Context;
import android.util.Log;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.baidu.aip.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Created by panmingzhi on 2018/4/28.
 */

public class BaiduTtsAi {

    public static final String APP_ID = "11151504";
    public static final String API_KEY = "kdh2shLbaPyehC9PD1KyZGn9";
    public static final String SECRET_KEY = "28f29c73f89f1c776f7404212acf0eaa";

    private static BaiduTtsAi baiduTtsAi = new BaiduTtsAi();

    private AipSpeech client;

    private BaiduTtsAi(){}

    public static BaiduTtsAi getInstance() {
        return baiduTtsAi;
    }

    public void tts(File mp3, String voice) throws IOException {
        if (client == null) {
            client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
        }


        // 调用接口
        TtsResponse res = client.synthesis(voice, "zh", 1, null);
        byte[] data = res.getData();
        JSONObject res1 = res.getResult();
        if (data != null) {
            try {
                Util.writeBytesToFileSystem(data, mp3.getPath());
            } catch (Exception e) {
                Log.e("BaiduTtsAi","语音合成失败",e);
            }
        }
        throw new IOException("语音合成失败");
    }
}
