package com.turtlegames.noliferadio.android.classes;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.PowerManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.turtlegames.noliferadio.NoLifeRadio;
import com.turtlegames.noliferadio.android.AndroidLauncher;
import com.turtlegames.noliferadio.android.R;

import java.io.IOException;

/**
 * Created by scanevaro on 05/10/2014.
 */
public class MediaPlayerService extends Service {
    private final int NOTIFICATION_ID = 9999;
    private MediaPlayer mediaPlayer;
    private WifiManager.WifiLock wifiLock;
    private static boolean playing;
    private final String url = "http://radio.nolife-radio.com:9000/stream";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        }

        try {
            if (!mediaPlayer.isPlaying()) {
                setPlaying(true);

                mediaPlayer.setDataSource(url);

                //Get state label
                if (((NoLifeRadio) Gdx.app.getApplicationListener()).state != null) {
                    final Label state = ((NoLifeRadio) Gdx.app.getApplicationListener()).state;

                    state.setText("Buffering..");
                    state.setColor(Color.YELLOW);

                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                            state.setText("Playing");
                            state.setColor(Color.GREEN);
                        }
                    });

                }
                mediaPlayer.prepareAsync();

                setWifiLock();
                addNotification();
            } else {
                setPlaying(false);

                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;

                wifiLock.release();
                wifiLock = null;

                final Label state = ((NoLifeRadio) Gdx.app.getApplicationListener()).state;
                state.setText("...IDLE...");
                state.setColor(Color.WHITE);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        setPlaying(false);

        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (wifiLock != null) {
            wifiLock.release();
            wifiLock = null;
        }
    }

    public static boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        if (playing)
            ((NoLifeRadio) Gdx.app.getApplicationListener()).buttonPlay.setChecked(true);
        else
            ((NoLifeRadio) Gdx.app.getApplicationListener()).buttonPlay.setChecked(false);

        this.playing = playing;
    }

    private void addNotification() {
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), AndroidLauncher.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle("NoLifeRadio")
                .setContentText("Playing")
                .setContentIntent(pi)
                .setSmallIcon(R.drawable.ic_launcher)
                .build();
//        notification.tickerText = text;
//        notification.icon = R.drawable.ic_launcher;
//        notification.flags |= Notification.FLAG_ONGOING_EVENT;
//        notification.setLatestEventInfo(getApplicationContext(), "NoLifeRadio",
//                "Playing", pi);
        startForeground(NOTIFICATION_ID, notification);
    }

    private void setWifiLock() {
        if (wifiLock == null) {
            wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                    .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");

            wifiLock.acquire();
        }

    }
}