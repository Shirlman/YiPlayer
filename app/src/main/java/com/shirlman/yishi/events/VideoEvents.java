package com.shirlman.yishi.events;

import com.shirlman.yishi.models.VideoGroup;
import com.shirlman.yishi.models.VideoInfo;

import java.util.List;

/**
 * Created by KB-Server on 2016/6/24.
 */
public class VideoEvents {
    public static class OnLocalVideoListGot {
        private List<VideoGroup> videoGroupList;

        public OnLocalVideoListGot(List<VideoGroup> videoGroupList) {
            this.videoGroupList = videoGroupList;
        }

        public List<VideoGroup> getVideoGroupList() {
            return videoGroupList;
        }
    }

    public static class OpenVideoGroup {
        private VideoGroup videoGroup;

        public OpenVideoGroup(VideoGroup videoGroup) {
            this.videoGroup = videoGroup;
        }

        public VideoGroup getVideoGroup() {
            return videoGroup;
        }
    }

    public static class PlayLocalVideo {
        private VideoInfo videoInfo;

        public PlayLocalVideo(VideoInfo videoInfo) {
            this.videoInfo = videoInfo;
        }

        public VideoInfo getVideoInfo() {
            return videoInfo;
        }
    }
}
