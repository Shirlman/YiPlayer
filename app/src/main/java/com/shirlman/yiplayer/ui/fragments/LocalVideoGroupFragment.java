package com.shirlman.yiplayer.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shirlman.yiplayer.MyApplication;
import com.shirlman.yiplayer.R;
import com.shirlman.yiplayer.events.VideoEvents;
import com.shirlman.yiplayer.jobs.GetLocalVideoListJob;
import com.shirlman.yiplayer.models.VideoGroup;
import com.shirlman.yiplayer.ui.adapters.LocalVideoGroupAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by KB-Server on 2016/6/25.
 */
public class LocalVideoGroupFragment extends Fragment{
    private View mRootView;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        EventBus.getDefault().register(this);

        MyApplication.getJobManager().addJobInBackground(new GetLocalVideoListJob());

        mRootView = inflater.inflate(R.layout.local_video_fragment, container, false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.local_video_recycler_view);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);

        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(VideoEvents.OnLocalVideoListGot event) {
        List<VideoGroup> videoGroupList = event.getVideoGroupList();

        if(videoGroupList.size() > 0) {
            mRecyclerView.setAdapter(new LocalVideoGroupAdapter(getActivity(), videoGroupList));
        }
    }
}
