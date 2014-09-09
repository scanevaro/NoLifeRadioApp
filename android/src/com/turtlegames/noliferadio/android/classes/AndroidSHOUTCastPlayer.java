package com.turtlegames.noliferadio.android.classes;

import android.media.AudioManager;
import android.media.MediaPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.turtlegames.noliferadio.interfaces.Player;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by scanevaro on 22/08/2014.
 */
public class AndroidSHOUTCastPlayer implements Player {
    private MediaPlayer mediaPlayer;
    private MediaPlayer.TrackInfo[] trackInfo;

    public AndroidSHOUTCastPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void play(String url, final Label state, final Label songName) {
        if (!mediaPlayer.isPlaying()) {
            try {
                mediaPlayer.setDataSource(url);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    state.setText("Buffering..");
                    state.setColor(Color.YELLOW);
                }
            });

            mediaPlayer.prepareAsync();

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    state.setText("Playing");
                    state.setColor(Color.GREEN);
                    getNowPlaying(songName);
                }
            });
        }
    }

    private void getNowPlaying(final Label songName) {
        new Thread(new Runnable() {
            public void run() {
                String title = null, djName = null;
                try {
                    URL updateURL = new URL("http://radio.nolife-radio.com:9000/stream");
                    URLConnection conn = updateURL.openConnection();
                    conn.setRequestProperty("Icy-MetaData", "1");
                    int interval = Integer.valueOf(conn.getHeaderField("icy-metaint")); // You can get more headers if you wish. There is other useful data.

                    InputStream is = conn.getInputStream();

                    int skipped = 0;
                    while (skipped < interval) {
                        skipped += is.skip(interval - skipped);
                    }

                    int metadataLength = is.read() * 16;

                    int bytesRead = 0;
                    int offset = 0;
                    byte[] bytes = new byte[metadataLength];

                    while (bytesRead < metadataLength && bytesRead != -1) {
                        bytesRead = is.read(bytes, offset, metadataLength);
                        offset = bytesRead;
                    }

                    String metaData = new String(bytes).trim();
                    //parse song name
                    String song = metaData.substring(metaData.indexOf("=") + 2, metaData.indexOf(";") - 1);

                    //set song name in label
                    songName.setText(song);
                    songName.setColor(Color.ORANGE);

                    is.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}