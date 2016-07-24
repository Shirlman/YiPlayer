package com.shirlman.yiplayer.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shirlman.yiplayer.R;
import com.shirlman.yiplayer.core.OnlineQueryService;
import com.shirlman.yiplayer.models.ICibaResponse;
import com.shirlman.yiplayer.models.ShooterSubtitleResponse;
import com.shirlman.yiplayer.models.VideoInfo;
import com.shirlman.yiplayer.models.YoudaoResponse;
import com.shirlman.yiplayer.ui.adapters.OnlineSubtitleAdapter;
import com.shirlman.yiplayer.util.StringUtils;

import org.videolan.libvlc.media.VideoView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;


/**
 * Created by KB-Server on 2016/6/22.
 */
public class VideoActivity extends AppCompatActivity {
    private final String TAG = VideoActivity.class.getName();

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
    private ImageButton mVideoControllerSettings;
    private boolean mIsLocked;
    private boolean mIsPaused;
    private boolean mIsTouchingVideoSeekBar;
    private boolean mIsVideoControllerShowing;
    private boolean mIsOnlyEnglishSubtitle;
    private Timer mHideVideoControllerTimer;
    private Timer mHideExplanationTimer;
    private int mDefaultLockImageSize;
    private TextView mTimedSubtitleView;
    private TextView mTranslationView;

    // Online subtitle
    private View mOnlineSubtitleButton;
    private View mOnlineSubtitleController;
    private View mOnlineSubtitleCloseButton;
    private EditText mOnlineSubtitleSearchBox;
    private View mOnlineSubtitleSearchButton;

    // Handler message
    private final int MSG_HIDE_VIDEO_CONTROLLER = 0;
    private final int MSG_HIDE_EXPLANATION = 1;

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
        mVideoControllerSettings = (ImageButton) mVideoControllerRootView.findViewById(R.id.video_controller_settings);
        mTimedSubtitleView = (TextView) findViewById(R.id.timed_subtitle_view);
        mTranslationView = (TextView) findViewById(R.id.word_translation_view);

        // Online subtitle
        mOnlineSubtitleButton = findViewById(R.id.online_subtitle_button);
        mOnlineSubtitleController = findViewById(R.id.online_subtitle_controller);
        mOnlineSubtitleCloseButton = findViewById(R.id.online_subtitle_close);
        mOnlineSubtitleSearchBox = (EditText) findViewById(R.id.online_subtitle_search_box);
        mOnlineSubtitleSearchButton = findViewById(R.id.online_subtitle_search_button);

        mTimedSubtitleView.setVisibility(View.INVISIBLE);
        mVideoControllerRootView.setVisibility(View.INVISIBLE);
        mOnlineSubtitleController.setVisibility(View.INVISIBLE);
        mTranslationView.setVisibility(View.INVISIBLE);

        String videoPath = mVideoInfo.getPath();
        mVideoView.setVideoPath(videoPath);

//        mVideoView.addSubtitleSource(mSubtitleView, videoPath);
        mVideoView.addTimedTextSource(videoPath);
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
        mVideoControllerVideoTitle.setText(mVideoInfo.getDisplayName());
        mVideoControllerTotalTime.setText(StringUtils.getTimeDisplayString(mVideoView.getDuration()));
        mVideoControllerPlayOrPause.setOnClickListener(mOnPlayOrPauseClickListener);
        mVideoControllerVideoLock.setOnClickListener(mOnLockClickListener);
        mVideoControllerSettings.setOnClickListener(mOnSettingsClickListener);

        mOnlineSubtitleButton.setOnClickListener(mOnlineSubtitleButtonClickListener);

        if(mVideoView.canSeekForward() || mVideoView.canSeekBackward()) {
            mVideoControllerVideoSeekBar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        } else {
            mVideoControllerVideoSeekBar.setEnabled(false);
        }

        mVideoView.setOnCurrentTimeUpdateListener(mOnCurrentTimeUpdateListener);
        mVideoView.setOnCompletionListener(mOnCompletionListener);
        mVideoView.setOnTimedTextListener(mOnTimedTextListener);
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

