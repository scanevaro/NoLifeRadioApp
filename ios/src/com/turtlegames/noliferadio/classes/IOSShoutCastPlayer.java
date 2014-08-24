package com.turtlegames.noliferadio.classes;

import com.turtlegames.noliferadio.interfaces.Player;
import org.robovm.apple.mediaplayer.MPMoviePlayerViewController;

/**
 * Created by scanevaro on 23/08/2014.
 */
public class IOSShoutCastPlayer implements Player {

    private MPMoviePlayerViewController playerViewController;

    public IOSShoutCastPlayer(){
        playerViewController = new MPMoviePlayerViewController();
    }

    @Override
    public void play() {

    }
}
