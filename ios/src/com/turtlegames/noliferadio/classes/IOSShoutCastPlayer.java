package com.turtlegames.noliferadio.classes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.turtlegames.noliferadio.interfaces.Player;
import org.robovm.apple.foundation.NSURL;
import org.robovm.apple.mediaplayer.MPMoviePlaybackState;
import org.robovm.apple.mediaplayer.MPMoviePlayerController;
import org.robovm.apple.mediaplayer.MPMoviePlayerViewController;
import org.robovm.apple.mediaplayer.MPMovieSourceType;

/**
 * Created by scanevaro on 23/08/2014.
 */
public class IOSShoutCastPlayer implements Player, Runnable {
    private MPMoviePlayerController player;
    private MPMoviePlaybackState listener;
    private Label status;

    public IOSShoutCastPlayer() {
        player = new MPMoviePlayerViewController().getMoviePlayer();
        listener = player.getPlaybackState();
    }

    @Override
    public void play(String url, Label status) {
        this.status = status;

        player.setMovieSourceType(MPMovieSourceType.Streaming);

        player.setContentURL(new NSURL(url));

        player.setShouldAutoplay(true);

        player.play();
    }

    @Override
    public void run() {
        while (true) {
            if (listener.value() == MPMoviePlaybackState.Playing.ordinal()) {
                status.setText("Playing");
                status.setColor(Color.GREEN);
            }
        }
    }
}
