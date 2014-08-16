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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class NoLifeRadio extends ApplicationAdapter implements Runnable {
    private SpriteBatch spriteBatch;
    private Stage stage;
    private TextButton buttonPlay;
    private Skin skin;
    private MediaPlayer mediaPlayer;
    private MediaPlayerThread mediaPlayerThread;

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
                if (mediaPlayerThread == null) {
                    mediaPlayerThread = new MediaPlayerThread();
                    new Thread(mediaPlayerThread).start();
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
    public void run() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer(new Media("http://radio.nolife-radio.com:8000"));
        }
    }

    @Override
    public void stop() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();
    }

    @Override
    public void dispose() {
        runnable.stop();
    }

    private class MediaPlayerThread implements Runnable {
        @Override
        public void run() {
            if (mediaPlayer == null)
                mediaPlayer.play();
        }
    }
}