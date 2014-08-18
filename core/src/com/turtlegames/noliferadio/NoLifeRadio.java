package com.turtlegames.noliferadio;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import javax.media.CannotRealizeException;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class NoLifeRadio extends ApplicationAdapter {
    private SpriteBatch spriteBatch;
    private Stage stage;
    private TextButton buttonPlay;
    private Skin skin;
    private Player mediaPlayer;
    private MediaPlayerThread mediaPlayerRunnable;
    private Thread mediaPlayerThread;
    private String mediaURL;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        //TODO Check UITest on LibGDX Tests for button with Image
        buttonPlay = new TextButton("Play", skin, "default");
        //		buttonPlay.setWidth(64f);
        //		buttonPlay.setHeight(64f);
        buttonPlay.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (mediaPlayerRunnable == null) {
                    mediaPlayerRunnable = new MediaPlayerThread();
                    mediaPlayerThread = new Thread(mediaPlayerRunnable);
                    mediaPlayerThread.start();
                }
            }
        });

        stage.addActor(buttonPlay);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        stage.draw();
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        mediaPlayerThread.interrupt();
    }

    private class MediaPlayerThread implements Runnable {
        @Override
        public void run() {
            URL mediaURL = null;
            try {
                mediaURL = new URL("http://radio.nolife-radio.com:9000/stream");
                mediaPlayer = Manager.createRealizedPlayer(mediaURL);
                mediaPlayer.start();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (NoPlayerException e) {
                e.printStackTrace();
            } catch (CannotRealizeException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}