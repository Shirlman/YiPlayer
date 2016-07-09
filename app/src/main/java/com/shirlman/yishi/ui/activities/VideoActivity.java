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

/**
 * Created by KB-Server on 2016/6/22.
 */
public class VideoActivity extends AppCompatActivity {
    private long mPosition;
    private TextView mSubtitleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final VideoInfo videoInfo = (VideoInfo) getIntent().getExtras().getSerializable(VideoInfo.class.getSimpleName());

        setContentView(R.layout.video_view);

        mSubtitleView = (TextView) findViewById(R.id.video_subtitle);

    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
