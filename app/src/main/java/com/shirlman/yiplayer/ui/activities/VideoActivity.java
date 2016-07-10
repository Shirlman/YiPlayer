package com.shirlman.yiplayer.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shirlman.yiplayer.R;
import com.shirlman.yiplayer.models.VideoInfo;
import com.shirlman.yiplayer.util.StringUtils;

import org.videolan.libvlc.media.VideoView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by KB-Server on 2016/6/22.
 */
public class VideoActivity extends AppCompatActivity {
    private VideoView mVideoView;
    private VideoInfo mVideoInfo;
    private SurfaceView mSubtitleView;

    // Video controller
    private View mVideoControllerLayout;
    private View mVideoControllerRootView;
    private TextView mVideoControllerVideoTitle;
    private TextView mVideoControllerCurrentTime;
    private TextView mVideoControllerTotalTime;
    private SeekBar mVideoControllerVideoSeekBar;
    private ImageButton mVideoControllerPlayOrPause;
    private ImageButton mVideoControllerVideoLock;
    private boolean mIsLocked;
    private boolean mIsPaused;
    private boolean mIsTouchingVideoSeekBar;
    private boolean mIsVideoControllerShowing;
    private Timer mHideVideoControllerTimer;
    private int mDefaultLockImageSize;

    // Handler message
    private final int MSG_HIDE_VIDEO_CONTROLLER = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mVideoInfo = (VideoInfo) getIntent().getExtras().getSerializable(VideoInfo.class.getSimpleName());

        if(mVideoInfo == null) {
            Toast.makeText(this, "No video!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.video_view);

        mVideoView = (VideoView) findViewById(R.id.video_view);
        mSubtitleView = (SurfaceView) findViewById(R.id.subtitle_view);

        // Video controller
        mVideoControllerLayout = findViewById(R.id.video_controller_layout);
        mVideoControllerRootView = findViewById(R.id.video_controller);
        mVideoControllerVideoTitle = (TextView) mVideoControllerRootView.findViewById(R.id.video_controller_video_title);
        mVideoControllerCurrentTime = (TextView) mVideoControllerRootView.findViewById(R.id.video_controller_current_time);
        mVideoControllerTotalTime = (TextView) mVideoControllerRootView.findViewById(R.id.video_controller_total_time);
        mVideoControllerVideoSeekBar = (SeekBar) mVideoControllerRootView.findViewById(R.id.video_controller_seek_bar);
        mVideoControllerPlayOrPause = (ImageButton) mVideoControllerRootView.findViewById(R.id.video_controller_play_pause);
        mVideoControllerVideoLock = (ImageButton) mVideoControllerRootView.findViewById(R.id.video_controller_lock);
        mVideoControllerRootView.setVisibility(View.GONE);

        String videoPath = mVideoInfo.getPath();
        mVideoView.setVideoPath(videoPath);
        mVideoView.addTimedTextSource(mSubtitleView, videoPath);

        mVideoView.setOnPreparedListener(mOnPreparedListener);

        mVideoView.start();
    }

    @Override
    protected void onPause() {
        mVideoView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(!mIsPaused) {
            mVideoView.resume();
        }

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mVideoView.release();
        super.onDestroy();
    }

    private void initVideoController() {
        mVideoControllerLayout.setOnClickListener(mVideoControllerOnClickListener);
        mVideoControllerVideoTitle.setText(mVideoInfo.getTitle());
        mVideoControllerTotalTime.setText(StringUtils.getTimeDisplayString(mVideoView.getDuration()));
        mVideoControllerPlayOrPause.setOnClickListener(mOnPlayOrPauseClickListener);
        mVideoControllerVideoLock.setOnClickListener(mOnLockClickListener);
        mVideoControllerVideoSeekBar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);

        mVideoView.setOnCurrentTimeUpdateListener(mOnCurrentTimeUpdateListener);
        mVideoView.setOnCompletionListener(mOnCompletionListener);
    }

