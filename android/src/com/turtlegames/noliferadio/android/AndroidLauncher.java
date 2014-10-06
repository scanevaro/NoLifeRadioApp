package com.turtlegames.noliferadio.android;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.turtlegames.noliferadio.NoLifeRadio;
import com.turtlegames.noliferadio.android.classes.AndroidSHOUTCastPlayer;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new NoLifeRadio(new AndroidSHOUTCastPlayer(this)), config);
    }
}
