package com.turtlegames.noliferadio.android.classes;

import android.content.Context;
import android.content.Intent;
import com.turtlegames.noliferadio.interfaces.Player;

/**
 * Created by scanevaro on 22/08/2014.
 */
public class AndroidSHOUTCastPlayer implements Player {
    private Context context;

    public AndroidSHOUTCastPlayer(Context context) {
        this.context = context;
    }

    @Override
    public void play() {
        context.startService(new Intent().setClass(context, MediaPlayerService.class));
    }

    @Override
    public void stop() {
        context.stopService(new Intent().setClass(context, MediaPlayerService.class));
    }

    @Override
    public boolean isPlaying() {
        return MediaPlayerService.isPlaying();
    }
}