    private void startHideVideoControllerTimer() {
        if(mHideVideoControllerTimer != null) {
            mHideVideoControllerTimer.cancel();
        }

        mHideVideoControllerTimer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.obtainMessage(MSG_HIDE_VIDEO_CONTROLLER).sendToTarget();
            }
        };

        mHideVideoControllerTimer.schedule(timerTask, 3000);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_HIDE_VIDEO_CONTROLLER:
                    hideVideoController();

                    break;
            }
        }
    };

    private void showVideoController() {
        mVideoControllerRootView.setVisibility(View.VISIBLE);
        startHideVideoControllerTimer();
        mIsVideoControllerShowing = true;
    }

    private void hideVideoController() {
        mVideoControllerRootView.setVisibility(View.INVISIBLE);
        mIsVideoControllerShowing = false;
    }

    private View.OnClickListener mVideoControllerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mIsVideoControllerShowing) {
                hideVideoController();
            } else {
                showVideoController();
            }
        }
    };

    private VideoView.OnPreparedListener mOnPreparedListener = new VideoView.OnPreparedListener() {
        @Override
        public void onPrepared() {
            initVideoController();
        }
    };

    private VideoView.OnCompletionListener mOnCompletionListener = new VideoView.OnCompletionListener() {
        @Override
        public void onCompletion() {
            finish();
        }
    };

    private VideoView.OnCurrentTimeUpdateListener mOnCurrentTimeUpdateListener = new VideoView.OnCurrentTimeUpdateListener() {
        @Override
        public void onCurrentTimeUpdate(int currentTime) {
            if(!mIsTouchingVideoSeekBar) {
                mVideoControllerCurrentTime.setText(StringUtils.getTimeDisplayString(currentTime));
                mVideoControllerVideoSeekBar.setProgress(currentTime * 100 / mVideoView.getDuration());
            }
        }
    };

    private View.OnClickListener mOnPlayOrPauseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mIsPaused = !mIsPaused;

            if(mIsPaused) {
                mVideoView.pause();
                mVideoControllerPlayOrPause.setImageResource(R.drawable.video_controller_play);
            } else {
                mVideoView.resume();
                mVideoControllerPlayOrPause.setImageResource(R.drawable.video_controller_pause);
            }

            startHideVideoControllerTimer();
        }
    };

    private View.OnClickListener mOnLockClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            View videoControllerTop = mVideoControllerRootView.findViewById(R.id.video_controller_top);
            View videoControllerBottom = mVideoControllerRootView.findViewById(R.id.video_controller_bottom);
            View videoControllerRight = mVideoControllerRootView.findViewById(R.id.video_controller_right);

            mIsLocked = !mIsLocked;
            int lockImageSize;

            if(mIsLocked) {
                mDefaultLockImageSize = mVideoControllerVideoLock.getWidth();
                lockImageSize = (int)(mDefaultLockImageSize * 1.5);

                videoControllerTop.setVisibility(View.INVISIBLE);
                videoControllerBottom.setVisibility(View.INVISIBLE);
                videoControllerRight.setVisibility(View.INVISIBLE);
            } else {
                lockImageSize = mDefaultLockImageSize;

                videoControllerTop.setVisibility(View.VISIBLE);
                videoControllerBottom.setVisibility(View.VISIBLE);
                videoControllerRight.setVisibility(View.VISIBLE);
            }

            ViewGroup.LayoutParams layoutParams = mVideoControllerVideoLock.getLayoutParams();
            layoutParams.width = lockImageSize;
            layoutParams.height = lockImageSize;
            mVideoControllerVideoLock.setLayoutParams(layoutParams);

            startHideVideoControllerTimer();
        }
    };

    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean manual) {
            if(manual) {
                mVideoControllerCurrentTime.setText(
                        StringUtils.getTimeDisplayString(progress * mVideoView.getDuration() / 100));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mIsTouchingVideoSeekBar = true;

            mHideVideoControllerTimer.cancel();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mIsTouchingVideoSeekBar = false;
            mVideoView.seekTo(seekBar.getProgress() * mVideoView.getDuration() / 100);

            startHideVideoControllerTimer();
        }
    };
}
