package com.shirlman.yiplayer.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by KB-Server on 2016/6/25.
 */
public class VideoGroup implements Serializable {
    private List<VideoInfo> videoInfoList;
    private String groupName;

    public List<VideoInfo> getVideoInfoList() {
        return videoInfoList;
    }

    public void setVideoInfoList(List<VideoInfo> videoInfoList) {
        this.videoInfoList = videoInfoList;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
