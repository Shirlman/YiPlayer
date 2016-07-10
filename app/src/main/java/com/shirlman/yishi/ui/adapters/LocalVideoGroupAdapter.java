package com.shirlman.yishi.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shirlman.yishi.R;
import com.shirlman.yishi.events.VideoEvents;
import com.shirlman.yishi.models.VideoGroup;
import com.shirlman.yishi.models.VideoInfo;
import com.shirlman.yishi.util.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by KB-Server on 2016/6/25.
 */
public class LocalVideoGroupAdapter extends RecyclerView.Adapter <LocalVideoGroupAdapter.ViewHolder>{
    private List<VideoGroup> mVideoGroupList;
    private LayoutInflater mLayoutInflater;

    public LocalVideoGroupAdapter(Context context, List<VideoGroup> videoGroupList) {
        mVideoGroupList = videoGroupList;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = mLayoutInflater.inflate(R.layout.local_video_item, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final VideoGroup videoGroup = mVideoGroupList.get(position);
        VideoInfo firstVideoInfo = videoGroup.getVideoInfoList().get(0);

        ImageLoader.getInstance().displayImage("file://" + firstVideoInfo.getThumbPath(), holder.firstVideoThumb);

        holder.firstVideoDuration.setText(StringUtils.getTimeDisplayString(firstVideoInfo.getDuration()));
        holder.videoGroupName.setText(videoGroup.getGroupName());

        String videoCount = String.format(
                holder.itemView.getResources().getString(R.string.local_video_count),
                videoGroup.getVideoInfoList().size());
        holder.videoCount.setText(videoCount);

        holder.firstVideoThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new VideoEvents.OpenVideoGroup(videoGroup));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVideoGroupList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton firstVideoThumb;
        public TextView videoGroupName;
        public TextView videoCount;
        public TextView firstVideoDuration;

        public ViewHolder(View itemView) {
            super(itemView);

            firstVideoThumb = (ImageButton) itemView.findViewById(R.id.local_video_thumb);
            videoGroupName = (TextView) itemView.findViewById(R.id.local_video_title);
            videoCount = (TextView) itemView.findViewById(R.id.local_video_info);
            firstVideoDuration = (TextView) itemView.findViewById(R.id.local_video_duration);
        }
    }
}
