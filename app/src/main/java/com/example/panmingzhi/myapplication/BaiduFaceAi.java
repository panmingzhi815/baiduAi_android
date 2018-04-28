package com.example.panmingzhi.myapplication;

import android.util.Log;

import com.baidu.aip.face.AipFace;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 百度Ai
 * Created by panmingzhi on 2018/4/28.
 */

public class BaiduFaceAi {

    private static final String BAIDU_AI = "BAIDU_FACE_AI";
    private static final BaiduFaceAi BAIDU_FACE_AI = new BaiduFaceAi();
    private static final String APP_ID = "10578310";
    private static final String API_KEY = "k5GdSjCS2NGGOD2EACfBlON9";
    private static final String SECRET_KEY = "cuXGl8i3Uo5zHG47qC7Y73qKsFbd3g64";
    private static final String GROUP_ID = "test";
    private AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);

    private BaiduFaceAi(){}

    public static BaiduFaceAi getInstance() {
        return BAIDU_FACE_AI;
    }

    public List<IdentifyUserResult> identifyUser(byte[] image) {
        Log.i(BAIDU_AI,"识别大小：" + image.length * 1f / 1024);
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        // 参数为本地图片路径
        JSONObject res = client.identifyUser(GROUP_ID, image, options);
        if (res.isNull("result")) {
            return Collections.emptyList();
        }

        ArrayList<IdentifyUserResult> objects = new ArrayList<>();
        try {
            JSONArray result = res.getJSONArray("result");
            for (int i = 0; i < result.length(); i++) {
                JSONObject jsonObject = result.getJSONObject(i);
                JSONArray jsonArray = jsonObject.getJSONArray("scores");
                Double[] scores = new Double[jsonArray.length()];
                for (int j = 0; j < jsonArray.length(); j++) {
                    scores[j] = jsonArray.getDouble(j);
                }
                IdentifyUserResult identifyUserResult = new IdentifyUserResult(jsonObject.getString("uid"),jsonObject.getString("user_info"),scores);
                objects.add(identifyUserResult);
            }
            Log.i(BAIDU_AI,"识别结果 ：" + result.toString());
        } catch (JSONException e) {
            Log.i(BAIDU_AI,"识别异常",e);
        }finally {
            return objects;
        }
    }

}
