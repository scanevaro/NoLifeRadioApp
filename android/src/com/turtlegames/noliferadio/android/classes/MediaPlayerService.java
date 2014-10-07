package com.turtlegames.noliferadio.android.classes;


import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.turtlegames.noliferadio.NoLifeRadio;

import java.io.IOException;

/**
 * Created by scanevaro on 05/10/2014.
 */
public class MediaPlayerService extends Service {
    private MediaPlayer mediaPlayer;
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
        }

        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.reset();

            try {
                mediaPlayer.setDataSource(url);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Get state label
            final Label state = ((NoLifeRadio) Gdx.app.getApplicationListener()).state;

            mediaPlayer.prepareAsync();

            state.setText("Buffering..");
            state.setColor(Color.YELLOW);

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    setPlaying(true);
                    state.setText("Playing");
                    state.setColor(Color.GREEN);
                }
            });
        } else {
            mediaPlayer.stop();

            final Label state = ((NoLifeRadio) Gdx.app.getApplicationListener()).state;
            state.setText("...IDLE...");
            state.setColor(Color.WHITE);

            setPlaying(false);
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        setPlaying(false);

        if (mediaPlayer != null)
            mediaPlayer.stop();
    }

    public static boolean isPlaying() {
        if (playing)
            ((NoLifeRadio) Gdx.app.getApplicationListener()).buttonPlay.setChecked(true);
        else
            ((NoLifeRadio) Gdx.app.getApplicationListener()).buttonPlay.setChecked(false);

        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }
}