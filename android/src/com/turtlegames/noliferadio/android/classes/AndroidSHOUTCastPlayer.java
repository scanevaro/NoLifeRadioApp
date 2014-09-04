package com.turtlegames.noliferadio.android.classes;

import android.media.AudioManager;
import android.media.MediaPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
	public void play(String url, final Label state)
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
					state.setText("Buffering..");
					mediaPlayer.setOnBufferingUpdateListener(null);
				}
			});

			mediaPlayer.prepareAsync();
			state.setText("Buffering..");
			state.setColor(Color.YELLOW);

			mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
			{

				public void onPrepared(MediaPlayer mp)
				{
					mp.start();
					state.setText("Playing");
					state.setColor(Color.GREEN);
				}
			});
		}
	}

	public MediaPlayer.TrackInfo[] getTrackInfo()
	{
		return null;
	}
}
