package com.shirlman.yishi.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.shirlman.yishi.R;
import com.shirlman.yishi.models.VideoInfo;
import com.shirlman.yishi.ui.widgets.VideoController;

import java.io.File;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by KB-Server on 2016/6/22.
 */
public class VideoActivity extends AppCompatActivity {
    private VideoView mVideoView;
    private long mPosition;
    private TextView mSubtitleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final VideoInfo videoInfo = (VideoInfo) getIntent().getExtras().getSerializable(VideoInfo.class.getSimpleName());

        Vitamio.isInitialized(getApplicationContext());

        setContentView(R.layout.video_view);
        mVideoView = (VideoView) findViewById(R.id.video_view);
        mSubtitleView = (TextView) findViewById(R.id.video_subtitle);

        mVideoView.setVideoPath(videoInfo.getPath());
        mVideoView.requestFocus();

        final VideoController videoController = new VideoController(this, videoInfo);
        mVideoView.setMediaController(videoController);

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // optional need Vitamio 4.0
                mediaPlayer.setPlaybackSpeed(1.0f);

                if (mPosition > 0) {
                    // 若不调用 VideoView.getDuration() 就进行 seekTo() 那么 seekTo() 无效！
                    mVideoView.getDuration();
                    mVideoView.seekTo(mPosition);
                }

                String subtitlePath = videoInfo.getPath().substring(0, videoInfo.getPath().lastIndexOf(".")) + ".srt";

                if(new File(subtitlePath).exists()) {
                    mVideoView.addTimedTextSource(subtitlePath);
                    mVideoView.setTimedTextShown(true);
                    mVideoView.setTimedTextEncoding(null);

                    mSubtitleView.setVisibility(View.VISIBLE);
                }
            }
        });

        mVideoView.setOnTimedTextListener(new MediaPlayer.OnTimedTextListener() {
            @Override
            public void onTimedText(String text) {
                mSubtitleView.setText(text);
            }

            @Override
            public void onTimedTextUpdate(byte[] pixels, int width, int height) {

            }
        });
    }

    @Override
    protected void onPause() {
        if(mVideoView != null && mVideoView.isPlaying()) {
            mPosition = mVideoView.getCurrentPosition();

            mVideoView.pause();
        }

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mVideoView != null) {
            mVideoView.stopPlayback();
        }
    }
}
