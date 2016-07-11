package org.videolan.libvlc.subtitle;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

/**
 * Created by KB-Server on 2016/7/11.
 */
public class TimedTextUtil {

//    public static void startTimedTextProcessingTask(final Uri uri) {
//        mTimedTextProcessingTask = new AsyncTask() {
//            @Override
//            protected Object doInBackground(Object[] objects) {
//                try {
//                    URL url = new URL(uri.getPath());
//                    InputStream inputStream = url.openStream();
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Object o) {
//
//                super.onPostExecute(o);
//            }
//        };
//    }

    public static TimedTextObject getTimedTextObject(String path) {
        TimedTextObject timedTextObject = null;

        String subtitleFormat = path.substring(path.lastIndexOf(".") + 1).toLowerCase();
        String fileName = path.substring(0, path.lastIndexOf("."));

        if(fileName.lastIndexOf("/") != -1) {
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
        }

        try {
            InputStream inputStream = new FileInputStream(path);

            switch (subtitleFormat) {
                case SubtitleFormat.ASS:
                    timedTextObject = new FormatASS().parseFile(fileName, inputStream);
                    break;
                case SubtitleFormat.SCC:
                    timedTextObject = new FormatSCC().parseFile(fileName, inputStream);
                    break;
                case SubtitleFormat.SRT:
                    timedTextObject = new FormatSRT().parseFile(fileName, inputStream);
                    break;
                case SubtitleFormat.STL:
                    timedTextObject = new FormatSTL().parseFile(fileName, inputStream);
                    break;
                case SubtitleFormat.TTML:
                    timedTextObject = new FormatTTML().parseFile(fileName, inputStream);
                    break;
                default:
                    path = getSubtitlePathUnderSameFolder(path);

                    if(path != null) {
                        timedTextObject = getTimedTextObject(path);
                    }

                    break;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FatalParsingException e) {
            e.printStackTrace();
        }

        return timedTextObject;
    }

    private static  String getSubtitlePathUnderSameFolder(String path){
        String subtitlePath = null;
        String prefix = path.substring(0, path.lastIndexOf(".") + 1);

        String[] supportSubtitleFormat = new String[] {
                SubtitleFormat.ASS,
                SubtitleFormat.SCC,
                SubtitleFormat.SRT,
                SubtitleFormat.STL,
                SubtitleFormat.TTML
        };

        for(String subtitleFormat : supportSubtitleFormat){
            String subPath = prefix + subtitleFormat.toLowerCase();

            if(new File(subPath).exists()){
                subtitlePath = subPath;

                break;
            }
        }

        return subtitlePath;
    }
}
