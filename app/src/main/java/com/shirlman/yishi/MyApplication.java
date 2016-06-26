package com.shirlman.yishi;

import android.app.Application;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by KB-Server on 2016/6/24.
 */
public class MyApplication extends Application {
    private static MyApplication mMyApplication;
    private static JobManager mJobManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mMyApplication = this;

        initImageLoader();
    }

    public static MyApplication getInstance() {
        return mMyApplication;
    }

    public static JobManager getJobManager() {
        if(mJobManager == null) {
            Configuration jobConfiguration = new Configuration.Builder(mMyApplication.getApplicationContext())
                    .minConsumerCount(5)//always keep at least one consumer alive
                    .maxConsumerCount(10)//up to 5 consumers at a time
                    .loadFactor(5)//5 jobs per consumer
                    //.consumerKeepAlive(120)//wait 2 minute
                    .build();

            mJobManager = new JobManager(jobConfiguration);
        }

        return mJobManager;
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration imageLoaderConfiguration =
                new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();

        ImageLoader.getInstance().init(imageLoaderConfiguration);
    }
}
