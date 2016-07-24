/**
 * Created by Shirlman on 2016/7/9.
 * Visit www.shirlman.com for more information.
 */

package org.videolan.libvlc.media;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PixelFormat;

import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.MediaController;


import org.videolan.libvlc.*;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.subtitle.TimedTextProcessor;

public class VideoView extends SurfaceView
        implements MediaController.MediaPlayerControl, IVLCVout.Callback, MediaPlayer.EventListener {

    private final String TAG = VideoView.class.getName();

    private static LibVLC mLibVLC;
    private MediaPlayer mMediaPlayer;
    private int mVideoWidth;
    private int mVideoHeight;
    private int mTotalTime = 0;
    private SurfaceView mSubtitlesView;
    private boolean mPausable;
    private int mBufferPercentage;
    private String mTimedTextPath;
    private Uri mTimedTextUri;
    private TimedTextProcessor mTimedTextProcessor;

    private final int MSG_PREPARED = 0;
    private final int MSG_COMPLETION = 1;
    private final int MSG_ERROR = 2;
    private final int MSG_BUFFERING_UPDATE = 3;
    private final int MSG_CURRENT_TIME_UPDATE = 4;

    private OnCompletionListener mOnCompletionListener;
    private OnPreparedListener mOnPreparedListener;
    private OnErrorListener mOnErrorListener;
    private OnBufferingUpdateListener mOnBufferingUpdateListener;
    private OnCurrentTimeUpdateListener mOnCurrentTimeUpdateListener;
    private OnTimedTextListener mOnTimedTextListener;

    public interface OnCompletionListener {
        void onCompletion();
    }

    public interface OnPreparedListener {
        void onPrepared();
    }

    public interface OnErrorListener {
        void onError();
    }

    public interface OnBufferingUpdateListener {
        void onBufferingUpdate(int percent);
    }

    public interface OnCurrentTimeUpdateListener {
        void onCurrentTimeUpdate(int currentTime);
    }

    public interface OnTimedTextListener {
        void onTimedText(Spanned spanned);
    }

    public void setOnPreparedListener(OnPreparedListener onPreparedListener) {
        mOnPreparedListener = onPreparedListener;
    }

    public void setOnCompletionListener(OnCompletionListener onCompletionListener) {
        mOnCompletionListener = onCompletionListener;
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        mOnErrorListener = onErrorListener;
    }

    public void setOnBufferingUpdateListener(OnBufferingUpdateListener onBufferingUpdateListener) {
        mOnBufferingUpdateListener = onBufferingUpdateListener;
    }

    public void setOnCurrentTimeUpdateListener(OnCurrentTimeUpdateListener onCurrentTimeUpdateListener) {
        mOnCurrentTimeUpdateListener = onCurrentTimeUpdateListener;
    }

    public void setOnTimedTextListener(OnTimedTextListener onTimedTextListener) {
        mOnTimedTextListener = onTimedTextListener;
    }

    public void enableTimedText() {
        mTimedTextProcessor.resume();
    }

    public void disableTimedText() {
        mTimedTextProcessor.pause();
    }

    public void updateTimedText() {
        mTimedTextProcessor.updateTimedText();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage (Message msg) {
            switch(msg.what) {
                case MSG_PREPARED:
                    if(mOnPreparedListener != null) {
                        mOnPreparedListener.onPrepared();
                    }

                    break;
                case MSG_COMPLETION:
                    if(mOnCompletionListener != null) {
                        mOnCompletionListener.onCompletion();
                    }

                    break;
                case MSG_ERROR:
                    if(mOnErrorListener != null) {
                        mOnErrorListener.onError();
                    }

                    break;
                case MSG_BUFFERING_UPDATE:
                    if(mOnBufferingUpdateListener != null) {
                        mOnBufferingUpdateListener.onBufferingUpdate(mBufferPercentage);
                    }

                    break;
                case MSG_CURRENT_TIME_UPDATE:
                    if(mOnCurrentTimeUpdateListener != null) {
                        mOnCurrentTimeUpdateListener.onCurrentTimeUpdate(getCurrentPosition());
                    }

                    break;
            }
        }
    };

    public VideoView(Context context) {
        super(context);

        init();
    }

    public VideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

        init();
    }

    public VideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);

        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {
        mLibVLC = new LibVLC();
        mMediaPlayer = new MediaPlayer(mLibVLC);

        mTimedTextProcessor = new TimedTextProcessor() {
            @Override
            public void onTimedText(Spanned spanned) {
                if(mOnTimedTextListener != null) {
                    mOnTimedTextListener.onTimedText(spanned);
                }
            }

            @Override
            public int getCurrentPosition() {
                return VideoView.this.getCurrentPosition();
            }
        };

        getHolder().setKeepScreenOn(true);
    }

    public void setVideoPath(String path) {
        Media media = new Media(mLibVLC, path);
        mMediaPlayer.setMedia(media);
    }

    public void setVideoURI(Uri uri) {
        Media media = new Media(mLibVLC, uri);
        mMediaPlayer.setMedia(media);
    }

    public void addSubtitleSource(SurfaceView subtitlesView, String path) {
        mMediaPlayer.addSlave(Media.Slave.Type.Subtitle, path, false);
        setSubtitlesSurfaceView(subtitlesView);
    }

    public void addSubtitleSource(SurfaceView subtitlesView, Uri uri) {
        mMediaPlayer.addSlave(Media.Slave.Type.Subtitle, uri, false);
        setSubtitlesSurfaceView(subtitlesView);
    }

    public void addTimedTextSource(String path) {
        mTimedTextPath = path;
    }

    public void addTimedTextSource(Uri uri) {
        mTimedTextUri = uri;
    }

    private void setSubtitlesSurfaceView(SurfaceView surfaceView) {
        mSubtitlesView = surfaceView;
        mSubtitlesView.setZOrderMediaOverlay(true);
        mSubtitlesView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mSubtitlesView.invalidate();
    }

    @Override
    public void start() {
        mMediaPlayer.getVLCVout().addCallback(this);
        mMediaPlayer.getVLCVout().setVideoView(this);

        if(mSubtitlesView != null) {
            mMediaPlayer.getVLCVout().setSubtitlesView(mSubtitlesView);
        }

        if(mTimedTextPath != null) {
            mTimedTextProcessor.start(mTimedTextPath);
        } else if(mTimedTextUri != null) {
            mTimedTextProcessor.start(mTimedTextUri);
        }

        mMediaPlayer.getVLCVout().attachViews();

        mMediaPlayer.setEventListener(this);

        mMediaPlayer.play();
    }

    @Override
    public void pause() {
        mMediaPlayer.pause();

        mTimedTextProcessor.pause();
    }

    public void resume() {
        if(mMediaPlayer.getVLCVout().areViewsAttached()) {
            mMediaPlayer.play();
        } else {
            start();
        }

        mTimedTextProcessor.resume();
    }

    public void stop() {
        mMediaPlayer.stop();

        mTimedTextProcessor.stop();
    }

    public void release() {
        mMediaPlayer.stop();
        mMediaPlayer.release();

        mTimedTextProcessor.stop();
    }

    @Override
    public int getDuration() {
        int duration;

        if(!mMediaPlayer.isReleased()) {
            duration = (int) mMediaPlayer.getLength();
        } else {
            duration = 0;
        }

        return duration;
    }

    @Override
    public int getCurrentPosition() {
        int currentPosition;

        if(!mMediaPlayer.isReleased()) {
            currentPosition = (int) mMediaPlayer.getTime();
        } else {
            currentPosition = 0;
        }

        return currentPosition;
    }

    @Override
    public void seekTo(int milliSeconds) {
        if(mMediaPlayer.isSeekable()) {
            if(milliSeconds > mTotalTime) {
                milliSeconds = mTotalTime;
            }

            mMediaPlayer.setTime(milliSeconds);

            mTimedTextProcessor.updateTimedText();
        }
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return mBufferPercentage;
    }

    @Override
    public boolean canPause() {
        return mPausable;
    }

    @Override
    public boolean canSeekBackward() {
        return mMediaPlayer.isSeekable();
    }

    @Override
    public boolean canSeekForward() {
        return mMediaPlayer.isSeekable();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public int getAudioSessionId() {
        return mMediaPlayer.getAudioTrack();
    }

    @Override
    public void onNewLayout(IVLCVout vlcVout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {
        mTotalTime = (int) mMediaPlayer.getLength();

        mVideoWidth = width;
        mVideoHeight = height;

        setVideoSize();

        mHandler.obtainMessage(MSG_PREPARED).sendToTarget();
    }

    private void setVideoSize() {
        int viewWidth = this.getWidth();
        int viewHeight = this.getHeight();

        int layoutWidth = viewWidth;
        int layoutHeight = viewHeight;

        float videoRatio = mVideoWidth / (float) mVideoHeight;
        float screenRatio = viewWidth / (float) viewHeight;

        if(videoRatio > screenRatio) {
            layoutHeight = (int) Math.ceil(viewWidth / videoRatio);
        } else {
            layoutWidth = (int) Math.ceil(viewHeight * videoRatio);
        }

        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        layoutParams.width  = layoutWidth;
        layoutParams.height = layoutHeight;
        this.setLayoutParams(layoutParams);
    }

    @Override
    public void onSurfacesCreated(IVLCVout vlcVout) {

    }

    @Override
    public void onSurfacesDestroyed(IVLCVout vlcVout) {
    }

    @Override
    public void onHardwareAccelerationError(IVLCVout vlcVout) {

    }

    @Override
    public void onEvent(MediaPlayer.Event event) {
        try {
            mPausable = event.getPausable();
            mBufferPercentage = (int) event.getBuffering();

            mHandler.obtainMessage(MSG_BUFFERING_UPDATE).sendToTarget();
            mHandler.obtainMessage(MSG_CURRENT_TIME_UPDATE).sendToTarget();

            int playerState = mMediaPlayer.getPlayerState();

            switch (playerState) {
                case Media.State.Ended:
                    mHandler.obtainMessage(MSG_COMPLETION).sendToTarget();

                    break;
                case Media.State.Error:
                    mHandler.obtainMessage(MSG_ERROR).sendToTarget();

                    break;
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }
}
