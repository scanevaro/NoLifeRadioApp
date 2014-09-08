package com.turtlegames.noliferadio.classes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.turtlegames.noliferadio.interfaces.Player;
import org.robovm.apple.foundation.NSObject;
import org.robovm.apple.foundation.NSURL;
import org.robovm.apple.mediaplayer.MPMovieLoadState;
import org.robovm.apple.mediaplayer.MPMoviePlayerController;
import org.robovm.apple.mediaplayer.MPMoviePlayerViewController;
import org.robovm.apple.mediaplayer.MPMovieSourceType;
import org.robovm.objc.block.VoidBlock1;

/**
 * Created by scanevaro on 23/08/2014.
 */
public class IOSShoutCastPlayer implements Player {
    private MPMoviePlayerController player;
    private NSObject observerLoadStateDidChange;
    private Label status;

    public IOSShoutCastPlayer() {
        player = new MPMoviePlayerViewController().getMoviePlayer();
    }

    @Override
    public void play(String url, final Label status) {
        this.status = status;

        player.setMovieSourceType(MPMovieSourceType.Streaming);

        observerLoadStateDidChange = MPMoviePlayerController.Notifications.observeLoadStateDidChange(
                player,
                new VoidBlock1<MPMoviePlayerController>() {
                    @Override
                    public void invoke(MPMoviePlayerController player) {
                        MPMovieLoadState loadState = player.getLoadState();
                        /* The load state is not known at this time. */
                        if (loadState.contains(MPMovieLoadState.Unknown)) {
//                            overlayController.setLoadStateDisplayString("unknown");
                            status.setText("...IDLE...");
                        }
                        /*
                         * The buffer has enough data that playback can begin,
                         * but it may run out of data before playback finishes.
                         */
                        if (loadState.contains(MPMovieLoadState.Playable)) {
//                            overlayController.setLoadStateDisplayString("playable");
                            status.setText("Ready");
                        }
                        /*
                         * Enough data has been buffered for playback to
                         * continue uninterrupted.
                         */
                        if (loadState.contains(MPMovieLoadState.PlaythroughOK)) {
                            // Add an overlay view on top of the movie view
//                            addOverlayView();
//                            overlayController.setLoadStateDisplayString("playthrough ok");
                            status.setText("Buffering");
                        }
                        /* The buffering of data has stalled. */
                        if (loadState.contains(MPMovieLoadState.Stalled)) {
//                            overlayController.setLoadStateDisplayString("stalled");
                            status.setText("Stalled");
                        }
                    }
                });

        player.setContentURL(new NSURL(url));

        player.setShouldAutoplay(true);

        player.play();

        //status.setText("Playing");
        //status.setColor(Color.GREEN);
    }
}
