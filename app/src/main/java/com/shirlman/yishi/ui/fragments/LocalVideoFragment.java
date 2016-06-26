package com.shirlman.yishi.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shirlman.yishi.R;
import com.shirlman.yishi.models.VideoGroup;
import com.shirlman.yishi.ui.adapters.LocalVideoAdapter;
import com.shirlman.yishi.ui.adapters.LocalVideoGroupAdapter;

/**
 * Created by KB-Server on 2016/6/24.
 */
public class LocalVideoFragment extends Fragment {
    private View mRootView;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        VideoGroup videoGroup = (VideoGroup) getArguments().getSerializable(VideoGroup.class.getSimpleName());

        mRootView = inflater.inflate(R.layout.local_video_fragment, container, false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.local_video_recycler_view);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);

        mRecyclerView.setAdapter(new LocalVideoAdapter(getActivity(), videoGroup));

        return mRootView;
    }
}
