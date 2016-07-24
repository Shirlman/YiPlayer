package com.shirlman.yiplayer.jobs;

import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.shirlman.yiplayer.MyApplication;
import com.shirlman.yiplayer.events.VideoEvents;
import com.shirlman.yiplayer.models.VideoGroup;
import com.shirlman.yiplayer.models.VideoInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by KB-Server on 2016/6/24.
 */
public class GetLocalVideoListJob extends Job {
    public GetLocalVideoListJob() {
        super(new Params(JobPriority.HIGH).requireNetwork());
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        HashMap<String, VideoGroup> videoGroupHashMap = new HashMap<>();

        String[] thumbColumns = {
                MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID
        };

        String[] mediaColumns = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE,
        };

        Cursor cursor = MyApplication.getInstance().getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                VideoInfo videoInfo = new VideoInfo();

                int mediaId = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID));

                Cursor thumbCursor = MyApplication.getInstance().getContentResolver().query(
                        MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, thumbColumns,
                        MediaStore.Video.Thumbnails.VIDEO_ID+ "=" + mediaId,
                        null,
                        null);

                if (thumbCursor != null && thumbCursor.moveToFirst()) {
                    videoInfo.setThumbPath(thumbCursor.getString(thumbCursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA)));
                }

                videoInfo.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)));
                videoInfo.setDisplayName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));

                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                videoInfo.setDuration(duration);

                videoInfo.setSize(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)));

                String videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                videoInfo.setPath(videoPath);

                String videoFolderPath = videoPath.substring(0, videoPath.lastIndexOf("/"));
                videoInfo.setFolderPath(videoFolderPath);

                String videoGroupName = videoFolderPath.substring(videoFolderPath.lastIndexOf("/") + 1);

                if(!videoGroupHashMap.containsKey(videoGroupName)) {
                    VideoGroup videoGroup = new VideoGroup();
                    videoGroup.setGroupName(videoGroupName);

                    List<VideoInfo> videoInfoList = new ArrayList<>();
                    videoInfoList.add(videoInfo);
                    videoGroup.setVideoInfoList(videoInfoList);

                    videoGroupHashMap.put(videoGroupName, videoGroup);
                } else {
                    videoGroupHashMap.get(videoGroupName).getVideoInfoList().add(videoInfo);
                }
            } while (cursor.moveToNext());
        }

        EventBus.getDefault().post(new VideoEvents.OnLocalVideoListGot(new ArrayList<>(videoGroupHashMap.values())));
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}
