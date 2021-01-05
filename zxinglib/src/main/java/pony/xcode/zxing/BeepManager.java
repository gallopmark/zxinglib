package pony.xcode.zxing;
/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;

import java.io.Closeable;

/**
 * Manages beeps and vibrations for {@link Activity}.
 */
public final class BeepManager implements MediaPlayer.OnErrorListener, Closeable {

    private static final String TAG = BeepManager.class.getSimpleName();

    private static final long VIBRATE_DURATION = 200L;

    private final Activity activity;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private boolean vibrate;

    BeepManager(Activity activity) {
        this.activity = activity;
        this.mediaPlayer = null;
        updatePrefs();
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public void setPlayBeep(boolean playBeep) {
        this.playBeep = playBeep;
    }

    synchronized void updatePrefs() {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
//        shouldBeep(prefs, activity);
//        vibrate = prefs.getBoolean(Preferences.KEY_VIBRATE, false);
        if (playBeep) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it too loud,
            // so we now play on the music stream.
            activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
            ensureMediaPlayer();
        }
    }

    synchronized void ensureMediaPlayer() {
        if (mediaPlayer == null) {
            try {
                mediaPlayer = MediaPlayer.create(activity, R.raw.zxl_beep);
                mediaPlayer.setLooping(false);
                mediaPlayer.setOnErrorListener(this);
            } catch (Exception e) {
                Log.w(TAG, e);
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
            }
        }
    }

    synchronized void playBeepSoundAndVibrate() {
        if (playBeep) {
            ensureMediaPlayer();
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null)
                vibrator.vibrate(VIBRATE_DURATION);
        }
    }

//    private static boolean shouldBeep(SharedPreferences prefs, Context activity) {
//        boolean shouldPlayBeep = prefs.getBoolean(Preferences.KEY_PLAY_BEEP, false);
//        if (shouldPlayBeep) {
//            // See if sound settings overrides this
//            AudioManager audioService = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
//            if (audioService != null && audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
//                shouldPlayBeep = false;
//            }
//        }
//        return shouldPlayBeep;
//    }

    @Override
    public synchronized boolean onError(MediaPlayer mp, int what, int extra) {
        // possibly media player error, so release and recreate
        close();
        ensureMediaPlayer();
        return true;
    }

    @Override
    public synchronized void close() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}