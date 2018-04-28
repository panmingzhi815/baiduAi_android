package com.example.panmingzhi.myapplication;

/**
 * 人脸识别结果
 * Created by panmingzhi on 2018/4/28.
 */

class IdentifyUserResult {
    private String uid;
    private String user_info;
    private Double[] scores;

    public IdentifyUserResult(String uid, String user_info, Double[] scores) {
        this.uid = uid;
        this.user_info = user_info;
        this.scores = scores;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser_info() {
        return user_info;
    }

    public void setUser_info(String user_info) {
        this.user_info = user_info;
    }

    public Double[] getScores() {
        return scores;
    }

    public void setScores(Double[] scores) {
        this.scores = scores;
    }
}