    private void startHideExplanationTimer() {
        if(mHideExplanationTimer != null) {
            mHideExplanationTimer.cancel();
        }

        mHideExplanationTimer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.obtainMessage(MSG_HIDE_EXPLANATION).sendToTarget();
            }
        };

        mHideExplanationTimer.schedule(timerTask, 3000);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_HIDE_VIDEO_CONTROLLER:
                    hideVideoController();

                    break;
                case MSG_HIDE_EXPLANATION:
                    mTranslationView.setVisibility(View.INVISIBLE);

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

    private VideoView.OnTimedTextListener mOnTimedTextListener = new VideoView.OnTimedTextListener() {
        @Override
        public void onTimedText(Spanned spanned) {
            if(spanned == null) {
                mTimedSubtitleView.setVisibility(View.INVISIBLE);
            } else {
                String timedText = spanned.toString();

                if(mIsOnlyEnglishSubtitle) {
                    String[] lines = timedText.split("\n");
                    List<String> lineList = new ArrayList<>(Arrays.asList(lines));

                    for(int i = lines.length - 1; i >= 0; i--) {
                        if(StringUtils.isChineseChar(lines[i])) {
                            // remove chinese subtitle
                            lineList.remove(i);
                        }
                    }

                    timedText = StringUtils.join(lineList, "\n");
                }

                if(StringUtils.notNullNorEmpty(timedText)) {
                    if(!mTimedSubtitleView.getText().toString().equals(timedText)) {
                        SpannableString spannableStringBuilder = getClickableSpan(timedText);

                        mTimedSubtitleView.setVisibility(View.VISIBLE);
                        mTimedSubtitleView.setText(spannableStringBuilder);
                        mTimedSubtitleView.setMovementMethod(LinkMovementMethod.getInstance());
                        mTimedSubtitleView.setLongClickable(false);
                    }
                } else {
                    mTimedSubtitleView.setVisibility(View.INVISIBLE);
                }
            }
        }
    };

    private SpannableString getClickableSpan(String str) {
        SpannableString spannableString = new SpannableString(str);

        char[] charArray = str.toCharArray();
        int wordBegin = -1;
        int wordEnd = -1;

        for(int i = 0; i < charArray.length; i++) {
            if((charArray[i] >= 'A' && charArray[i] <= 'Z') ||
                    (charArray[i] >= 'a' && charArray[i] <= 'z')) {

                if(wordBegin == -1) {
                    wordBegin = i;
                }
            } else if(charArray[i] != '\'' || i == charArray.length - 1){

                if(wordBegin != -1) {
                    wordEnd = i;
                }
            }

            if(wordBegin != -1 && wordEnd != -1) {
                // found word
                final String word = str.substring(wordBegin, wordEnd);

                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        queryWordFromICiba(word);
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);

                        ds.setUnderlineText(false);
                        ds.setColor(getResources().getColor(android.R.color.white));
                    }
                }, wordBegin, wordEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                wordBegin = -1;
                wordEnd = -1;
            }
        }

        return spannableString;
    }

    private void queryWordFromICiba(final String word) {
        String iCibaBaseUrl = "http://dict-co.iciba.com/api/dictionary.php/";
        String iCibaKey = "F49787A2BF3E1ED04BC4CEA3E04B5E44";

        Map<String, Object> searchFilters = new LinkedHashMap<>();
        searchFilters.put("key", iCibaKey);
        searchFilters.put("w", word.toLowerCase());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(iCibaBaseUrl)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        OnlineQueryService service = retrofit.create(OnlineQueryService.class);
        service.queryWordFromICiba(searchFilters).enqueue(new Callback<ICibaResponse>() {
            @Override
            public void onResponse(Call<ICibaResponse> call, Response<ICibaResponse> response) {
                String translation = StringUtils.join(response.body().getAcceptation(), "");

                if(translation.endsWith("\n")) {
                    translation = translation.substring(0, translation.length() - 2);
                }

                SpannableString wordSpanned = new SpannableString(word + ":");
                wordSpanned.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View view) {

                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);

                        ds.setUnderlineText(false);
                    }
                }, 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                mTranslationView.setText(wordSpanned);
                mTranslationView.append(" " + translation);
                mTranslationView.setVisibility(View.VISIBLE);

                startHideExplanationTimer();
            }

            @Override
            public void onFailure(Call<ICibaResponse> call, Throwable t) {
                Log.e(TAG, "queryWordFromICiba.onFailure: ", t);

                mTranslationView.setText(String.format(getResources().getString(R.string.word_not_queried), word));
                mTranslationView.setVisibility(View.VISIBLE);

                startHideExplanationTimer();
            }
        });
    }

    private void queryWordFromYoudao(String word) {
        String youDaoBaseUrl = "http://fanyi.youdao.com/openapi.do/";
        String youDaoKeyFrom = "video-trans";
        String youDaoKey = "44985642";
        String youDaoType = "data";
        String youDaoDocType = "json";
        String youDaoVersion = "1.1";

        Map<String, Object> searchFilters = new LinkedHashMap<>();
        searchFilters.put("keyfrom", youDaoKeyFrom);
        searchFilters.put("key", youDaoKey);
        searchFilters.put("type", youDaoType);
        searchFilters.put("doctype", youDaoDocType);
        searchFilters.put("version", youDaoVersion);
        searchFilters.put("q", word);

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(youDaoBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        OnlineQueryService service = retrofit.create(OnlineQueryService.class);
        service.queryWordFromYoudao(searchFilters).enqueue(new Callback<YoudaoResponse>() {
            @Override
            public void onResponse(Call<YoudaoResponse> call, Response<YoudaoResponse> response) {
                String translation = StringUtils.join(response.body().getBasic().getExplains(), "\n");
                mTranslationView.setText(translation);
            }

            @Override
            public void onFailure(Call<YoudaoResponse> call, Throwable t) {
                Log.e(TAG, "onEnglishWordClicked.onFailure: ", t);
            }
        });
    }

    private VideoView.OnCurrentTimeUpdateListener mOnCurrentTimeUpdateListener = new VideoView.OnCurrentTimeUpdateListener() {
        @Override
        public void onCurrentTimeUpdate(int currentTime) {
            if(!mIsTouchingVideoSeekBar) {
                mVideoControllerCurrentTime.setText(StringUtils.getTimeDisplayString(currentTime));

                if(mVideoView.getDuration() != 0) {
                    mVideoControllerVideoSeekBar.setProgress(currentTime * 100 / mVideoView.getDuration());
                }
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

    private View.OnClickListener mOnSettingsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mIsOnlyEnglishSubtitle = !mIsOnlyEnglishSubtitle;

            mVideoView.updateTimedText();
        }
    };

    private View.OnClickListener mOnSubtitleCloseButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mOnlineSubtitleController.setVisibility(View.INVISIBLE);
        }
    };

    private View.OnClickListener mOnSubtitleSearchButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String searchKeyword = mOnlineSubtitleSearchBox.getText().toString().trim();
            searchSubtitleFromShooter(searchKeyword);
        }
    };

    private View.OnClickListener mOnlineSubtitleButtonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            mOnlineSubtitleController.setVisibility(View.VISIBLE);
            mOnlineSubtitleCloseButton.setOnClickListener(mOnSubtitleCloseButtonClickListener);
            mOnlineSubtitleSearchButton.setOnClickListener(mOnSubtitleSearchButtonClickListener);
            mOnlineSubtitleSearchBox.setText(mVideoInfo.getTitle());

            searchSubtitleFromShooter(mVideoInfo.getTitle());
        }
    };

    private void searchSubtitleFromShooter(String keyword) {
        String shooterBaseUrl = "http://api.assrt.net/v1/sub/search/";
        String shooterToken = "5fjG5Znw0KgfqL1QmDffB3A7qzaGAXzF";
        String count = "20";
        String page = "0";

        Map<String, Object> searchFilters = new LinkedHashMap<>();
        searchFilters.put("token", shooterToken);
        searchFilters.put("cnt", count);
        searchFilters.put("pos", page);
        searchFilters.put("q", keyword);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(shooterBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OnlineQueryService service = retrofit.create(OnlineQueryService.class);
        service.querySubtitleFromShooter(searchFilters).enqueue(new Callback<ShooterSubtitleResponse>() {
            @Override
            public void onResponse(Call<ShooterSubtitleResponse> call, Response<ShooterSubtitleResponse> response) {
                if(response.isSuccessful() && response.body() != null && response.body().getSub() != null) {
                    RecyclerView onlineSubtitleRecyclerView =
                            (RecyclerView) mOnlineSubtitleController.findViewById(R.id.online_subtitle_recycler_view);

                    OnlineSubtitleAdapter onlineSubtitleAdapter = new OnlineSubtitleAdapter(VideoActivity.this, response.body().getSub().getSubs());

                    onlineSubtitleRecyclerView.setAdapter(onlineSubtitleAdapter);
                }
            }

            @Override
            public void onFailure(Call<ShooterSubtitleResponse> call, Throwable t) {
                Log.e(TAG, "querySubtitleFromShooter.onFailure: ", t);
            }
        });
    }

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

            if(mHideVideoControllerTimer != null) {
                mHideVideoControllerTimer.cancel();
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mIsTouchingVideoSeekBar = false;
            mVideoView.seekTo(seekBar.getProgress() * mVideoView.getDuration() / 100);

            startHideVideoControllerTimer();
        }
    };
}
