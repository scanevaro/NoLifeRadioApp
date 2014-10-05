package com.turtlegames.noliferadio;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.turtlegames.noliferadio.classes.IOSShoutCastPlayer;
import org.robovm.apple.avfoundation.AVAudioSession;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.uikit.UIApplication;

public class IOSLauncher extends IOSApplication.Delegate {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();

        if (!AVAudioSession.sharedInstance().setCategory("AVAudioSessionCategoryPlayback", new NSError.NSErrorPtr()))
            System.out.println("Error with AVAudioSession");

        return new IOSApplication(new NoLifeRadio(new IOSShoutCastPlayer()), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}