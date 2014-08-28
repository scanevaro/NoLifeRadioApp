package com.turtlegames.noliferadio.classes;

import com.turtlegames.noliferadio.interfaces.Player;
import org.robovm.apple.foundation.NSURL;
import org.robovm.apple.mediaplayer.MPMoviePlayerController;
import org.robovm.apple.mediaplayer.MPMoviePlayerViewController;
import org.robovm.apple.mediaplayer.MPMovieSourceType;

/**
 * Created by scanevaro on 23/08/2014.
 */
public class IOSShoutCastPlayer implements Player
{
	private MPMoviePlayerController player;

	public IOSShoutCastPlayer()
	{
		player = new MPMoviePlayerViewController().getMoviePlayer();
	}

	@Override
	public void play(String url)
	{
		player.setMovieSourceType(MPMovieSourceType.Streaming);

		player.setContentURL(new NSURL(url));

        player.setShouldAutoplay(true);

		player.play();
	}
}
