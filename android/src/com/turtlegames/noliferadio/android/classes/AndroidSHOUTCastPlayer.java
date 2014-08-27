package com.turtlegames.noliferadio.android.classes;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import com.turtlegames.noliferadio.interfaces.Player;

import java.io.IOException;

/**
 * Created by scanevaro on 22/08/2014.
 */
public class AndroidSHOUTCastPlayer implements Player
{

	MediaPlayer mediaPlayer;

	public AndroidSHOUTCastPlayer()
	{
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

	}

	@Override
	public void play(String url)
	{
		if (!mediaPlayer.isPlaying())
		{
			try
			{
				mediaPlayer.setDataSource(url);
			}
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch (IllegalStateException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener()
			{
				public void onBufferingUpdate(MediaPlayer mp, int percent)
				{
					Log.i("Buffering", "" + percent);
				}
			});

			mediaPlayer.prepareAsync();

			mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
			{

				public void onPrepared(MediaPlayer mp)
				{
					mp.start();
				}
			});
		}
	}
}
