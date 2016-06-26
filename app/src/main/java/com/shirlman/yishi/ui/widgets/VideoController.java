package com.shirlman.yishi.ui.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.shirlman.yishi.R;
import com.shirlman.yishi.models.VideoInfo;

import io.vov.vitamio.widget.MediaController;

/**
 * Created by KB-Server on 2016/6/25.
 */
public class VideoController extends MediaController{
    private Context mContext;
    private View mRootView;
    private boolean isLocked;

    public VideoController(Context context, VideoInfo videoInfo) {
        super(context);

        mContext = context;
    }

    @Override
    protected View makeControllerView() {
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.video_controller, this);

        initControllerView();

        return mRootView;
    }

    private void initControllerView() {
        mRootView.findViewById(R.id.video_controller).setOnClickListener(onControllerClickListener);
        mRootView.findViewById(R.id.video_lock).setOnClickListener(onLockClickListener);
        mRootView.findViewById(R.id.video_settings).setOnClickListener(onSettingClickListener);
        mRootView.findViewById(R.id.video_next).setOnClickListener(onNextClickListener);
        mRootView.findViewById(R.id.mediacontroller_back).setOnClickListener(onBackClickListener);
    }

    private OnClickListener onControllerClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            VideoController.this.hide();
        }
    };

    private OnClickListener onLockClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            View videoLockView = mRootView.findViewById(R.id.video_lock);
            View videoControllerTop = mRootView.findViewById(R.id.video_controller_top);
            View videoControllerBottom = mRootView.findViewById(R.id.video_controller_bottom);
            View videoControllerRight = mRootView.findViewById(R.id.video_controller_right);

            if(!isLocked) {
                videoLockView.setScaleX(1.5f);
                videoLockView.setScaleY(1.5f);

                videoControllerTop.setVisibility(INVISIBLE);
                videoControllerBottom.setVisibility(INVISIBLE);
                videoControllerRight.setVisibility(INVISIBLE);
            } else {
                videoLockView.setScaleX(1f);
                videoLockView.setScaleY(1f);

                videoControllerTop.setVisibility(VISIBLE);
                videoControllerBottom.setVisibility(VISIBLE);
                videoControllerRight.setVisibility(VISIBLE);
            }

            isLocked = !isLocked;
        }
    };

    private OnClickListener onNextClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private OnClickListener onSettingClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private OnClickListener onBackClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
